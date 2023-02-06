package com.imooc.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IBuildTemplateService;
import com.imooc.coupon.service.ITemplateBaseService;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Controller for coupon template related functions
 */
@Slf4j
@RestController
public class CouponTemplateController {

    /**
     * Build coupon template service
     */
    private final IBuildTemplateService buildTemplateService;

    private final ITemplateBaseService templateBaseService;


    public CouponTemplateController(IBuildTemplateService buildTemplateService, ITemplateBaseService templateBaseService) {
        this.buildTemplateService = buildTemplateService;
        this.templateBaseService = templateBaseService;
    }

    /**
     * Build coupon template
     * 127.0.0.1:7001/coupon-template/template/build
     * 127.0.0.1:9000/coupon/coupon-template/template/build
     * @param request
     * @return
     * @throws CouponException
     */
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request) throws CouponException {
        log.info("Build Template: {}", JSON.toJSONString(request));
        return buildTemplateService.buildTemplate(request);
    }

    /**
     * info of the constructed coupon template
     * 127.0.0.1:7001/coupon-template/template/info?id=1
     * @param id
     * @return
     * @throws CouponException
     */
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id) throws CouponException {
        log.info("Build Template info For : {}", id);
        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     * lookup all usable coupon templates
     * 127.0.0.1:7001/coupon-template/template/sdk/all
     * @return
     */
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate(){
        log.info("Find all usable template");
        return templateBaseService.findAllUsableTemplate();
    }

    /**
     * Get mapping from templates ids to CouponTemplateSDK
     * 127.0.0.1:7001/coupon-template/template/sdk/infos
     * @param ids
     * @return
     */
    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(
           @RequestParam("ids") Collection<Integer> ids
    ){
        log.info("findIds2TemplateSDK: {}", JSON.toJSONString(ids));
        return templateBaseService.findIds2TemplateSDK(ids);
    }
}
