package com.imooc.coupon.service;

import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.TemplateRequest;

/**
 * Interface to construct coupon template
 */
public interface IBuildTemplateService {


    /**
     * Create coupon template
     * @param request {@link TemplateRequest} template info request object
     * @return {@link CouponTemplate} coupon template entity
     * @throws CouponException
     */
    CouponTemplate buildTemplate(TemplateRequest request) throws CouponException;
}
