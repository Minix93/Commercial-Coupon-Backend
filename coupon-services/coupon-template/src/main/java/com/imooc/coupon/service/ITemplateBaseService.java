package com.imooc.coupon.service;

import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * basic services (view, delete, ...) for coupon template
 */
public interface ITemplateBaseService {


    /**
     * Get Coupon info according to id
     * @param id Template id
     * @return {@link CouponTemplate} CouponTemplate Entity
     * @throws CouponException
     */
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;


    /**
     * Find all usable coupon template
     * @return {@link CouponTemplateSDK}s
     */
    List<CouponTemplateSDK> findAllUsableTemplate();


    /**
     * Obtain the mapping from ids to CouponTemplateSDK
     * @param ids template ids
     * @return Map<key: template id, value: CouponTemplateSDK>
     */
    Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids);


}
