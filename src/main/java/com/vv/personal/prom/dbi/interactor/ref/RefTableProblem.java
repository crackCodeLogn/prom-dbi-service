package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Problem;
import com.vv.personal.prom.artifactory.proto.ProblemList;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return null;
    }

    @Override
    public ProblemList retrieveSelective() {
        return null;
    }

}
