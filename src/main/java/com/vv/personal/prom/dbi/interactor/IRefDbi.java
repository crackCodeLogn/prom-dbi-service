package com.vv.personal.prom.dbi.interactor;

import java.util.Collection;

/**
 * @author Vivek
 * @since 01/01/21
 */
public interface IRefDbi extends IDbi {

    int insertNewIntegerAndString(Integer id, String detail);

    Collection<Integer> selectAllIdsForTable(String table, String column);

    void populatePrimaryIds();
}
