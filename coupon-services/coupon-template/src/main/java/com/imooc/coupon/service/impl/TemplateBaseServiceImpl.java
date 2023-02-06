package com.imooc.coupon.service.impl;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.ITemplateBaseService;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of templateBaseService
 */
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private final CouponTemplateDao templateDao;

    public TemplateBaseServiceImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }


    /**
     * Get template Info based on id
     * @param id Template id
     * @return {@link CouponTemplate} Coupon Template DAO
     * @throws CouponException
     */
    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {

        Optional<CouponTemplate> template = templateDao.findById(id);
        if(!template.isPresent()){
            throw new CouponException("Template is not exist: " + id);
        }

        return template.get();
    }

    /**
     * Find all currently usable coupon template
     * @return {@link CouponTemplateSDK}s
     */
    @Override
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        List<CouponTemplate> templates =
                templateDao.findAllByAvailableAndExpired(true,false);

        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    /**
     * Get the mapping from ids to CouponTemplateSDK
     * @param ids template ids
     * @return Map<Key: template id, value: CouponTemplateSDK>
     */
    @Override
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);

        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toMap(
                CouponTemplateSDK::getId, Function.identity()
        ));
    }


    /**
     * Convert CouponTemplate to CouponTemplateSDK
     * @param template
     * @return
     */
    private CouponTemplateSDK template2TemplateSDK(CouponTemplate template){
        return new CouponTemplateSDK(
          template.getId(),
          template.getName(),
          template.getLogo(),
          template.getDesc(),
          template.getCategory().getCode(),
          template.getProductLine().getCode(),
          template.getKey(),
          template.getTarget().getCode(),
          template.getRule()
        );
    }
}
