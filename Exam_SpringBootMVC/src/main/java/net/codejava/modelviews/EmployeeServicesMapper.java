package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.EmployeeServices;
import net.codejava.utils.Views;

public class EmployeeServicesMapper implements RowMapper<EmployeeServices> {
    @Override
    public EmployeeServices mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeServices empService = new EmployeeServices();
        empService.setEmpServiceId(rs.getInt(Views.COL_EMPLOYEE_SERVICES_ID));
        empService.setEmployeeId(rs.getInt(Views.COL_EMPLOYEE_SERVICES_EMPLOYEE_ID));
        empService.setServiceId(rs.getInt(Views.COL_EMPLOYEE_SERVICES_SERVICE_ID));
        empService.setDetails(rs.getString(Views.COL_EMPLOYEE_SERVICES_DETAILS));
        return empService;
    }
}
