package com.vv.personal.prom.dbi.interactor.ref;

import com.google.protobuf.GeneratedMessageV3;
import com.vv.personal.prom.artifactory.proto.Make;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vivek
 * @since 02/01/21
 */
public class RefTableMake extends RefDbi<Make> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableMake.class);

    public RefTableMake(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef);
    }

    @Override
    public <T extends GeneratedMessageV3> int insertNewEntities(T t) throws Exception {
        throw new Exception("Not Supported");
    }

    @Override
    public int pushNewEntity(Make make) {
        LOGGER.info("Pushing new make '{}' into the DB", make);
        if (cachedRef.isMakeIdPresent(make.getMakeId())) {
            LOGGER.info("Make '{}' already present", make.getMakeName());
            return 0;
        }
        return insertNewIntegerAndString(TABLE, make.getMakeId(), make.getMakeName());
    }

    @Override
    public void populatePrimaryIds() {
        getCachedRef().addAllMakeIds(selectAllIdsForTable());
    }
}
