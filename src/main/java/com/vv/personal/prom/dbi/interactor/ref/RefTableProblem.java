package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Problem;
import com.vv.personal.prom.artifactory.proto.ProblemList;
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
public class RefTableProblem extends RefDbi<Problem, ProblemList> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableProblem.class);

    public RefTableProblem(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef, LOGGER);
    }

    @Override
    public int pushNewEntity(Problem problem) {
        LOGGER.info("Pushing new problem '{}' into the DB", problem);
        if (cachedRef.isIdPresentInEntityCache(TABLE, problem.getProblemId())) {
            LOGGER.info("Problem '{}' already present", problem.getProblemName());
            return 0;
        }
        return insertNewIntegerAndString(TABLE, problem.getProblemId(), problem.getProblemName());
    }

    @Override
    public int deleteEntity(Integer idToDel) {
        return 0;
    }

    @Override
    public ProblemList retrieveAll() {
        String sql = String.format(SELECT_ALL, TABLE);
        ResultSet resultSet = executeNonUpdateSql(sql);
        int rowsReturned = 0;
        ProblemList.Builder problemLister = ProblemList.newBuilder();
        try {
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    Problem problem = generateDetail(resultSet);
                    problemLister.addProblem(problem);
                    rowsReturned++;
                } catch (SQLException throwables) {
                    LOGGER.error("Failed to completely extract result from the above select all query. ", throwables);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to execute / process sql '{}'. ", sql, e);
        }
        LOGGER.info("Received {} entries for sql => '{}'", rowsReturned, sql);
        return problemLister.build();
    }

    @Override
    public ProblemList retrieveSelective() {
        return null;
    }

    @Override
    public Problem generateDetail(ResultSet resultSet) {
        Problem.Builder builder = Problem.newBuilder();
        try {
            builder.setProblemId(resultSet.getInt("id_prob"));
            builder.setProblemName(resultSet.getString("name_prob"));
        } catch (SQLException throwables) {
            LOGGER.error("Failed to retrieve problem detail from DB. ", throwables);
        }
        return builder.build();
    }

}
