package com.imooc.coupon.dao;

import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DAO interface for Coupon
 */
public interface CouponDao extends JpaRepository<Coupon, Integer> {


    /**
     * find all coupon record based on userId + coupon status
     * @param userId
     * @param status
     * @return list of coupons
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);

}
