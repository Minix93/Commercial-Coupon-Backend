package com.imooc.coupon.controller;

import com.imooc.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Health checking interface
 */
@Slf4j
@RestController
public class HealthCheck {

    /**
     * discover services
     */
    private final DiscoveryClient client;

    /**
     * service registration interface,
     */
    private final Registration registration;

    public HealthCheck(DiscoveryClient client, Registration registration) {
        this.client = client;
        this.registration = registration;
    }

    /**
     * Health checking interface
     * 127.0.0.1:7001/coupon-template/health
     * @return
     */
    @GetMapping("/health")
    public String health(){
        log.debug("view health api");
        return "CouponTemplate Is Ok";
    }

    /**
     * Exception test interface
     * @return
     * @throws CouponException
     */
    @GetMapping("/exception")
    public String exception() throws CouponException {

        log.debug("view exception api");
        throw new CouponException("CouponTemplate Has Some Problem");
    }

    /**
     * Get microservices info from Eureka Server
     * @return
     */
    @GetMapping("/info")
    public List<Map<String, Object>> info(){
        // waiting 2 min to get registration info
        List<ServiceInstance> instances =
                client.getInstances(registration.getServiceId());
        List<Map<String, Object>> result = new ArrayList<>(instances.size());

        instances.forEach(i -> {
            Map<String, Object> info = new HashMap<>();
            info.put("serviceId", i.getServiceId());
            info.put("instanceId", i.getInstanceId());
            info.put("port",i.getPort());
            result.add(info);
        });
        return result;
    }

}
