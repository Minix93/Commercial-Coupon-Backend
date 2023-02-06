package com.imooc.coupon.converter;

import com.imooc.coupon.constant.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DistributeTargetConverter
        implements AttributeConverter<DistributeTarget, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DistributeTarget attribute) {
        return attribute.getCode();
    }

    @Override
    public DistributeTarget convertToEntityAttribute(Integer dbData) {
        return DistributeTarget.of(dbData);
    }
}
