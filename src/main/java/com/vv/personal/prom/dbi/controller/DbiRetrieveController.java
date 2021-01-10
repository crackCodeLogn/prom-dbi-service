package com.vv.personal.prom.dbi.controller;

import com.vv.personal.prom.artifactory.proto.*;
import com.vv.personal.prom.dbi.interactor.ref.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vivek
 * @since 05/01/21
 */
@RestController("DbiRetrieveController")
@RequestMapping("/prom/dbi/retrieve")
public class DbiRetrieveController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbiRetrieveController.class);

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

    @GetMapping("/customer/all")
    public CustomerList retrieveAllCustomers(Auth auth) {
        if (!authController.isAuthorized() && !authController.checkCred(auth.getCred())) return null;
        CustomerList customerList = refTableCustomer.retrieveAll();
        LOGGER.info("Retrieved following list of customers from DB:-\n{}", customerList);
        return customerList;
    }

    @GetMapping("/company/all")
    public CompanyList retrieveAllCompanies(Auth auth) {
        if (!authController.isAuthorized() && !authController.checkCred(auth.getCred())) return null;
        CompanyList companyList = refTableCompany.retrieveAll();
        LOGGER.info("Retrieved following list of companies from DB:-\n{}", companyList);
        return companyList;
    }

    @GetMapping("/problem/all")
    public ProblemList retrieveAllProblems(Auth auth) {
        if (!authController.isAuthorized() && !authController.checkCred(auth.getCred())) return null;
        ProblemList problemList = refTableProblem.retrieveAll();
        LOGGER.info("Retrieved following list of problems from DB:-\n{}", problemList);
        return problemList;
    }

    @GetMapping("/make/all")
    public MakeList retrieveAllMakes(Auth auth) {
        if (!authController.isAuthorized() && !authController.checkCred(auth.getCred())) return null;
        MakeList makeList = refTableMake.retrieveAll();
        LOGGER.info("Retrieved following list of makes from DB:-\n{}", makeList);
        return makeList;
    }

    @GetMapping("/component/all")
    public ComponentList retrieveAllComponents(Auth auth) {
        if (!authController.isAuthorized() && !authController.checkCred(auth.getCred())) return null;
        ComponentList componentList = refTableComponent.retrieveAll();
        LOGGER.info("Retrieved following list of components from DB:-\n{}", componentList);
        return componentList;
    }
}
