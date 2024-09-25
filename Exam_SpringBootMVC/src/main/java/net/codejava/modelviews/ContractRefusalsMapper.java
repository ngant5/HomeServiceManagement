package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.ContractRefusals;
import net.codejava.utils.Views;

public class ContractRefusalsMapper implements RowMapper<ContractRefusals> {
    @Override
    public ContractRefusals mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContractRefusals contractRefusal = new ContractRefusals();
        contractRefusal.setRefusalId(rs.getInt(Views.COL_CONTRACT_REFUSALS_ID));
        contractRefusal.setContractDetailId(rs.getInt(Views.COL_CONTRACT_REFUSALS_CONTRACT_DETAIL_ID));
        contractRefusal.setRefusalReason(rs.getString(Views.COL_CONTRACT_REFUSALS_REASON));
        contractRefusal.setStatus(rs.getInt(Views.COL_CONTRACT_REFUSALS_STATUS));
        contractRefusal.setRefusalDate(rs.getTimestamp(Views.COL_CONTRACT_REFUSALS_DATE).toLocalDateTime());
        contractRefusal.setCreatedAt(rs.getTimestamp(Views.COL_CONTRACT_REFUSALS_CREATED_AT).toLocalDateTime());
        return contractRefusal;
    }
}
