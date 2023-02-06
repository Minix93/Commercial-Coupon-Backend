package com.imooc.coupon.executor.impl;

import com.imooc.coupon.constant.RuleFlag;
import com.imooc.coupon.executor.AbstractExecutor;
import com.imooc.coupon.executor.RuleExecutor;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Excutor for "Get total off when exceed"
 */
@Component
@Slf4j
public class GTOWEExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * @return
     */
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.GETTOTALOFFWHENEXCEED;
    }

    /**
     * @param settlement {@link SettlementInfo} includes selected coupons
     * @return
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(
                goodsCostSum(settlement.getGoodsInfos())
        );

        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );

        if (null != probability) {
            log.debug("Template doesn't match goodsType!");
            return probability;
        }

        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfos()
                .get(0).getTemplate();
        double base = (double) templateSDK.getRule().getDiscount().getBase();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        if (goodsSum < base ){
            log.debug("Current Goods Cost Sum < Coupon base");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfos(Collections.emptyList());
            return settlement;
        }

        settlement.setCost(retain2Decimals(
                (goodsSum - quota) > minCost() ? (goodsSum - quota ) : minCost()
        ));
        log.debug("Use Coupon to make goods cost from {} to {}", goodsSum, settlement.getCost());

        return settlement;
    }
}
