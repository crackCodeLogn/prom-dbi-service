package com.vv.personal.prom.dbi.controller;

import com.vv.personal.prom.dbi.interactor.ref.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.vv.personal.prom.dbi.constants.Constants.*;

/**
 * @author Vivek
 * @since 03/01/21
 */
@RestController("DbiCacheController")
@RequestMapping("/prom/dbi/cache")
public class DbiCacheController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbiCacheController.class);

    @Autowired
    @Qualifier("RefTableCustomer")
    private RefTableCustomer refTableCustomer;

    @Autowired
    @Qualifier("RefTableCompany")
    private RefTableCompany refTableCompany;

    @Autowired
    @Qualifier("RefTableProblem")
    private RefTableProblem refTableProblem;

    @Autowired
    @Qualifier("RefTableMake")
    private RefTableMake refTableMake;

    @Autowired
    @Qualifier("RefTableComponent")
    private RefTableComponent refTableComponent;

    @Autowired
    private AuthController authController;

    @GetMapping("/clear/ref/all")
    public void clearAllRefCache() {
        if (!authController.isAuthorized()) return;
        refTableCustomer.flushCache();
        refTableCompany.flushCache();
        refTableComponent.flushCache();
        refTableMake.flushCache();
        refTableProblem.flushCache();
    }

    @GetMapping("/clear/ref/{}")
    public void clearSingleRefTableCache(String refTableCacheToClear) {
        if (!authController.isAuthorized()) return;
        switch (refTableCacheToClear) {
            case TABLE_REF_CUSTOMER:
                refTableCustomer.flushCache();
                break;
            case TABLE_REF_COMPANY:
                refTableCompany.flushCache();
                break;
            case TABLE_REF_COMPONENT:
                refTableComponent.flushCache();
                break;
            case TABLE_REF_MAKE:
                refTableMake.flushCache();
                break;
            case TABLE_REF_PROBLEM:
                refTableProblem.flushCache();
                break;
        }
    }

    @GetMapping("/populate/ref/all")
    public void populateAllRefCache() {
        if (!authController.isAuthorized()) return;
        refTableCustomer.populatePrimaryIds();
        refTableCompany.populatePrimaryIds();
        refTableComponent.populatePrimaryIds();
        refTableMake.populatePrimaryIds();
        refTableProblem.populatePrimaryIds();
    }

    @GetMapping("/populate/ref/{}")
    public void populateSingleRefTableCache(String refTableCacheToPopulate) {
        if (!authController.isAuthorized()) return;
        switch (refTableCacheToPopulate) {
            case TABLE_REF_CUSTOMER:
                refTableCustomer.populatePrimaryIds();
                break;
            case TABLE_REF_COMPANY:
                refTableCompany.populatePrimaryIds();
                break;
            case TABLE_REF_COMPONENT:
                refTableComponent.populatePrimaryIds();
                break;
            case TABLE_REF_MAKE:
                refTableMake.populatePrimaryIds();
                break;
            case TABLE_REF_PROBLEM:
                refTableProblem.populatePrimaryIds();
                break;
        }
    }

    @GetMapping("/display/ref/all")
    public String displayAllRefTableCache() {
        if (!authController.isAuthorized()) return null;
        return refTableCustomer.getCachedRef().getActiveRefEntityIds().toString();
    }
}
