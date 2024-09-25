package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.SalaryProposals;
import net.codejava.utils.Views;

public class SalaryProposalsMapper implements RowMapper<SalaryProposals> {
    @Override
    public SalaryProposals mapRow(ResultSet rs, int rowNum) throws SQLException {
        SalaryProposals proposal = new SalaryProposals();
        proposal.setProposalId(rs.getInt(Views.COL_SALARY_PROPOSALS_ID));
        proposal.setEmployeeId(rs.getInt(Views.COL_SALARY_PROPOSALS_EMPLOYEE_ID));
        proposal.setProposedSalary(rs.getInt(Views.COL_SALARY_PROPOSALS_PROPOSED_SALARY));
        proposal.setFromDate(rs.getInt(Views.COL_SALARY_PROPOSALS_FROM_DATE));
        proposal.setToDate(rs.getInt(Views.COL_SALARY_PROPOSALS_TO_DATE));
        proposal.setApprovalStatus(rs.getInt(Views.COL_SALARY_PROPOSALS_APPROVAL_STATUS));
        proposal.setCreatedAt(rs.getTimestamp(Views.COL_SALARY_PROPOSALS_CREATED_AT).toLocalDateTime());
        return proposal;
    }
}
