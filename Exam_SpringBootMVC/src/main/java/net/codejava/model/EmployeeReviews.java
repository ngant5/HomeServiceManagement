package net.codejava.model;

import java.time.LocalDateTime;

public class EmployeeReviews {
    private int reviewId;
    private int contractDetailId; 
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    // Constructor mặc định
    public EmployeeReviews() {
    }

    // Constructor có tham số (đã có sẵn trong mã của bạn)
    public EmployeeReviews(int reviewId, int contractDetailId, int rating, String comment, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.contractDetailId = contractDetailId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Các phương thức getter và setter cho các trường dữ liệu
    public int getReviewId() { 
        return reviewId;
    }

    public void setReviewId(int reviewId) { 
        this.reviewId = reviewId; 
    }

    public int getContractDetailId() {
        return contractDetailId; 
    }

    public void setContractDetailId(int contractDetailId) {
        this.contractDetailId = contractDetailId;
    }

    public int getRating() { 
        return rating;
    }

    public void setRating(int rating) { 
        this.rating = rating; 
    }

    public String getComment() {
        return comment; 
    }

    public void setComment(String comment) { 
        this.comment = comment; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt; 
    }
}
