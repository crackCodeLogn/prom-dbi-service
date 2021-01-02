package com.vv.personal.prom.dbi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vivek
 * @since 01/01/21
 */
@Configuration
public class DbiConfigForRef extends AbstractDbiConfigurator {

    @Value("${db.ref.server.host:localhost}")
    private String dbServerHost;

    @Value("${db.ref.server.port:5432}")
    private int dbServerPort;

    @Value("${db.ref.name:ref}")
    private String dbName;

    @Value("${db.ref.user:postgres}")
    private String dbUser;

    @Value("${db.ref.cred}")
    private String dbCred;

    @Override
    public String getDbServerHost() {
        return dbServerHost;
    }

    @Override
    public Integer getDbServerPort() {
        return dbServerPort;
    }

    @Override
    public String getDbName() {
        return dbName;
    }

    @Override
    public String getDbUser() {
        return dbUser;
    }

    @Override
    public String getDbCred() {
        return dbCred;
    }

}
