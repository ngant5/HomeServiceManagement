package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.Customers;
import net.codejava.utils.Views;

public class CustomersMapper implements RowMapper<Customers> {
    @Override
    public Customers mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customers customer = new Customers();
        customer.setCustomerId(rs.getInt(Views.COL_CUSTOMERS_ID));
        customer.setFullname(rs.getString(Views.COL_CUSTOMERS_FULLNAME));
        customer.setPassword(rs.getString(Views.COL_CUSTOMERS_PASSWORD));
        customer.setEmail(rs.getString(Views.COL_CUSTOMERS_EMAIL));
        customer.setPhone(rs.getString(Views.COL_CUSTOMERS_PHONE));
        customer.setAddress(rs.getString(Views.COL_CUSTOMERS_ADDRESS));
        customer.setProfileImage(rs.getString(Views.COL_CUSTOMERS_PROFILE_IMAGE));
        customer.setStatus(rs.getInt(Views.COL_CUSTOMERS_STATUS));
        customer.setCreatedAt(rs.getTimestamp(Views.COL_CUSTOMERS_CREATED_AT).toLocalDateTime());
        customer.setVerifyCode(rs.getString(Views.COL_CUSTOMERS_VERIFY_CODE));
        customer.setToken(rs.getString(Views.COL_CUSTOMERS_TOKEN));
        return customer;
    }
}
