package com.vv.personal.prom.dbi.interactor;

import com.google.protobuf.GeneratedMessageV3;

import java.sql.ResultSet;

/**
 * @author Vivek
 * @since 02/01/21
 */
public interface IDbi {

    ResultSet executeNonUpdateSql(String sql);

    int executeUpdateSql(String sql);

    default <T extends GeneratedMessageV3> int insertNewEntities(T t) throws Exception {
        String sql = "";
        return executeUpdateSql(sql);
    }

}
