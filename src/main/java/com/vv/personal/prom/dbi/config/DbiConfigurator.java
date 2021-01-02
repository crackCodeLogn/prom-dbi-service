package com.vv.personal.prom.dbi.config;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author Vivek
 * @since 01/01/21
 */
public interface DbiConfigurator {

    String getDbServerHost();

    Integer getDbServerPort();

    String getDbName();

    String getDbUser();

    String getDbCred();

    Connection getDbConnection();

    boolean closeDbConnection();

    Statement getStatement();
}
