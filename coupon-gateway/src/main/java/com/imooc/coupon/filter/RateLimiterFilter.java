package com.imooc.coupon.filter;


import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RateLimiterFilter implements GatewayFilter, Ordered {

    /**
     * limit the token aquire to 2 per second
     */
    RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request =  exchange.getRequest();

        if(rateLimiter.tryAcquire()){
            log.info("get rate token success");
            return chain.filter(exchange);
        }
            log.error("rate limit: {}", request.getURI());
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
