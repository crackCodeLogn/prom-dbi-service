package com.vv.personal.prom.dbi.interactor;

import com.google.protobuf.GeneratedMessageV3;
import com.vv.personal.prom.artifactory.proto.Company;
import com.vv.personal.prom.artifactory.proto.Customer;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vv.personal.prom.dbi.constants.Constants.TABLE_REF_CUSTOMER;
import static com.vv.personal.prom.dbi.util.DbiUtil.extractContactNumbers;

/**
 * @author Vivek
 * @since 01/01/21
 */
public class RefTableCustomer extends RefDbi {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableCustomer.class);
    private final String INSERT_STMT_NEW_CUSTOMER = "INSERT INTO %s(id_cust, id_comp, name_first, name_last, contact_cust)" +
            " VALUES(%d, %d, '%s', '%s', '%s')";

    public RefTableCustomer(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef);
    }

    public int pushNewCustomer(Customer newCustomer) {
        LOGGER.info("Pushing new customer '{}' into the DB", newCustomer);
        if (cachedRef.isCustomerIdPresent(newCustomer.getCustomerId())) {
            LOGGER.info("Customer '{}' already present", newCustomer.getFirstName());
            return 0;
        }
        return insertNewEntries(newCustomer);
    }

    @Override
    public <T extends GeneratedMessageV3> int insertNewEntries(T t) {
        if (t instanceof Company) return super.insertNewEntries(t);
        else if (t instanceof Customer) {
            Customer customer = (Customer) t;
            String contactNumbers = extractContactNumbers(customer.getContactNumbersList());
            LOGGER.info("Finalized contact numbers for customer '{}' => '{}'", customer.getFirstName(), contactNumbers);

            String sql = String.format(INSERT_STMT_NEW_CUSTOMER, TABLE_REF_CUSTOMER,
                    customer.getCustomerId(),
                    customer.getCompany().getCompanyId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    contactNumbers);
            return executeUpdateSql(sql);
        }
        return -1;
    }

    @Override
    public void populatePrimaryIds() {
        getCachedRef().addAllCustomerIds(selectAllIdsForTable(TABLE, PRIMARY_COLUMN));
    }

}
