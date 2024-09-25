package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.EmployeeContractSchedule;
import net.codejava.utils.Views;

public class EmployeeContractScheduleMapper implements RowMapper<EmployeeContractSchedule> {
    @Override
    public EmployeeContractSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeContractSchedule schedule = new EmployeeContractSchedule();
        schedule.setScheduleId(rs.getInt(Views.COL_EMPLOYEE_CONTRACT_SCHEDULE_ID));
        schedule.setContractDetailId(rs.getInt(Views.COL_EMPLOYEE_CONTRACT_SCHEDULE_CONTRACT_DETAIL_ID));
        schedule.setWorkDate(rs.getInt(Views.COL_EMPLOYEE_CONTRACT_SCHEDULE_WORK_DATE));
        schedule.setStartTime(rs.getTimestamp(Views.COL_EMPLOYEE_CONTRACT_SCHEDULE_START_TIME).toLocalDateTime());
        schedule.setEndTime(rs.getTimestamp(Views.COL_EMPLOYEE_CONTRACT_SCHEDULE_END_TIME).toLocalDateTime());
        schedule.setHoursWorked(rs.getTimestamp(Views.COL_EMPLOYEE_CONTRACT_SCHEDULE_HOURS_WORKED).toLocalDateTime());
        schedule.setStatus(rs.getInt(Views.COL_EMPLOYEE_CONTRACT_SCHEDULE_STATUS));
        return schedule;
    }
}



