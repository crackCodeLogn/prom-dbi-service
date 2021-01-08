package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.dbi.interactor.IDbi;

import java.sql.ResultSet;
import java.util.Collection;

/**
 * @author Vivek
 * @since 01/01/21
 */
public interface IRefDbi<T, K> extends IDbi {

    int insertNewIntegerAndString(String table, Integer id, String detail);

    int pushNewEntity(T t);

    int deleteEntity(Integer idToDel);

    K retrieveAll();

    K retrieveSelective(); //TODO -- work on this later, not imp atm.

    T generateDetail(ResultSet resultSet);

    Collection<Integer> selectAllIdsForTable(String table, String column);

    void populatePrimaryIds();

    void flushCache();

    default int addToCacheOnSqlResult(Integer sqlResult, String table, Integer id) {
        if (sqlResult == 1) addToCache(table, id);
        return sqlResult;
    }

    void addToCache(String table, Integer id);
}
