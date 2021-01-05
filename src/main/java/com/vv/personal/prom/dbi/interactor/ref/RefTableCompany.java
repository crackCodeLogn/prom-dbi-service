package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Company;
import com.vv.personal.prom.artifactory.proto.CompanyList;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static com.vv.personal.prom.dbi.constants.Constants.SELECT_ALL;
import static com.vv.personal.prom.dbi.util.DbiUtil.convertToContactList;
import static com.vv.personal.prom.dbi.util.DbiUtil.extractContactNumbers;

/**
 * @author Vivek
 * @since 01/01/21
 */
public class RefTableCompany extends RefDbi<Company, CompanyList> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableCompany.class);
    private final String INSERT_STMT_NEW_COMPANY = "INSERT INTO %s(id_comp, name_comp, person_contact_comp, contact_comp)" +
            " VALUES(%d, '%s', '%s', '%s')";

    public RefTableCompany(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef, LOGGER);
    }

    @Override
    public int pushNewEntity(Company company) {
        LOGGER.info("Pushing company => {}", company);
        if (cachedRef.isIdPresentInEntityCache(TABLE, company.getCompanyId())) {
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
    public CompanyList retrieveAll() {
        String sql = String.format(SELECT_ALL, TABLE);
        ResultSet resultSet = executeNonUpdateSql(sql);
        int rowsReturned = 0;
        CompanyList.Builder companyLister = CompanyList.newBuilder();
        try {
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    Company.Builder companyBuilder = Company.newBuilder();
                    companyBuilder.setCompanyId(resultSet.getInt(1));
                    companyBuilder.setCompanyName(resultSet.getString(2));
                    companyBuilder.setCompanyContactPerson(resultSet.getString(3));
                    Collection<String> companyContactNumbers = convertToContactList(resultSet.getString(4));
                    companyBuilder.addAllContactNumbers(companyContactNumbers);
                    companyLister.addCompanies(companyBuilder.build());
                    rowsReturned++;
                } catch (SQLException throwables) {
                    LOGGER.error("Failed to completely extract result from the above select all query. ", throwables);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to execute / process sql '{}'. ", sql, e);
        }
        LOGGER.info("Received {} entries for sql => '{}'", rowsReturned, sql);
        return companyLister.build();
    }

    @Override
    public CompanyList retrieveSelective() {
        return CompanyList.newBuilder().build();
    }

}
