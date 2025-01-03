package net.codejava.modelviews;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import net.codejava.model.EmployeeReviews;
import net.codejava.utils.Views;

public class EmployeeReviewsMapper implements RowMapper<EmployeeReviews> {
    @Override
    public EmployeeReviews mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeReviews review = new EmployeeReviews();
        review.setReviewId(rs.getInt(Views.COL_EMPLOYEE_REVIEWS_ID));
        review.setContractDetailId(rs.getInt(Views.COL_EMPLOYEE_REVIEWS_CONTRACT_DETAIL_ID));
        review.setRating(rs.getInt(Views.COL_EMPLOYEE_REVIEWS_RATING));
        review.setComment(rs.getString(Views.COL_EMPLOYEE_REVIEWS_COMMENT));

        // Convert Timestamp to java.sql.Date
        Timestamp timestamp = rs.getTimestamp(Views.COL_EMPLOYEE_REVIEWS_CREATED_AT);
        if (timestamp != null) {
            // Convert Timestamp to java.sql.Date
            review.setCreatedAt(new java.sql.Date(timestamp.getTime()));
        }

        return review;
    }
}
