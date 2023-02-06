package com.imooc.coupon.converter;


import com.imooc.coupon.constant.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter for coupon status enum
 */
@Converter
public class CouponStatusConverter implements
        AttributeConverter<CouponStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CouponStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer dbData) {
        return CouponStatus.of(dbData);
    }
}
