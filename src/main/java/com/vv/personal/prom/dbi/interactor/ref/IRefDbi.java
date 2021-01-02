package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.dbi.interactor.IDbi;

import java.util.Collection;

import static com.vv.personal.prom.dbi.constants.Constants.INSERT_STMT_INT_STR;

/**
 * @author Vivek
 * @since 01/01/21
 */
public interface IRefDbi<T> extends IDbi {

    default int insertNewIntegerAndString(String table, Integer id, String detail) {
        String sql = String.format(INSERT_STMT_INT_STR, table, id, detail);
        return executeUpdateSql(sql);
    }

    int pushNewEntity(T t);

    int deleteEntity(Integer idToDel);

    Collection<Integer> selectAllIdsForTable(String table, String column);

    void populatePrimaryIds();

    void flushCache();
}
