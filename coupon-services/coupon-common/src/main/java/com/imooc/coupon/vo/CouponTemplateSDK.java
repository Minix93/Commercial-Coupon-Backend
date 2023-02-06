package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Coupon template info used between microservices
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateSDK {

    /** Coupon template primary key*/
    private Integer id;

    /** Coupon template name*/
    private String name;

    /** Coupon logo*/
    private String logo;

    /** Coupon description*/
    private String desc;

    /** Coupon category*/
    private String category;

    /** Product Line*/
    private Integer productLine;

    /** Coupon template code*/
    private String key;

    /** target user*/
    private Integer target;

    /** Coupon Rule*/
    private TemplateRule rule;



}
