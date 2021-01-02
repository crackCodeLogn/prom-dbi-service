package com.vv.personal.prom.dbi.controller;

import com.vv.personal.prom.artifactory.proto.Customer;
import com.vv.personal.prom.dbi.interactor.ref.RefTableCompany;
import com.vv.personal.prom.dbi.interactor.ref.RefTableCustomer;
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

    @PostMapping("/customer")
    public int addNewCustomer(@RequestBody Customer newCustomer) {
        refTableCompany.pushNewEntity(newCustomer.getCompany());
        return refTableCustomer.pushNewEntity(newCustomer);
    }
}
