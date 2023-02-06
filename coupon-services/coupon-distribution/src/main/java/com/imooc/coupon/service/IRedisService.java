package com.imooc.coupon.service;


import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;

import java.util.List;

/**
 * Operations services for Redis
 * 1. cache for user's coupons
 * 2. cache for coupon code generated from templates
 */
public interface IRedisService {

    /**
     * find cached coupon list based on userId and coupon status
     * @param userId user's id
     * @param status coupon status {@link com.imooc.coupon.constant.CouponStatus}
     * @return {@link Coupon}s, may return null which represents that no records for current user
     */
    List<Coupon> getCachedCoupons(Long userId, Integer status);

    /**
     * Save empty coupon list to cache
     * This interface is used to prevent cache penetration
     * @param userId user id
     * @param status coupon status list
     */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    /**
     * Try to acquire a coupon code from cache
     * @param templateId coupon template primary key
     * @return coupon code
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * Save coupon to cache
     * @param userId user id
     * @param coupons {@link Coupon}s
     * @param status coupon status
     * @return number of coupons that has been successfully saved
     * @throws CouponException
     */
    Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException;
}
