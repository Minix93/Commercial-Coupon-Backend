package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Enum for Goods' types
 */
@Getter
@AllArgsConstructor
public enum GoodsType {

    ENTERTAIN("Entertainment", 1),
    FRESH("Fresh foods", 2),
    FURNITURE("furniture", 3),
    OTHERS("others", 4),
    ALL("all types", 5);

    private String description;

    private Integer code;

    public static GoodsType of(Integer code){
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(code + " not exists!")
                );
    }
}
