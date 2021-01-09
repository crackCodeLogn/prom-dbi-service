package com.vv.personal.prom.dbi.auth;

import com.vv.personal.prom.dbi.feign.AuthFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vivek
 * @since 09/01/21
 */
public class Authenticator {
    private final Logger LOGGER = LoggerFactory.getLogger(Authenticator.class);

    private final AuthFeign authFeign;
    private final ConcurrentHashMap<String, Boolean> authStatusPerIp = new ConcurrentHashMap<>();

    public Authenticator(AuthFeign authFeign) {
        this.authFeign = authFeign;
    }

    public Boolean authorizeCred(String ip, String incomingCred) {
        Boolean isAuthSuccess = false;
        try {
            isAuthSuccess = authFeign.checkCred(incomingCred);
        } catch (Exception e) {
            LOGGER.error("Failed to authenticate. ", e);
        }
        addToAuthCache(ip, isAuthSuccess);
        return isAuthSuccess;
    }

    public void addToAuthCache(String ip, Boolean authStatus) {
        authStatusPerIp.put(ip, authStatus);
    }

    public Boolean isAuthorized(String ip) {
        if (authStatusPerIp.containsKey(ip)) return authStatusPerIp.get(ip);
        LOGGER.warn("IP {} not authenticated. It is advised to enter credentials in AuthController for op access!", ip);
        authStatusPerIp.put(ip, false);
        return false;
    }

    public void clearAuthCache() {
        authStatusPerIp.clear();
    }

    public Map<String, Boolean> getAuthStatusPerIp() {
        return authStatusPerIp;
    }
}
