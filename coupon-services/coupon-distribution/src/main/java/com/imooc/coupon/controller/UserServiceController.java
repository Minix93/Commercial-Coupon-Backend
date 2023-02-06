package com.imooc.coupon.controller;


import com.alibaba.fastjson.JSON;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IUserService;
import com.imooc.coupon.vo.AcquireTemplateRequest;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class UserServiceController {

    private final IUserService userService;


    public UserServiceController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * find coupons by userId and status
     * @param userId
     * @param status
     * @return
     * @throws CouponException
     */
    @GetMapping("/coupons")
    public List<Coupon> findCouponByStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("status") Integer status) throws CouponException {
        log.info("Find Coupons By Status: {}, {}", userId, status);
        return userService.findCouponsByStatus(userId, status);
    }

    /**
     * Find available coupon template based on user Id
     * @param userId
     * @return
     * @throws CouponException
     */
    @GetMapping("/template")
    public List<CouponTemplateSDK> findAvailableTemplate(
           @RequestParam("userId") Long userId) throws CouponException{
        log.info("Find Available Template: {}", userId);
        return userService.findAvailableTemplate(userId);
    }

    /**
     * User acquire coupons
     * @param request
     * @return
     * @throws CouponException
     */
    @PostMapping("/acquire/template")
    public Coupon acquireTemplate(@RequestBody AcquireTemplateRequest request) throws CouponException{
        log.info("Acquire Template: {}", JSON.toJSONString(request));
        return userService.acquireTemplate(request);
    }


    /**
     * Coupon settlement
     * @param info
     * @return
     * @throws CouponException
     */
    @PostMapping("/settlement")
    public SettlementInfo settlement(@RequestBody SettlementInfo info)
            throws CouponException {

        log.info("Settlement: {}", JSON.toJSONString(info));
        return userService.settlement(info);
    }

}
