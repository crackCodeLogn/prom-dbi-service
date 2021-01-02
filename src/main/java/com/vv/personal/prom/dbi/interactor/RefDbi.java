package com.vv.personal.prom.dbi.interactor;

import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.vv.personal.prom.dbi.constants.Constants.INSERT_STMT_INT_STR;
import static com.vv.personal.prom.dbi.constants.Constants.SELECT_ALL_IDS;

/**
 * @author Vivek
 * @since 01/01/21
 */
public abstract class RefDbi implements IRefDbi {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefDbi.class);
    protected final String TABLE;
    protected final String PRIMARY_COLUMN;
    protected final CachedRef cachedRef;
    private final DbiConfigForRef dbiConfigForRef;

    public RefDbi(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        this.TABLE = table;
        this.PRIMARY_COLUMN = primaryColumn;
        this.dbiConfigForRef = dbiConfigForRef;
        this.cachedRef = cachedRef;
        LOGGER.info("Created handler for '{}'", TABLE);
    }

    @Override
    public ResultSet executeNonUpdateSql(String sql) {
        LOGGER.info("Executing SQL => {}", sql);
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
    }

    @Override
    public int executeUpdateSql(String sql) {
        LOGGER.info("Executing SQL => {}", sql);
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
    }

    @Override
    public int insertNewIntegerAndString(Integer id, String detail) {
        String sql = String.format(INSERT_STMT_INT_STR, TABLE, id, detail);
        return executeUpdateSql(sql);
    }

    @Override
    public Collection<Integer> selectAllIdsForTable(String table, String column) {
        String sql = String.format(SELECT_ALL_IDS, column, table);
        ResultSet resultSet = executeNonUpdateSql(sql);
        LOGGER.info("Received result of select All for '{}' of table '{}'", column, table);
        List<Integer> ids = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                ids.add(resultSet.getInt(1));
            } catch (SQLException throwables) {
                LOGGER.error("Failed to completely extract result from the above select all query. ", throwables);
            }
        }
        return ids;
    }

    public CachedRef getCachedRef() {
        return cachedRef;
    }
}
