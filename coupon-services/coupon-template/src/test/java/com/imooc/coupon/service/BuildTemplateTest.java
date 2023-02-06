package com.imooc.coupon.service;


import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.CouponCategory;
import com.imooc.coupon.constant.DistributeTarget;
import com.imooc.coupon.constant.PeriodType;
import com.imooc.coupon.constant.ProductLine;
import com.imooc.coupon.vo.TemplateRequest;
import com.imooc.coupon.vo.TemplateRule;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Build coupon template test
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildTemplateTest {

    @Autowired
    private IBuildTemplateService buildTemplateService;

    @Test
    public void testBuildTemplate() throws Exception {
        System.out.println(JSON.toJSONString(
                buildTemplateService.buildTemplate(mockTemplateRequest())
        ));

        // sleep the main thread for 5 second to allow async service to finish
        Thread.sleep(5000);
    }

    /**
     * Mock TeamplateRequest
     * @return
     */
    private TemplateRequest mockTemplateRequest() {
        TemplateRequest request = new TemplateRequest();
        request.setName("CouponTemplate-" + new Date().getTime());
        request.setLogo("Coupon-Project");
        request.setDesc("This is a coupon template");
        request.setCategory(CouponCategory.GETTOTALOFFWHENEXCEED.getCode());
        request.setProductLine(ProductLine.CAT.getCode());
        request.setCount(10000);
        request.setUserId(10001L);
        request.setTarget(DistributeTarget.SINGLE.getCode());

        TemplateRule rule = new TemplateRule();
        rule.setExpiration(new TemplateRule.Expiration(
                PeriodType.SHIFT.getCode(),
                1, DateUtils.addDays(new Date(), 60).getTime()
        ));
        rule.setDiscount(new TemplateRule.Discount(5,1));
        rule.setLimitatNion(1);
        rule.setUsage(new TemplateRule.Usage(
                "BC","Vancouver",
                JSON.toJSONString(Arrays.asList("Furniture","Snack"))
        ));
        rule.setWeight(JSON.toJSONString(Collections.EMPTY_LIST));

        request.setRule(rule);

        return request;

    }

}
