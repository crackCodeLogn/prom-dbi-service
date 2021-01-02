package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Company;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vv.personal.prom.dbi.util.DbiUtil.extractContactNumbers;

/**
 * @author Vivek
 * @since 01/01/21
 */
public class RefTableCompany extends RefDbi<Company> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableCompany.class);
    private final String INSERT_STMT_NEW_COMPANY = "INSERT INTO %s(id_comp, name_comp, person_contact_comp, contact_comp)" +
            " VALUES(%d, '%s', '%s', '%s')";

    public RefTableCompany(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef, LOGGER);
    }

    @Override
    public int pushNewEntity(Company company) {
        LOGGER.info("Pushing company => {}", company);
        if (cachedRef.isCompanyIdPresent(company.getCompanyId())) {
            LOGGER.info("Company '{}' already present", company.getCompanyName());
            return -1;
        }

        String contactNumbers = extractContactNumbers(company.getContactNumbersList());
        LOGGER.info("Finalized contact numbers for company '{}' => '{}'", company.getCompanyName(), contactNumbers);
        String sql = String.format(INSERT_STMT_NEW_COMPANY, TABLE,
                company.getCompanyId(),
                company.getCompanyName(),
                company.getCompanyContactPerson(),
                contactNumbers);
        return executeUpdateSql(sql);
    }

    @Override
    public int deleteEntity(Integer idToDel) {
        LOGGER.info("Deleting company with Id => {}", idToDel);

        return -1;
    }

    @Override
    public void populatePrimaryIds() {
        getCachedRef().addAllCompanyIds(selectAllIdsForTable());
    }

    @Override
    public void flushCache() {
        getCachedRef()
    }
}
