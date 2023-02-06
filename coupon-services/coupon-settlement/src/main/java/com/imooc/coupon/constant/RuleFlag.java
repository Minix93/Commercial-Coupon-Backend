package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum for rule categories
 */
@Getter
@AllArgsConstructor
public enum RuleFlag {

    GETTOTALOFFWHENEXCEED("rule for 001"),
    GETDISCOUNT("rule for 002"),
    GETTOTALOFFDIRECTLY("rule for 003"),

    GETTOTOALOFF_GETDISCOUNT("rule for combination of 001 and 002");

    private String description;
}
