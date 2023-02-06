package com.imooc.coupon.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object for acquire coupon template request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {

    /**
     * user id
     */
    private Long userId;

    /**
     * coupon template SDK
     */
    private CouponTemplateSDK templateSDK;
}
