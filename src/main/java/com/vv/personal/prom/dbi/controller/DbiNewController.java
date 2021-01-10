package com.vv.personal.prom.dbi.controller;

import com.vv.personal.prom.artifactory.proto.*;
import com.vv.personal.prom.dbi.interactor.ref.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vivek
 * @since 30/12/20
 */
@RestController("DbiController")
@RequestMapping("/prom/dbi/new")
public class DbiNewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbiNewController.class);

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

    @PostMapping("/customer")
    public int addNewCustomer(@RequestBody Customer newCustomer) {
        if (!authController.isAuthorized() && !authController.checkCred(newCustomer.getAuth().getCred())) return -1;
        addNewCompany(newCustomer.getCompany());
        return refTableCustomer.pushNewEntity(newCustomer);
    }

    @PostMapping("/company")
    public int addNewCompany(@RequestBody Company newcustomer) {
        if (!authController.isAuthorized() && !authController.checkCred(newcustomer.getAuth().getCred())) return -1;
        return refTableCompany.pushNewEntity(newcustomer);
    }

    @PostMapping("/make")
    public int addNewMake(@RequestBody Make newMake) {
        if (!authController.isAuthorized() && !authController.checkCred(newMake.getAuth().getCred())) return -1;
        return refTableMake.pushNewEntity(newMake);
    }

    @PostMapping("/problem")
    public int addNewProblem(@RequestBody Problem newProblem) {
        if (!authController.isAuthorized() && !authController.checkCred(newProblem.getAuth().getCred())) return -1;
        return refTableProblem.pushNewEntity(newProblem);
    }

    @PostMapping("/component")
    public int addNewComponent(@RequestBody Component newComponent) {
        if (!authController.isAuthorized() && !authController.checkCred(newComponent.getAuth().getCred())) return -1;
        return refTableComponent.pushNewEntity(newComponent);
    }

}
