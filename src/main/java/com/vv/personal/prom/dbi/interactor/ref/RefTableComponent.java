package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Component;
import com.vv.personal.prom.artifactory.proto.ComponentList;
import com.vv.personal.prom.artifactory.proto.SupportedComponents;
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
        return insertNewIntegerAndString(TABLE, component.getComponentId(), component.getSupportedComponents().name());
    }

    @Override
    public int deleteEntity(Integer idToDel) {
        return 0;
    }

    @Override
    public ComponentList retrieveAll() {
        String sql = String.format(SELECT_ALL, TABLE);
        ResultSet resultSet = executeNonUpdateSql(sql);
        int rowsReturned = 0;
        ComponentList.Builder componentLister = ComponentList.newBuilder();
        try {
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    Component component = generateDetail(resultSet);
                    componentLister.addComponent(component);
                    rowsReturned++;
                } catch (SQLException throwables) {
                    LOGGER.error("Failed to completely extract result from the above select all query. ", throwables);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to execute / process sql '{}'. ", sql, e);
        }
        LOGGER.info("Received {} entries for sql => '{}'", rowsReturned, sql);
        return componentLister.build();
    }

    @Override
    public ComponentList retrieveSelective() {
        return null;
    }

    @Override
    public Component generateDetail(ResultSet resultSet) {
        Component.Builder builder = Component.newBuilder();
        try {
            builder.setComponentId(resultSet.getInt("id_cpnt"));
            builder.setSupportedComponents(SupportedComponents.valueOf(resultSet.getString("name_cpnt")));
        } catch (SQLException throwables) {
            LOGGER.error("Failed to retrieve company detail from DB. ", throwables);
        }
        return builder.build();
    }

}
