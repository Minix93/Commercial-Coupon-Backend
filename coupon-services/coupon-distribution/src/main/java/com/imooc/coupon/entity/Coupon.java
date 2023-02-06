package com.imooc.coupon.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.coupon.constant.CouponStatus;
import com.imooc.coupon.converter.CouponStatusConverter;
import com.imooc.coupon.serialization.CouponSerialize;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Coupon (record of the coupon received by users) entity table
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon")
@JsonSerialize(using = CouponSerialize.class)
public class Coupon {
    /**
     * Self increasec primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * relative coupon template primary key
     */
    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    /**
     * received user
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * coupon code
     */
    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    /**
     * Time the user receive the coupon
     */
    @CreatedDate
    @Column(name = "assign_time", nullable = false)
    private Date assignTime;


    /**
     * coupon status
     */
    @Basic
    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    /**
     * Relative coupon template info
     */
    @Transient
    private CouponTemplateSDK templateSDK;


    /**
     * @return invalid coupon object
     */
    public static Coupon invalidCoupon(){
        Coupon coupon = new Coupon();
        coupon.setId(-1);
        return coupon;
    }

    public Coupon(Integer templateId, Long userId, String couponCode,
                  CouponStatus status){
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }
}
