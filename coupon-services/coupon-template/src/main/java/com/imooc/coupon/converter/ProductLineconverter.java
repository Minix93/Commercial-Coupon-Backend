package com.imooc.coupon.converter;

import com.imooc.coupon.constant.ProductLine;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProductLineconverter
        implements AttributeConverter <ProductLine, Integer>{
    @Override
    public Integer convertToDatabaseColumn(ProductLine attribute) {
        return attribute.getCode();
    }

    @Override
    public ProductLine convertToEntityAttribute(Integer dbData) {
        return ProductLine.of(dbData);
    }
}
