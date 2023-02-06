package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Categories of coupon
 */

@Getter
@AllArgsConstructor
public enum CouponCategory {

    GETTOTALOFFWHENEXCEED("Get certain dollars off when total price meet the requirment", "001"),
    GETDISCOUNT("Get certain percentage off", "002"),
    GETTOTALOFFDIRECTLY("Get certain dollars off directly from the order", "003");


    /** Description (Category) of coupon */
    private String description;

    /** Code of coupon*/
    private String code;

    public static CouponCategory of(String code) {
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists!"));

    }

}
