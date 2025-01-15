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
import java.util.Optional;

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

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return schedules.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                EmployeeContractSchedule schedule = schedules.get(i);
                ps.setInt(1, schedule.getEmployeeId());
                ps.setDate(2, java.sql.Date.valueOf(schedule.getWorkDate()));
                ps.setTimestamp(3, java.sql.Timestamp.valueOf(schedule.getStartTime()));
                ps.setTimestamp(4, java.sql.Timestamp.valueOf(schedule.getEndTime()));
                ps.setInt(5, schedule.getStatus());
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
            schedule.setWorkDate(rs.getDate("work_date").toLocalDate());
            schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
            schedule.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
            schedule.setStatus(rs.getInt("status"));
            return schedule;
        };
    }

    // Tìm kiếm lịch làm việc của nhân viên theo ngày
    public List<EmployeeContractSchedule> findByEmployeeIdAndWorkDate(int employeeId, LocalDate workDate) {
    	String sql = "SELECT * FROM Employee_Contract_Schedule WHERE employee_id = ? AND work_date = ? AND status = 1";
        return jdbcTemplate.query(sql, new Object[]{employeeId, workDate}, employeeContractScheduleRowMapper());
    }

    // Phương thức tìm tất cả các employee_id duy nhất từ bảng Employee_Contract_Schedule
    public List<Integer> findAllEmployeeIds() {
        String sql = "SELECT DISTINCT employee_id FROM Employee_Contract_Schedule";
        return jdbcTemplate.queryForList(sql, Integer.class);
    }

    // Phương thức tìm EmployeeContractSchedule theo scheduleId
    public Optional<EmployeeContractSchedule> findById(int scheduleId) {
        String sql = "SELECT * FROM Employee_Contract_Schedule WHERE schedule_id = ?";
        List<EmployeeContractSchedule> result = jdbcTemplate.query(sql, new Object[]{scheduleId}, employeeContractScheduleRowMapper());

        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    // Phương thức lưu hoặc cập nhật EmployeeContractSchedule
    public EmployeeContractSchedule save(EmployeeContractSchedule schedule) {
        String sql = "INSERT INTO Employee_Contract_Schedule (employee_id, work_date, start_time, end_time, status) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE employee_id = VALUES(employee_id), work_date = VALUES(work_date), " +
                     "start_time = VALUES(start_time), end_time = VALUES(end_time), status = VALUES(status)";

        jdbcTemplate.update(sql, schedule.getEmployeeId(), java.sql.Date.valueOf(schedule.getWorkDate()), 
                            java.sql.Timestamp.valueOf(schedule.getStartTime()), java.sql.Timestamp.valueOf(schedule.getEndTime()), 
                            schedule.getStatus());

        return schedule;
    }

    public List<EmployeeContractSchedule> findByEmployeeIdAndStatus(int employeeId, int status) {
        String sql = "SELECT * FROM Employee_Contract_Schedule WHERE employee_id = ? AND status = 0";
        return jdbcTemplate.query(sql, new Object[]{employeeId, status}, employeeContractScheduleRowMapper());
    }
}
