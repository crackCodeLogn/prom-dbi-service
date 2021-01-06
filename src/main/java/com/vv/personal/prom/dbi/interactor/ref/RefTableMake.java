package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Make;
import com.vv.personal.prom.artifactory.proto.MakeList;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vv.personal.prom.dbi.constants.Constants.SELECT_ALL;

/**
 * @author Vivek
 * @since 02/01/21
 */
public class RefTableMake extends RefDbi<Make, MakeList> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableMake.class);

    public RefTableMake(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef, LOGGER);
    }

    @Override
    public int pushNewEntity(Make make) {
        LOGGER.info("Pushing new make '{}' into the DB", make);
        if (cachedRef.isIdPresentInEntityCache(TABLE, make.getMakeId())) {
            LOGGER.info("Make '{}' already present", make.getMakeName());
            return 0;
        }
        return insertNewIntegerAndString(TABLE, make.getMakeId(), make.getMakeName());
    }

    @Override
    public int deleteEntity(Integer idToDel) {
        return 0;
    }

    @Override
    public MakeList retrieveAll() {
        String sql = String.format(SELECT_ALL, TABLE);
        ResultSet resultSet = executeNonUpdateSql(sql);
        int rowsReturned = 0;
        MakeList.Builder makeLister = MakeList.newBuilder();
        try {
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    Make make = generateDetail(resultSet);
                    makeLister.addMake(make);
                    rowsReturned++;
                } catch (SQLException throwables) {
                    LOGGER.error("Failed to completely extract result from the above select all query. ", throwables);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to execute / process sql '{}'. ", sql, e);
        }
        LOGGER.info("Received {} entries for sql => '{}'", rowsReturned, sql);
        return makeLister.build();
    }

    @Override
    public MakeList retrieveSelective() {
        return null;
    }

    @Override
    public Make generateDetail(ResultSet resultSet) {
        Make.Builder builder = Make.newBuilder();
        try {
            builder.setMakeId(resultSet.getInt(1));
            builder.setMakeName(resultSet.getString(2));
        } catch (SQLException throwables) {
            LOGGER.error("Failed to retrieve make detail from DB. ", throwables);
        }
        return builder.build();
    }

}
