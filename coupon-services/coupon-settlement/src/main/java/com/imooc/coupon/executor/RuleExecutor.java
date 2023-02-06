package com.imooc.coupon.executor;

import com.imooc.coupon.constant.RuleFlag;
import com.imooc.coupon.vo.SettlementInfo;

/**
 * Interface for coupon rule processing
 */
public interface RuleExecutor {


    /**
     * rule marker
     * @return {@link RuleFlag}
     */
    RuleFlag ruleConfig();

    /**
     * rule calculation for coupon
     * @param settlement {@link SettlementInfo} includes selected coupons
     * @return {@link SettlementInfo} modified settlement info
     */
    SettlementInfo computeRule(SettlementInfo settlement);
}
