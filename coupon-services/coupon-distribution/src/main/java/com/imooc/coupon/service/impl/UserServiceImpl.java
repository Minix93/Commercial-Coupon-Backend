package com.imooc.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.dao.CouponDao;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.feign.SettlementClient;
import com.imooc.coupon.feign.TemplateClient;
import com.imooc.coupon.service.IRedisService;
import com.imooc.coupon.service.IUserService;
import com.imooc.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.Pair;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of user services
 * All operations and status are stored in redis, and deliver the message to MySQL by kafka
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final CouponDao couponDao;

    private final IRedisService redisService;

    private final TemplateClient templateClient;

    private final SettlementClient settlementClient;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserServiceImpl(CouponDao couponDao,
                           IRedisService redisService,
                           @Qualifier("com.imooc.coupon.feign.TemplateClient") TemplateClient templateClient,
                           @Qualifier("com.imooc.coupon.feign.SettlementClient") SettlementClient settlementClient,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     *
     * @param userId user id
     * @param status coupon status
     * @return
     * @throws CouponException
     */
    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {

        List<Coupon> curCached = redisService.getCachedCoupons(userId, status);
        List<Coupon> preTarget;

        if(CollectionUtils.isNotEmpty(curCached)){
            log.debug("coupon cache is not empty: {}, {}", userId, status);
            preTarget = curCached;
        } else {
            log.debug("coupon cache is empty, get coupon from db: {}, {}", userId, status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(
                    userId, CouponStatus.of(status)
            );
            // if there is no record in database, return directly
            if (CollectionUtils.isEmpty(dbCoupons)){
                log.debug("current user do not have coupon: {}, {}", userId, status);
                return dbCoupons;
            }

            // fill templateSDK field for dbCoupons
            Map<Integer, CouponTemplateSDK> id2TemplateSDK = templateClient.findIds2TemplateSDK(
                    dbCoupons.stream()
                            .map(Coupon::getTemplateId)
                            .collect(Collectors.toList())
            ).getData();

            dbCoupons.forEach(dc ->
                    dc.setTemplateSDK(
                            id2TemplateSDK.get(dc.getTemplateId())));
            // database has the record
            preTarget = dbCoupons;
            // write to cache
            redisService.addCouponToCache(
                    userId, preTarget, status
            );
        }

        // delete invalid coupon
        preTarget = preTarget.stream()
                .filter(c -> c.getId() != -1)
                .collect(Collectors.toList());

        // if the status is usable, need to check the expiration
        if (CouponStatus.of(status) == CouponStatus.USABLE){
            CouponClassify classify = CouponClassify.classify(preTarget);
            // if there exists expired coupons
            if (CollectionUtils.isNotEmpty(classify.getExpired())){
                log.info("Add expired Coupons to Cache From FindCouponsByStatus: {}, {}",
                        userId, status);
                redisService.addCouponToCache(userId, classify.getExpired(), CouponStatus.EXPIRED.getCode());
                // send message to kafka to process
                kafkaTemplate.send(
                        Constant.TOPIC,
                        JSON.toJSONString(new CouponKafkaMessage(
                                CouponStatus.EXPIRED.getCode(),
                                classify.getExpired().stream().map(Coupon::getId).collect(Collectors.toList())
                        ))
                );
            }

            return classify.getUsable();
        }

        return preTarget;
    }

    /**
     * find acquirable coupons for user by userId
     * @param userId user id
     * @return
     * @throws CouponException
     */
    @Override
    public List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException {

        long curTime = new Date().getTime();
        List<CouponTemplateSDK> templateSDKS = templateClient.findAllUsableTemplate().getData();

        log.debug("Find All template(from templateClient) Count: {}", templateSDKS.size());

        // filter out expired coupon template
        templateSDKS = templateSDKS.stream()
                .filter( t -> t.getRule().getExpiration().getDeadline() > curTime)
                .collect(Collectors.toList());

        log.info("Find Usable Template Count: {}", templateSDKS.size());

        // key: templateId
        // value: - left: template limitation
        //        - right: coupon template
        Map<Integer, Pair<Integer, CouponTemplateSDK>> limit2Template =
                new HashMap<>(templateSDKS.size());

        templateSDKS.forEach(
                t -> limit2Template.put(
                        t.getId(),
                        Pair.of(t.getRule().getLimitatNion(), t)
                )
        );

        List<CouponTemplateSDK> result = new ArrayList<>(limit2Template.size());
        List<Coupon> userUsableCoupons = findCouponsByStatus(userId, CouponStatus.USABLE.getCode());

        log.debug("Current User Has Usable Coupons: {}, {}", userId,
                userUsableCoupons);
        // key: templateId
        // value: coupon list
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        // judge if the coupon template is acquirable based on template rule
        limit2Template.forEach( (k,v) -> {

            int limitation = v.getFirst();
            CouponTemplateSDK templateSDK = v.getSecond();
            
            // user already obtained the maximum available number of coupons for current template
            if (templateId2Coupons.containsKey(k)
                    && templateId2Coupons.get(k).size() >= limitation){
                return;
            }
            result.add(templateSDK);
        });

        return result;
    }

    /**
     * User acquire coupons
     * 1. get coupons from TemplateClient and check expiration
     * 2. check limitation
     * 3. save to db
     * 4. fill CouponTemplateSDK
     * 5. save to cache
     * @param request {@link AcquireTemplateRequest}
     * @return
     * @throws CouponException
     */
    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {

        Map<Integer, CouponTemplateSDK> id2Template =
                templateClient.findIds2TemplateSDK(
                        Collections.singletonList(request.getTemplateSDK().getId())
                ).getData();

        if (id2Template.size() <= 0){
            log.error("Can Not Acquire Template From TemplateClient: {}",
                    request.getTemplateSDK().getId());
            throw new CouponException("Can Not Acquire Template From TemplateClient");
        }

        List<Coupon> userUsableCoupons = findCouponsByStatus(
                request.getUserId(), CouponStatus.USED.getCode()
        );
        Map<Integer, List<Coupon>> templateId2Coupons =
                userUsableCoupons
                        .stream()
                        .collect(Collectors.groupingBy(Coupon::getTemplateId));

        if (templateId2Coupons.containsKey(request.getTemplateSDK().getId())
                && templateId2Coupons.get(request.getTemplateSDK().getId()).size() >=
                 request.getTemplateSDK().getRule().getLimitatNion()   ){
            log.error("Exceed Template Assign Limitation");
            throw new CouponException("Exceed Template Assign Limitation");
        }

        String couponCode = redisService.tryToAcquireCouponCodeFromCache(
                request.getTemplateSDK().getId()
        );
        if (StringUtils.isEmpty(couponCode)){
            log.error("Can Not Acquire Coupon Code: {}",
                    request.getTemplateSDK().getId());
            throw new CouponException("Can Not Acquire Coupon Code");
        }

        Coupon newCoupon = new Coupon(
                request.getTemplateSDK().getId(),
                request.getUserId(),
                couponCode,
                CouponStatus.USABLE
        );
        newCoupon = couponDao.save(newCoupon);

        // fill CouponTemplateSDK field before putting into cache
        newCoupon.setTemplateSDK(request.getTemplateSDK());

        redisService.addCouponToCache(
          request.getUserId(),
          Collections.singletonList(newCoupon),
          CouponStatus.USABLE.getCode()
        );

        return newCoupon;
    }

    /**
     * Rule related operations will be processed by Settlement service
     * @param info {@link SettlementInfo}
     * @return
     * @throws CouponException
     */
    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CouponException {

        // when no coupon is delivered, return the original price directly
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos =
                info.getCouponAndTemplateInfos();
        if (CollectionUtils.isEmpty(ctInfos)){
            log.info("Empty Coupons  For Settle.");
            double goodsSum = 0.0;

            for (GoodsInfo gi : info.getGoodsInfos()){
                goodsSum += gi.getPrice() + gi.getCount();
            }

            info.setCost(retain2Decimals(goodsSum));
        }

        // validate if the coupon is owned by user
        List<Coupon> coupons = findCouponsByStatus(
                info.getUserId(), CouponStatus.USABLE.getCode()
        );
        Map<Integer, Coupon> id2Coupon = coupons.stream()
                .collect(Collectors.toMap(
                        Coupon::getId,
                        Function.identity()
                ));
        if (MapUtils.isEmpty(id2Coupon) || !CollectionUtils.isSubCollection(
                ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getId)
                        .collect(Collectors.toList()), id2Coupon.keySet()
        )) {
            log.info("{}", id2Coupon.keySet());
            log.info("{}", ctInfos.stream()
                    .map(SettlementInfo.CouponAndTemplateInfo::getId)
                    .collect(Collectors.toList()));
            log.error("User Coupon Has Some Problem, It Is Not SubCollection" +
                    "Of Coupons!");
            throw new CouponException("User Coupon Has Some Problem, " +
                    "It Is Not SubCollection Of Coupons!");
        }

        log.debug("Current Settlement Coupons Is User's: {}", ctInfos.size());

        List<Coupon> settleCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settleCoupons.add(id2Coupon.get(ci.getId())));

        // Get settlement info from settlement service
        SettlementInfo processedInfo =
                settlementClient.computeRule(info).getData();
        if (processedInfo.getEmploy() && CollectionUtils.isNotEmpty(
                processedInfo.getCouponAndTemplateInfos()
        )) {
            log.info("Settle User Coupon: {}, {}", info.getUserId(),
                    JSON.toJSONString(settleCoupons));
            // update cache
            redisService.addCouponToCache(
                    info.getUserId(),
                    settleCoupons,
                    CouponStatus.USED.getCode()
            );
            // update db
            kafkaTemplate.send(
                    Constant.TOPIC,
                    JSON.toJSONString(new CouponKafkaMessage(
                            CouponStatus.USED.getCode(),
                            settleCoupons.stream().map(Coupon::getId)
                                    .collect(Collectors.toList())
                    ))
            );
        }

        return null;
    }

    /**
     * Keep 2 decimals
     * @param value
     * @return
     */
    private double retain2Decimals(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
