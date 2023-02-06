package com.imooc.coupon.service.impl;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IAsyncService;
import com.imooc.coupon.service.IBuildTemplateService;
import com.imooc.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * implementation of buildtemplateservice interface
 */
@Slf4j
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {

    private final IAsyncService asyncService;

    private final CouponTemplateDao templateDao;

    public BuildTemplateServiceImpl(IAsyncService asyncService, CouponTemplateDao templateDao) {
        this.asyncService = asyncService;
        this.templateDao = templateDao;
    }

    /**
     * Create Coupon template entity
     * @param request {@link TemplateRequest} template info request object
     * @return {@link CouponTemplate} Coupon template Entity
     * @throws CouponException
     */
    @Override
    public CouponTemplate buildTemplate(TemplateRequest request) throws CouponException {

        // validate parameters
        if (!request.validate()){
            throw new CouponException("BuildTemplate Param Is Not Valid!");
        }

        // Check if coupon template with same name exists already
        if (null != templateDao.findByName(request.getName())) {
            throw new CouponException("Exist Same Name Template");
        }

        //Construct CouponTemplate and save to database
        CouponTemplate template = requestToTemplate(request);
        template = templateDao.save(template);

        // async generate coupon code based on the template
        asyncService.asyncConstructCouponByTemplate(template);

        return template;
    }

    /**
     * convert TemplateRequest to CouponTemplate
     * @param request
     * @return
     */
    private CouponTemplate requestToTemplate(TemplateRequest request){
        return new CouponTemplate(
                request.getName(),
                request.getLogo(),
                request.getDesc(),
                request.getCategory(),
                request.getProductLine(),
                request.getCount(),
                request.getUserId(),
                request.getTarget(),
                request.getRule()
        );
    }
}
