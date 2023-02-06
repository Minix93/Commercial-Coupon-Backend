package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DistributeTarget {

    SINGLE("Single user (Users need to get coupon themselves)", 1),
    MULTI("Multi users (Coupon will automaticlaly discributed to users)", 2);


    private String description;

    private Integer code;

    public static DistributeTarget of(Integer code){
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists!"));
    }
}
