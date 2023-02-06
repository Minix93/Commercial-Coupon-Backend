package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * User's coupon status
 */
@Getter
@AllArgsConstructor
public enum CouponStatus {

    USABLE("usable",1),
    USED("used",2),
    EXPIRED("expired(not used)", 3);

    private String description;

    private Integer code;


    public static CouponStatus of(Integer code){
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
