package com.imooc.coupon.service.impl;

import com.google.common.base.Stopwatch;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of AsyncService interface
 */

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    /**
     * Coupon Template DAO
     */
    private final CouponTemplateDao templateDao;

    /**
     * Redis Template
     */
    private final StringRedisTemplate redisTemplate;

    public AsyncServiceImpl(CouponTemplateDao templateDao, StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }

    @Async("getAsyncExecutor")
    @Override
    public void asyncConstructCouponByTemplate(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> couponCodes = buildCouponCode(template);

        String redisKey = String.format("%s%s",
                Constant.RedisPrefix.COUPON_TEMPLATE, template.getId().toString());
        log.info("Push CouponCode to Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));

        template.setAvailable(true);

        templateDao.save(template);

        watch.stop();
        log.info("Construct CouponCode By Template Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));


    }

    /**
     * Construct coupon code (18 digits for each coupon) :
     *  1~4 : productLine + category
     *  5~10 : randomized date (20221001)
     *  11~18: random range (0, 9)
     * @param template {@link CouponTemplate} Entity
     * @return Set<String> same size as template count
     */
    private Set<String> buildCouponCode(CouponTemplate template){
        Stopwatch watch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getCount());

        // 1~4
        String prefix4 = template.getProductLine().getCode().toString()
                + template.getCategory().getCode();


        String date = new SimpleDateFormat("yyMMdd")
                .format(template.getCreateTime());
        for (int i = 0; i != template.getCount(); ++i){
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        while(result.size() < template.getCount()){
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        assert result.size() == template.getCount();

        watch.stop();
        log.info("Build Coupon Code Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));

        return result;
    }


    /**
     * Construct last 14 digit for coupon code
     * @param date creation date of coupon
     * @return 14 digit coupon code
     */
    private String buildCouponCodeSuffix14(String date){
        char[] bases = new char[]{'1','2','3','4','5','6','7','8','9'};

        List<Character> chars = date.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        Collections.shuffle(chars);
        String mid6 = chars.stream().map(Objects::toString).collect(Collectors.joining());

        String suffix8 = RandomStringUtils.random(1,bases)
                + RandomStringUtils.randomNumeric(7);

        return mid6 + suffix8;
    }
}
