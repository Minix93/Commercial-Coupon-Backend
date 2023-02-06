package com.imooc.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <h1>Check the token in the request</h1>
 */
@Slf4j
@Component
public class TokenGatewayFilter implements GatewayFilter, Ordered {


    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String requestMethod = exchange.getRequest().getMethodValue();
        String requestURL = exchange.getRequest().getURI().getPath();

        log.info(String.format("%s request to %s"), requestMethod, requestURL);

        Object token = exchange.getRequest().getHeaders().getFirst("token");

        if (token == null){
            log.error("error: token is empty");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }


        return chain.filter(exchange);
    }

    /**
     * @return the int order of current filter
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
