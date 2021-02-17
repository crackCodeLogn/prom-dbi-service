package com.vv.personal.prom.dbi.config;

import com.vv.personal.prom.dbi.auth.Authenticator;
import com.vv.personal.prom.dbi.feign.AuthFeign;
import com.vv.personal.prom.dbi.interactor.ref.*;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.vv.personal.prom.dbi.constants.Constants.*;
import static com.vv.personal.prom.dbi.util.DbiUtil.generateCreateTableSql;

/**
 * @author Vivek
 * @since 01/01/21
 */
@Configuration
public class DbiConfig {

    @Autowired
    private AuthFeign authFeign;

    private final List<RefDbi> refDbis = new ArrayList<>();
    @Value("${dbi.tables.create.onStartup:true}")
    private boolean createTablesOnStartup;

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
        return new RefTableCustomer(TABLE_REF_CUSTOMER, PRIMARY_COL_CUSTOMER, RefDbConnector(), cachedRef(),
                generateCreateTableSql("Ref.Customer"));
    }

    @Bean
    @Qualifier("RefTableCompany")
    public RefTableCompany refTableCompany() {
        return new RefTableCompany(TABLE_REF_COMPANY, PRIMARY_COL_COMPANY, RefDbConnector(), cachedRef(),
                generateCreateTableSql("Ref.Company"));
    }

    @Bean
    @Qualifier("RefTableMake")
    public RefTableMake refTableMake() {
        return new RefTableMake(TABLE_REF_MAKE, PRIMARY_COL_MAKE, RefDbConnector(), cachedRef(),
                generateCreateTableSql("Ref.Make"));
    }

    @Bean
    @Qualifier("RefTableProblem")
    public RefTableProblem refTableProblem() {
        return new RefTableProblem(TABLE_REF_PROBLEM, PRIMARY_COL_PROBLEM, RefDbConnector(), cachedRef(),
                generateCreateTableSql("Ref.Problem"));
    }

    @Bean
    @Qualifier("RefTableComponent")
    public RefTableComponent refTableComponent() {
        return new RefTableComponent(TABLE_REF_COMPONENT, PRIMARY_COL_COMPONENT, RefDbConnector(), cachedRef(),
                generateCreateTableSql("Ref.Component"));
    }

    @Bean(initMethod = "start")
    @Scope("prototype")
    public StopWatch stopWatch() {
        return new StopWatch();
    }

    @Bean
    public Authenticator authenticator() {
        return new Authenticator(authFeign);
    }

    @PostConstruct
    public void postHaste() {
        refDbis.add(refTableCustomer());
        refDbis.add(refTableCompany());
        refDbis.add(refTableComponent());
        refDbis.add(refTableMake());
        refDbis.add(refTableProblem());
    }

    public boolean isCreateTablesOnStartup() {
        return createTablesOnStartup;
    }

    public List<RefDbi> getRefDbis() {
        return refDbis;
    }
}
