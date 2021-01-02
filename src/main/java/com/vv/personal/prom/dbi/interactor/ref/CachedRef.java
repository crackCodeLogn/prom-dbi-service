package com.vv.personal.prom.dbi.interactor.ref;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.vv.personal.prom.dbi.constants.Constants.*;

/**
 * @author Vivek
 * @since 02/01/21
 */
public class CachedRef {
    public final ConcurrentHashMap<String, Set<Integer>> activeRefEntityIds = new ConcurrentHashMap<>();

    public CachedRef() {
        activeRefEntityIds.put(TABLE_REF_CUSTOMER, generateEmptySet());
        activeRefEntityIds.put(TABLE_REF_COMPANY, generateEmptySet());
        activeRefEntityIds.put(TABLE_REF_PROBLEM, generateEmptySet());
        activeRefEntityIds.put(TABLE_REF_MAKE, generateEmptySet());
        activeRefEntityIds.put(TABLE_REF_COMPONENT, generateEmptySet());
    }

    public synchronized Boolean addNewIdToEntityCache(String entity, Integer idToAdd) {
        return activeRefEntityIds.get(entity).add(idToAdd);
    }

    public synchronized Boolean isIdPresentInEntityCache(String entity, Integer idToQuery) {
        return activeRefEntityIds.get(entity).contains(idToQuery);
    }

    public synchronized void bulkAddNewIdsToEntityCache(String entity, Collection<Integer> newIdsToAdd) {
        newIdsToAdd.forEach(newId -> addNewIdToEntityCache(entity, newId));
    }

    public synchronized Boolean deleteIdFromEntityCache(String entity, Integer idToDel) {
        return activeRefEntityIds.get(entity).remove(idToDel);
    }

    public synchronized void flushEntityCache(String entity) {
        activeRefEntityIds.get(entity).clear();
    }

    public ConcurrentHashMap<String, Set<Integer>> getActiveRefEntityIds() {
        return activeRefEntityIds;
    }

    private Set<Integer> generateEmptySet() {
        return new HashSet<>(0);
    }
}
