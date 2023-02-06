package com.imooc.coupon.converter;

import com.imooc.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * coupon category enum type converter
 * convert the category to its own code
 *
 */
@Converter
public class CouponCategoryConverter
        implements AttributeConverter <CouponCategory, String>{

    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    @Override
    public CouponCategory convertToEntityAttribute(String dbData) {
        return CouponCategory.of(dbData);
    }
}
