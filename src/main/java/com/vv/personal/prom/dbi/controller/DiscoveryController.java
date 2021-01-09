package com.vv.personal.prom.dbi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Vivek
 * @since 30/12/20
 */
@RestController("DiscoveryController")
public class DiscoveryController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private AuthController authController;

    @GetMapping("/lookup/{serviceId}")
    public List<ServiceInstance> lookup(@PathVariable String serviceId) {
        if (!authController.isAuthorized()) return null;
        return discoveryClient.getInstances(serviceId);
    }
}
