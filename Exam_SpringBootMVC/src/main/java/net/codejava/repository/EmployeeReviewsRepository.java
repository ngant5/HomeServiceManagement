package net.codejava.repository;

import net.codejava.model.EmployeeReviews;

import java.sql.*;
import java.time.LocalDateTime;

public class EmployeeReviewsRepository {

    private Connection connection;

    public EmployeeReviewsRepository(Connection connection) {
        this.connection = connection;
    }

    // Tạo mới đánh giá
    public EmployeeReviews createReview(int contractDetailId, int rating, String comment) {
        String sql = "INSERT INTO employee_reviews (contract_detail_id, rating, comment, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, contractDetailId);
            stmt.setInt(2, rating);
            stmt.setString(3, comment);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int reviewId = generatedKeys.getInt(1);
                    return new EmployeeReviews(reviewId, contractDetailId, rating, comment, LocalDateTime.now());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật đánh giá
    public EmployeeReviews updateReview(int reviewId, int rating, String comment) {
        String sql = "UPDATE employee_reviews SET rating = ?, comment = ? WHERE review_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rating);
            stmt.setString(2, comment);
            stmt.setInt(3, reviewId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                return getReviewById(reviewId); // Trả về đánh giá đã được cập nhật
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy đánh giá theo reviewId
    public EmployeeReviews getReviewById(int reviewId) {
        String sql = "SELECT * FROM employee_reviews WHERE review_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployeeReviews(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy đánh giá theo contractDetailId
    public EmployeeReviews getReviewByContractDetailId(int contractDetailId) {
        String sql = "SELECT * FROM employee_reviews WHERE contract_detail_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, contractDetailId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployeeReviews(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Chuyển đổi ResultSet thành đối tượng EmployeeReviews
    private EmployeeReviews mapResultSetToEmployeeReviews(ResultSet rs) throws SQLException {
        int reviewId = rs.getInt("review_id");
        int contractDetailId = rs.getInt("contract_detail_id");
        int rating = rs.getInt("rating");
        String comment = rs.getString("comment");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        return new EmployeeReviews(reviewId, contractDetailId, rating, comment, createdAt);
    }
}
