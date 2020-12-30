package com.vv.personal.prom.dbi.controller;

import com.vv.personal.prom.artifactory.proto.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping("/customer")
    public Boolean addNewCustomer(@RequestBody Customer newCustomer) {
        LOGGER.info("Received new customer to add to DB: {}", newCustomer);
        return Boolean.FALSE;
    }
}
