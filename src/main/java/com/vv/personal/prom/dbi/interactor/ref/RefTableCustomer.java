package com.vv.personal.prom.dbi.interactor.ref;

import com.vv.personal.prom.artifactory.proto.Company;
import com.vv.personal.prom.artifactory.proto.Customer;
import com.vv.personal.prom.artifactory.proto.CustomerList;
import com.vv.personal.prom.dbi.config.DbiConfigForRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static com.vv.personal.prom.dbi.constants.Constants.SELECT_ALL;
import static com.vv.personal.prom.dbi.constants.Constants.TABLE_REF_CUSTOMER;
import static com.vv.personal.prom.dbi.util.DbiUtil.convertToContactList;
import static com.vv.personal.prom.dbi.util.DbiUtil.extractContactNumbers;

/**
 * @author Vivek
 * @since 01/01/21
 */
public class RefTableCustomer extends RefDbi<Customer, CustomerList> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefTableCustomer.class);
    private final String INSERT_STMT_NEW_CUSTOMER = "INSERT INTO %s(id_cust, id_comp, name_first, name_last, contact_cust)" +
            " VALUES(%d, %d, '%s', '%s', '%s')";

    public RefTableCustomer(String table, String primaryColumn, DbiConfigForRef dbiConfigForRef, CachedRef cachedRef) {
        super(table, primaryColumn, dbiConfigForRef, cachedRef, LOGGER);
    }

    @Override
    public int pushNewEntity(Customer customer) {
        LOGGER.info("Pushing new customer '{}' into the DB", customer);
        if (cachedRef.isIdPresentInEntityCache(TABLE, customer.getCustomerId())) {
            LOGGER.info("Customer '{}' already present", customer.getFirstName());
            return 0;
        }

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

    @Override
    public int deleteEntity(Integer idToDel) {
        return 0;
    }

    @Override
    public CustomerList retrieveAll() {
        String sql = String.format(SELECT_ALL, TABLE);
        ResultSet resultSet = executeNonUpdateSql(sql);
        int rowsReturned = 0;
        CustomerList.Builder customerLister = CustomerList.newBuilder();
        try {
            while (true) {
                try {
                    if (!resultSet.next()) break;
                    Customer customer = generateDetail(resultSet);
                    customerLister.addCustomer(customer);
                    rowsReturned++;
                } catch (SQLException throwables) {
                    LOGGER.error("Failed to completely extract result from the above select all query. ", throwables);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to execute / process sql '{}'. ", sql, e);
        }
        LOGGER.info("Received {} entries for sql => '{}'", rowsReturned, sql);
        return customerLister.build();
    }

    @Override
    public CustomerList retrieveSelective() {
        return null;
    }

    @Override
    public Customer generateDetail(ResultSet resultSet) {
        Customer.Builder builder = Customer.newBuilder();
        try {
            builder.setCustomerId(resultSet.getInt("id_cust"));
            builder.setCompany(generateCompanyBodyForCustomer(resultSet.getInt("id_comp")));
            builder.setFirstName(resultSet.getString("name_first"));
            builder.setLastName(resultSet.getString("name_last"));
            Collection<String> customerContactNumbers = convertToContactList(resultSet.getString("contact_cust"));
            builder.addAllContactNumbers(customerContactNumbers);
        } catch (SQLException throwables) {
            LOGGER.error("Failed to retrieve company detail from DB. ", throwables);
        }
        return builder.build();
    }

    /**
     * @param customerId - to fill the object
     * @return A sample Company obj with only id populated. Later on to be merged with comp ref on client side - android
     */
    private Company generateCompanyBodyForCustomer(int customerId) {
        return Company.newBuilder()
                .setCompanyId(customerId)
                .build();
    }

}
