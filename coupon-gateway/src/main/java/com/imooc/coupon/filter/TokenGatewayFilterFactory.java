package com.imooc.coupon.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {


    @Resource
    TokenGatewayFilter tokenGatewayFilter;

    @Override
    public GatewayFilter apply(Object config) {
        return tokenGatewayFilter;
    }
}
