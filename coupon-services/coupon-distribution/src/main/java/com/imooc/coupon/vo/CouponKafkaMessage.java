package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Object of Kafka message
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponKafkaMessage {

    /**
     * Coupon Status
     */
    private Integer status;


    /**
     * Coupon primary key
     */
    private List<Integer> ids;


}
