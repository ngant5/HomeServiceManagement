package net.codejava.repository;

import net.codejava.model.EmployeeContractSchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EmployeeContractScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor với JdbcTemplate
    public EmployeeContractScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] insertBatch(List<EmployeeContractSchedule> schedules) {
        String sql = "INSERT INTO Employee_Contract_Schedule (employee_id, work_date, start_time, end_time, status) " +
                     "VALUES (?, ?, ?, ?, ?)";

        // Thực hiện batch update
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return schedules.size(); // Kích thước của batch
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EmployeeContractSchedule schedule = schedules.get(i);
                // Set giá trị vào PreparedStatement
                ps.setInt(1, schedule.getEmployeeId()); // set employeeId
                ps.setDate(2, java.sql.Date.valueOf(schedule.getWorkDate())); // set workDate
                ps.setTimestamp(3, java.sql.Timestamp.valueOf(schedule.getStartTime())); // set startTime
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(schedule.getEndTime())); // set endTime
                ps.setInt(5, schedule.getStatus()); // set status
            }
        });
    }


    // RowMapper để chuyển kết quả từ ResultSet thành đối tượng EmployeeContractSchedule
    private RowMapper<EmployeeContractSchedule> employeeContractScheduleRowMapper() {
        return (rs, rowNum) -> {
            EmployeeContractSchedule schedule = new EmployeeContractSchedule();
            schedule.setScheduleId(rs.getInt("schedule_id"));
            schedule.setContractDetailId(rs.getInt("contract_detail_id"));
            schedule.setEmployeeId(rs.getInt("employee_id"));
            schedule.setWorkDate(rs.getDate("work_date").toLocalDate());  // Convert từ SQL Date sang LocalDate
            schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime()); // Convert từ Timestamp sang LocalDateTime
            schedule.setEndTime(rs.getTimestamp("end_time").toLocalDateTime()); // Convert từ Timestamp sang LocalDateTime
            schedule.setStatus(rs.getInt("status"));
            return schedule;
        };
    }

    // Tìm kiếm lịch làm việc của nhân viên theo ngày
    public List<EmployeeContractSchedule> findByEmployeeIdAndWorkDate(int employeeId, LocalDate workDate) {
        String sql = "SELECT * FROM Employee_Contract_Schedule WHERE employee_id = ? AND work_date = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId, workDate}, employeeContractScheduleRowMapper());
    }

    // Tìm kiếm lịch làm việc của nhân viên trong khoảng thời gian (startDate đến endDate)
    public List<EmployeeContractSchedule> findByEmployeeIdAndWorkDateBetween(int employeeId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM Employee_Contract_Schedule WHERE employee_id = ? AND work_date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId, startDate, endDate}, employeeContractScheduleRowMapper());
    }
}
