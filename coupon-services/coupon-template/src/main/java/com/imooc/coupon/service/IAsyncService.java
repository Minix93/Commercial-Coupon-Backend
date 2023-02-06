package com.imooc.coupon.service;

import com.imooc.coupon.entity.CouponTemplate;

/**
 * Async service interface
 */
public interface IAsyncService {

    /**
     * Construct coupon code according to template (async function)
     * @param template {@link CouponTemplate} Coupon template Entity
     */
    void asyncConstructCouponByTemplate(CouponTemplate template);
}
