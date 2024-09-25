package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.Employees;
import net.codejava.utils.Views;

public class EmployeesMapper implements RowMapper<Employees> {
    @Override
    public Employees mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employees employee = new Employees();
        employee.setEmployeeId(rs.getInt(Views.COL_EMPLOYEES_ID));
        employee.setFullname(rs.getString(Views.COL_EMPLOYEES_FULLNAME));
        employee.setPassword(rs.getString(Views.COL_EMPLOYEES_PASSWORD));
        employee.setUserType(rs.getString(Views.COL_EMPLOYEES_USER_TYPE));
        employee.setEmail(rs.getString(Views.COL_EMPLOYEES_EMAIL));
        employee.setPhone(rs.getString(Views.COL_EMPLOYEES_PHONE));
        employee.setAddress(rs.getString(Views.COL_EMPLOYEES_ADDRESS));
        employee.setProfileImage(rs.getString(Views.COL_EMPLOYEES_PROFILE_IMAGE));
        employee.setExperienceYears(rs.getInt(Views.COL_EMPLOYEES_EXPERIENCE_YEARS));
        employee.setSalary(rs.getInt(Views.COL_EMPLOYEES_SALARY));
        employee.setStatus(rs.getInt(Views.COL_EMPLOYEES_STATUS));
        employee.setVerifyCode(rs.getString(Views.COL_EMPLOYEES_VERIFY_CODE));
        employee.setToken(rs.getString(Views.COL_EMPLOYEES_TOKEN));
        employee.setCreatedAt(rs.getTimestamp(Views.COL_EMPLOYEES_CREATED_AT).toLocalDateTime());
        return employee;
    }
}
