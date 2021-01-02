package com.vv.personal.prom.dbi.interactor.ref;

import com.google.protobuf.GeneratedMessageV3;
import com.vv.personal.prom.artifactory.proto.Problem;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vivek
 * @since 02/01/21
 */
public class RefTableProblem extends RefDbi<Problem> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableProblem.class);

    public RefTableProblem(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef);
    }

    @Override
    public int pushNewEntity(Problem problem) {
        LOGGER.info("Pushing new problem '{}' into the DB", problem);
        if (cachedRef.isProblemIdPresent(problem.getProblemId())) {
            LOGGER.info("Problem '{}' already present", problem.getProblemName());
            return 0;
        }
        return insertNewIntegerAndString(TABLE, problem.getProblemId(), problem.getProblemName());
    }

    @Override
    public <T extends GeneratedMessageV3> int insertNewEntities(T t) throws Exception {
        throw new Exception("Not Supported");
    }

    @Override
    public void populatePrimaryIds() {
        getCachedRef().addAllProblemIds(selectAllIdsForTable());
    }
}
