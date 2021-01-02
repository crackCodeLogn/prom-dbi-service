package com.vv.personal.prom.dbi.config;

import com.vv.personal.prom.dbi.interactor.CachedRef;
import com.vv.personal.prom.dbi.interactor.RefTableCompany;
import com.vv.personal.prom.dbi.interactor.RefTableCustomer;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.vv.personal.prom.dbi.constants.Constants.*;

/**
 * @author Vivek
 * @since 01/01/21
 */
@Configuration
public class SpringConfig {

    @Bean(initMethod = "getDbConnection", destroyMethod = "closeDbConnection")
    public DbiConfigForRef RefDbConnector() {
        return new DbiConfigForRef();
    }

    @Bean
    public CachedRef cachedRef() {
        return new CachedRef();
    }

    @Bean
    @Qualifier("RefTableCustomer")
    public RefTableCustomer refTableCustomer() {
        return new RefTableCustomer(TABLE_REF_CUSTOMER, PRIMARY_COL_CUSTOMER, RefDbConnector(), cachedRef());
    }

    @Bean
    @Qualifier("RefTableCompany")
    public RefTableCompany refTableCompany() {
        return new RefTableCompany(TABLE_REF_COMPANY, PRIMARY_COL_COMPANY, RefDbConnector(), cachedRef());
    }

    @Bean(initMethod = "start")
    @Scope("prototype")
    public StopWatch stopWatch() {
        return new StopWatch();
    }

}
