package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * fake goods info
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfo {

    /**
     * Goods type
     */
    private Integer type;


    /**
     * goods price
     */
    private Double price;

    /**
     * goods count
     */
    private Integer count;


}
