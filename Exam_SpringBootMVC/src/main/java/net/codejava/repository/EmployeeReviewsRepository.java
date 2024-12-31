package net.codejava.repository;

import net.codejava.model.EmployeeReviews;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EmployeeReviewsRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeReviewsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Tạo mới đánh giá
    public EmployeeReviews createReview(int contractDetailId, int employeeId, int rating, String comment) {
        String sql = "INSERT INTO employee_reviews (contract_detail_id, employee_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?)";

        // Sử dụng JdbcTemplate để thực hiện câu lệnh INSERT
        jdbcTemplate.update(sql, contractDetailId, employeeId, rating, comment, Timestamp.valueOf(LocalDateTime.now()));

        // Lấy review_id sau khi INSERT thành công
        String getLastInsertedIdSql = "SELECT SCOPE_IDENTITY()";
        Integer reviewId = jdbcTemplate.queryForObject(getLastInsertedIdSql, Integer.class);

        if (reviewId != null) {
            return new EmployeeReviews(reviewId, contractDetailId, employeeId, rating, comment, LocalDateTime.now());
        }
        return null;
    }

    // Cập nhật đánh giá
    public EmployeeReviews updateReview(int reviewId, int employeeId, int rating, String comment) {
        String sql = "UPDATE employee_reviews SET rating = ?, comment = ?, employee_id = ? WHERE review_id = ?";

        // Cập nhật đánh giá
        int rowsUpdated = jdbcTemplate.update(sql, rating, comment, employeeId, reviewId);
        if (rowsUpdated > 0) {
            return getReviewById(reviewId); // Trả về đánh giá đã được cập nhật
        }
        return null;
    }

    // Lấy đánh giá theo reviewId
    public EmployeeReviews getReviewById(int reviewId) {
        String sql = "SELECT * FROM employee_reviews WHERE review_id = ?";

        // Sử dụng JdbcTemplate để lấy đánh giá từ database
        return jdbcTemplate.queryForObject(sql, new Object[]{reviewId}, this::mapRowToEmployeeReviews);
    }

    // Lấy đánh giá theo contractDetailId
    public EmployeeReviews getReviewByContractDetailId(int contractDetailId) {
        String sql = "SELECT * FROM employee_reviews WHERE contract_detail_id = ?";

        // Sử dụng JdbcTemplate để lấy đánh giá từ database
        return jdbcTemplate.queryForObject(sql, new Object[]{contractDetailId}, this::mapRowToEmployeeReviews);
    }

    // Lấy tất cả các đánh giá theo contractId
    public List<EmployeeReviews> findByContractId(int contractId) {
        String sql = "SELECT * FROM employee_reviews WHERE contract_detail_id IN (SELECT contract_detail_id FROM contract_details WHERE contract_id = ?)";

        // Sử dụng JdbcTemplate để lấy tất cả đánh giá từ database theo contractId
        return jdbcTemplate.query(sql, new Object[]{contractId}, this::mapRowToEmployeeReviews);
    }

    // Chuyển đổi ResultSet thành đối tượng EmployeeReviews
    private EmployeeReviews mapRowToEmployeeReviews(ResultSet rs, int rowNum) throws SQLException {
        int reviewId = rs.getInt("review_id");
        int contractDetailId = rs.getInt("contract_detail_id");
        int employeeId = rs.getInt("employee_id");  // Đọc thông tin employee_id
        int rating = rs.getInt("rating");
        String comment = rs.getString("comment");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new EmployeeReviews(reviewId, contractDetailId, employeeId, rating, comment, createdAt);  // Trả về đối tượng EmployeeReviews với đầy đủ thông tin
    }
}
