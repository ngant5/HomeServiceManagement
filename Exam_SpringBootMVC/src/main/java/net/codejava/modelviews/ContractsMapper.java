package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.Contracts;
import net.codejava.utils.Views;

public class ContractsMapper implements RowMapper<Contracts> {
    @Override
    public Contracts mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contracts contract = new Contracts();
        contract.setContractId(rs.getInt(Views.COL_CONTRACTS_ID));
        contract.setCustomerId(rs.getInt(Views.COL_CONTRACTS_CUSTOMER_ID));
        contract.setContractStatus(rs.getInt(Views.COL_CONTRACTS_STATUS));
        contract.setTotalPrice(rs.getInt(Views.COL_CONTRACTS_TOTAL_PRICE));
        contract.setPaymentStatus(rs.getInt(Views.COL_CONTRACTS_PAYMENT_STATUS));
        contract.setCreatedAt(rs.getTimestamp(Views.COL_CONTRACTS_CREATED_AT).toLocalDateTime());
        contract.setContractFile(rs.getString(Views.COL_CONTRACTS_FILE));
        return contract;
    }
}
