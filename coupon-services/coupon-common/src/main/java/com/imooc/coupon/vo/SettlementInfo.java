package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Settlement info
 * includes:
 *  1. userId
 *  2. goodsInfo
 *  3. couponList
 *  4. final price after the settlement
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {

    /**
     * User Id
     */
    private long userId;


    /**
     * Goods info
     */
    private List<GoodsInfo> goodsInfos;

    /**
     * List of coupons
     */
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;


    /**
     * Whether to validate the settlement
     */
    private Boolean employ;


    /**
     * final price after settlement
     */
    private Double cost;

    /**
     * coupon and template info
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponAndTemplateInfo{

        /**
         * Coupon primary key
         */
        private Integer id;

        /**
         * relative coupon template info
         */
        private CouponTemplateSDK template;
    }
}
