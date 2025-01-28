package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.ContractDetails;
import net.codejava.utils.Views;

public class ContractDetailsMapper implements RowMapper<ContractDetails> {
    @Override
    public ContractDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        ContractDetails contractDetail = new ContractDetails();
        contractDetail.setContractDetailId(rs.getInt(Views.COL_CONTRACT_DETAILS_ID));
        contractDetail.setContractId(rs.getInt(Views.COL_CONTRACT_DETAILS_CONTRACT_ID));
        contractDetail.setEmpServiceId(rs.getInt(Views.COL_CONTRACT_DETAILS_EMP_SERVICE_ID));
        contractDetail.setServiceAddress(rs.getString(Views.COL_CONTRACT_DETAILS_SERVICE_ADDRESS));
        contractDetail.setServicePhone(rs.getString(Views.COL_CONTRACT_DETAILS_SERVICE_PHONE));
        contractDetail.setStartDate(rs.getTimestamp(Views.COL_CONTRACT_DETAILS_START_DATE).toLocalDateTime().toLocalDate());
        contractDetail.setEndDate(rs.getTimestamp(Views.COL_CONTRACT_DETAILS_END_DATE).toLocalDateTime().toLocalDate());
        contractDetail.setStatus(rs.getInt(Views.COL_CONTRACT_DETAILS_STATUS));
        contractDetail.setHoursWorked(rs.getString(Views.COL_CONTRACT_DETAILS_HOURS_WORKED));
        contractDetail.setTotalPrice(rs.getString(Views.COL_CONTRACT_DETAILS_TOTAL_PRICE));
        return contractDetail;
    }
}
