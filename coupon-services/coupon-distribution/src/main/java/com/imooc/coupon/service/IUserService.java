package com.imooc.coupon.service;

import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.AcquireTemplateRequest;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;

import java.util.List;

/**
 * service interface for user
 * 1. present all status coupons for user
 * 2. check the coupon template that is availabe to acquire for user - coupon-template
 * 3. acquire coupon
 * 4. consume coupon - coupon-settlement
 *
 */
public interface IUserService {

    /**
     * find coupon records by userid and status
     * @param userId user id
     * @param status coupon status
     * @return {@link Coupon}s
     * @throws CouponException
     */
    List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException;


    /**
     * find current coupon template that is availabe for current user
     * @param userId user id
     * @return {@link CouponTemplateSDK}
     * @throws CouponException
     */
    List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException;

    /**
     * users acquire the coupon
     * @param request {@link AcquireTemplateRequest}
     * @return {@link Coupon}
     * @throws CouponException
     */
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;

    /**
     * Settlement of coupons
     * @param info {@link SettlementInfo}
     * @return {@link SettlementInfo}
     * @throws CouponException
     */
    SettlementInfo settlement(SettlementInfo info) throws CouponException;

}
