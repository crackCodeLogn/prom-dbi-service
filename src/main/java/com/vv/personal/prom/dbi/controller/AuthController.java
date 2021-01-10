package com.vv.personal.prom.dbi.controller;

import com.vv.personal.prom.dbi.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Vivek
 * @since 09/01/21
 */
@RestController("DbiAuthController")
@RequestMapping("/prom/dbi/auth")
public class AuthController {
    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private Authenticator authenticator;

    @PostMapping("/checkCred")
    public Boolean checkCred(@RequestBody String incomingCred) {
        LOGGER.info("Received request to authenticate.");
        boolean authSuccess = authenticator.authorizeCred(extractClientIp(), incomingCred);
        if (authSuccess) LOGGER.info("Authentication result: {}", authSuccess);
        else LOGGER.warn("Authentication result: {}", authSuccess);
        return authSuccess;
    }

    public Boolean isAuthorized() {
        boolean isAuth = authenticator.isAuthorized(extractClientIp());
        if (isAuth) LOGGER.info("Authorized");
        else LOGGER.warn("Not Authorized");
        return isAuth;
    }

    private String extractClientIp() {
        String ipAddress = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        LOGGER.info("IP registered: {}", ipAddress);
        return ipAddress;
    }
}
