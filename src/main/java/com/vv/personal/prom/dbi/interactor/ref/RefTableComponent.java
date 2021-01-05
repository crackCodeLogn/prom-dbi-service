package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Component;
import com.vv.personal.prom.artifactory.proto.ComponentList;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vivek
 * @since 02/01/21
 */
public class RefTableComponent extends RefDbi<Component, ComponentList> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableComponent.class);

    public RefTableComponent(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef, LOGGER);
    }

    @Override
    public int pushNewEntity(Component component) { //enum based, defined in proto
        LOGGER.info("Pushing new component '{}' into the DB", component);
        if (cachedRef.isIdPresentInEntityCache(TABLE, component.getSupportedComponentsValue())) {
            LOGGER.info("Component '{}' already present", component.getSupportedComponents());
            return 0;
        }
        return insertNewIntegerAndString(TABLE, component.getSupportedComponentsValue(), component.getSupportedComponents().name());
    }

    @Override
    public int deleteEntity(Integer idToDel) {
        return 0;
    }

    @Override
    public ComponentList retrieveAll() {
        return null;
    }

    @Override
    public ComponentList retrieveSelective() {
        return null;
    }

}
