package com.imooc.coupon.dao;

import com.imooc.coupon.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Coupon Template DAO interface
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer> {

    /** find template by the name of template */
    CouponTemplate findByName(String name);

    /**
     * find by available and expired
     * @param available
     * @param expired
     * @return
     */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);



    List<CouponTemplate> findAllByExpired(Boolean expired);
}
