package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static com.vv.personal.prom.dbi.constants.Constants.INSERT_STMT_INT_STR;
import static com.vv.personal.prom.dbi.constants.Constants.SELECT_ALL_IDS;

/**
 * @author Vivek
 * @since 01/01/21
 */
public abstract class RefDbi<T, K> implements IRefDbi<T, K> {
    private final Logger LOGGER;
    protected final String TABLE;
    protected final String PRIMARY_COLUMN;
    protected final CachedRef cachedRef;
    private final DbiConfigForRef dbiConfigForRef;
    private final String createTableIfNotExistSql;
    private final ExecutorService singleWriterThread = Executors.newSingleThreadExecutor();
    private final ExecutorService multiReadThreads = Executors.newFixedThreadPool(4);

    public RefDbi(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef, String createTableIfNotExistSql, Logger logger) {
        this.TABLE = table;
        this.PRIMARY_COLUMN = primaryColumn;
        this.dbiConfigForRef = dbiConfigForRef;
        this.cachedRef = cachedRef;
        this.createTableIfNotExistSql = createTableIfNotExistSql;
        this.LOGGER = logger;

        LOGGER.info("Created handler for '{}'", TABLE);
    }

    @Override
    public ResultSet executeNonUpdateSql(String sql) {
        LOGGER.info("Executing SQL => {}", sql);
        Callable<ResultSet> nonUpdateSqlTask = () -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            try {
                ResultSet sqlResult = dbiConfigForRef.getStatement().executeQuery(sql);
                LOGGER.info("SQL completed => {}", sql);
                return sqlResult;
            } catch (SQLException throwables) {
                LOGGER.error("Failed to execute above SQL. ", throwables);
            } finally {
                stopWatch.stop();
                LOGGER.info("Non-update SQL execution complete in {}ms", stopWatch.getTime(TimeUnit.MILLISECONDS));
            }
            return null;
        };
        try {
            return multiReadThreads.submit(nonUpdateSqlTask).get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Interrupted / Gone bad while executing sql -> '{}'/ ", sql, e);
        }
        return null;
    }

    @Override
    public int executeUpdateSql(String sql) {
        LOGGER.info("Executing SQL => {}", sql);
        Callable<Integer> updateSqlTask = () -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            try {
                int sqlResult = dbiConfigForRef.getStatement().executeUpdate(sql);
                LOGGER.info("Result of above SQL {} => {}", sql, sqlResult);
                return sqlResult;
            } catch (SQLException throwables) {
                LOGGER.error("Failed to execute above SQL. ", throwables);
            } finally {
                stopWatch.stop();
                LOGGER.info("Update SQL execution complete in {}ms", stopWatch.getTime(TimeUnit.MILLISECONDS));
            }
            return -1;
        };
        try {
            return singleWriterThread.submit(updateSqlTask).get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Interrupted / Gone bad while executing sql -> '{}'. ", sql, e);
        }
        return -1;
    }

    @Override
    public int insertNewIntegerAndString(String table, Integer id, String detail) {
        String sql = String.format(INSERT_STMT_INT_STR, table, id, detail);
        int sqlExecResult = executeUpdateSql(sql);
        return addToCacheOnSqlResult(sqlExecResult, id);
    }

    protected int addToCacheOnSqlResult(Integer sqlExecResult, Integer id) {
        if (addToCacheOnSqlResult(sqlExecResult, TABLE, id) == 1) {
            LOGGER.debug("Cache for {} updated with id: {}", TABLE, id);
        } else {
            LOGGER.warn("Failed to update cache for {} with id: {}", TABLE, id);
        }
        return sqlExecResult;
    }

    @Override
    public Collection<Integer> selectAllIdsForTable(String table, String column) {
        List<Integer> ids = new ArrayList<>();
        int rowsReturned = 0;
        String sql = String.format(SELECT_ALL_IDS, column, table);
        try {
            ResultSet resultSet = executeNonUpdateSql(sql);
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    ids.add(resultSet.getInt(1));
                    rowsReturned++;
                } catch (SQLException throwables) {
                    LOGGER.error("Failed to completely extract result from the above select all query. ", throwables);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to execute / process sql '{}'. ", sql, e);
        }
        LOGGER.info("Received {} entries of select All for '{}' of table '{}'", rowsReturned, column, table);
        return ids;
    }

    @Override
    public int createTableIfNotExists() {
        return executeUpdateSql(createTableIfNotExistSql);
    }

    @Override
    public void addToCache(String table, Integer id) {
        getCachedRef().addNewIdToEntityCache(table, id);
    }

    @Override
    public void populatePrimaryIds() {
        getCachedRef().bulkAddNewIdsToEntityCache(TABLE, selectAllIdsForTable());
    }

    @Override
    public void flushCache() {
        getCachedRef().flushEntityCache(TABLE);
    }

    public Collection<Integer> selectAllIdsForTable() {
        return selectAllIdsForTable(TABLE, PRIMARY_COLUMN);
    }

    public CachedRef getCachedRef() {
        return cachedRef;
    }

    public String getCreateTableIfNotExistSql() {
        return createTableIfNotExistSql;
    }
}
