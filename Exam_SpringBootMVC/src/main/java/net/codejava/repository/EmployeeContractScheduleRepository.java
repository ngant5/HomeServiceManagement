package net.codejava.repository;

import net.codejava.model.EmployeeContractSchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        String sql = "INSERT INTO Employee_Contract_Schedule (employee_id, work_date, start_time, end_time, status, hours_work) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

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
                LocalDateTime startDateTime = schedule.getWorkDate().atTime(schedule.getStartTime());
                LocalDateTime endDateTime = schedule.getWorkDate().atTime(schedule.getEndTime());

                ps.setTimestamp(3, Timestamp.valueOf(startDateTime));
                ps.setTimestamp(4, Timestamp.valueOf(endDateTime));

                ps.setInt(5, schedule.getStatus());
                ps.setInt(6, schedule.getHoursWork());  

            }
        });
    }
    
    public boolean existsByMonthAndYear(int month, int year) {
        String sql = "SELECT COUNT(*) FROM Employee_Contract_Schedule WHERE MONTH(work_date) = ? AND YEAR(work_date) = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, month, year);
        return count != null && count > 0;
    }

    // RowMapper để chuyển kết quả từ ResultSet thành đối tượng EmployeeContractSchedule
    private RowMapper<EmployeeContractSchedule> employeeContractScheduleRowMapper() {
        return (rs, rowNum) -> {
            EmployeeContractSchedule schedule = new EmployeeContractSchedule();
            schedule.setScheduleId(rs.getInt("schedule_id"));
            schedule.setContractDetailId(rs.getInt("contract_detail_id"));
            schedule.setEmployeeId(rs.getInt("employee_id"));
            schedule.setWorkDate(rs.getDate("work_date").toLocalDate());
            schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime().toLocalTime());
            schedule.setEndTime(rs.getTimestamp("end_time").toLocalDateTime().toLocalTime());
            schedule.setHoursWork(rs.getInt("hours_work"));
            schedule.setStatus(rs.getInt("status"));
            Timestamp expireTime = rs.getTimestamp("expire_time");
            if (expireTime != null) {
                schedule.setExpireTime(expireTime.toLocalDateTime());
            } else {
                schedule.setExpireTime(null);
            }
            return schedule;
        };
    }

    // Tìm kiếm lịch làm việc của nhân viên theo ngày
    public List<EmployeeContractSchedule> findByEmployeeIdAndWorkDate(int employeeId, LocalDate workDate) {
    	String sql = "SELECT * FROM Employee_Contract_Schedule WHERE employee_id = ? AND work_date = ? AND status = 0";
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

    public EmployeeContractSchedule save(EmployeeContractSchedule schedule) {
        String sql = "INSERT INTO Employee_Contract_Schedule (employee_id, work_date, start_time, end_time, status) " +
                     "VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE employee_id = VALUES(employee_id), work_date = VALUES(work_date), " +
                     "start_time = VALUES(start_time), end_time = VALUES(end_time), status = VALUES(status)";
        
        java.sql.Timestamp startTimestamp = java.sql.Timestamp.valueOf(schedule.getWorkDate().atTime(schedule.getStartTime()));
        java.sql.Timestamp endTimestamp = java.sql.Timestamp.valueOf(schedule.getWorkDate().atTime(schedule.getEndTime()));

        jdbcTemplate.update(sql, schedule.getEmployeeId(),
                            java.sql.Date.valueOf(schedule.getWorkDate()), startTimestamp, endTimestamp,
                            schedule.getStatus());

        return schedule;
    }

    public int updateStatus(int scheduleId, int status, LocalDateTime expireTime) {
        try {
            String sql = "UPDATE Employee_Contract_Schedule SET status = ?, expire_time = ? WHERE schedule_id = ?";
            int rowsAffected = jdbcTemplate.update(sql, status, expireTime != null ? Timestamp.valueOf(expireTime) : null, scheduleId);
            
            if (rowsAffected > 0) {
                System.out.println("Status updated successfully for scheduleId: " + scheduleId + " to status: " + status);
            } else {
                System.out.println("No record found for scheduleId: " + scheduleId + " or update failed.");
            }
            return rowsAffected;
        } catch (Exception e) {
            System.err.println("Error updating status for scheduleId: " + scheduleId + ": " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }



    public List<EmployeeContractSchedule> findByStatusAndExpireTimeIsNotNull(int status) {
        String sql = "SELECT * FROM Employee_Contract_Schedule WHERE status = ? AND expire_time IS NOT NULL";
        return jdbcTemplate.query(sql, new Object[]{status}, employeeContractScheduleRowMapper());
    }

    public List<EmployeeContractSchedule> findByEmployeeIdAndStatus(int employeeId, int status) {
        String sql = "SELECT * FROM Employee_Contract_Schedule WHERE employee_id = ? AND status = 0";
        return jdbcTemplate.query(sql, new Object[]{employeeId, status}, employeeContractScheduleRowMapper());
    }
    
    public int updateStatusRemove(int scheduleId, int status, LocalDateTime expireTime) {
        try {
            String sql = "UPDATE Employee_Contract_Schedule SET status = ?, expire_time = ? WHERE schedule_id = ?";
            int rowsAffected = jdbcTemplate.update(sql, status, expireTime != null ? Timestamp.valueOf(expireTime) : null, scheduleId);

            if (rowsAffected > 0) {
                System.out.println("Status updated successfully for scheduleId: " + scheduleId);
            } else {
                System.out.println("No record found for scheduleId: " + scheduleId + " or update failed.");
            }
            return rowsAffected;
        } catch (Exception e) {
            System.err.println("Error updating status for scheduleId: " + scheduleId + ": " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }


}
