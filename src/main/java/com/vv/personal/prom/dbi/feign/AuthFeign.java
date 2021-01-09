package com.vv.personal.prom.dbi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Vivek
 * @since 09/01/21
 */
@FeignClient("prom-v2-auth-service")
public interface AuthFeign {

    @PostMapping("/prom/auth/checkCred")
    Boolean checkCred(@RequestBody String incomingCred);
}
