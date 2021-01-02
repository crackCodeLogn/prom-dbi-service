package com.vv.personal.prom.dbi.interactor;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vivek
 * @since 02/01/21
 */
public class CachedRef {

    public final ConcurrentHashMap<Integer, Boolean> activeCustomerIds = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, Boolean> activeCompanyIds = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, Boolean> activeComponentIds = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, Boolean> activeProblemIds = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Integer, Boolean> activeMakeIds = new ConcurrentHashMap<>();

    /**
     * @param customerId
     * @return putIfAbsent returns value for key already present / null if not previously
     */
    public synchronized Boolean addNewCustomerId(Integer customerId) {
        return activeCustomerIds.putIfAbsent(customerId, true);
    }

    public synchronized Boolean isCustomerIdPresent(Integer customerId) {
        return activeCustomerIds.containsKey(customerId);
    }

    public synchronized void addAllCustomerIds(Collection<Integer> customerIds) {
        customerIds.forEach(this::addNewCustomerId);
    }

    public synchronized Boolean addNewCompanyId(Integer companyId) {
        return activeCompanyIds.putIfAbsent(companyId, true);
    }

    public synchronized Boolean isCompanyIdPresent(Integer companyId) {
        return activeCompanyIds.containsKey(companyId);
    }

    public synchronized void addAllCompanyIds(Collection<Integer> companyIds) {
        companyIds.forEach(this::addNewCompanyId);
    }

    public ConcurrentHashMap<Integer, Boolean> getActiveCustomerIds() {
        return activeCustomerIds;
    }

    public ConcurrentHashMap<Integer, Boolean> getActiveCompanyIds() {
        return activeCompanyIds;
    }

    public ConcurrentHashMap<Integer, Boolean> getActiveComponentIds() {
        return activeComponentIds;
    }

    public ConcurrentHashMap<Integer, Boolean> getActiveProblemIds() {
        return activeProblemIds;
    }

    public ConcurrentHashMap<Integer, Boolean> getActiveMakeIds() {
        return activeMakeIds;
    }
}
