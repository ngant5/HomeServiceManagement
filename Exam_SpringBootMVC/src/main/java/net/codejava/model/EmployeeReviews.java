package net.codejava.model;

import java.time.LocalDateTime;

public class EmployeeReviews {
    private int reviewId;
    private int contractDetailId;
    private int employeeId;  // Thêm trường employeeId
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    // Constructor mặc định
    public EmployeeReviews() {
    }

    // Constructor có tham số (đã có thêm employeeId)
    public EmployeeReviews(int reviewId, int contractDetailId, int employeeId, int rating, String comment, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.contractDetailId = contractDetailId;
        this.employeeId = employeeId;  // Gán giá trị cho employeeId
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

    public int getEmployeeId() {  // Thêm getter cho employeeId
        return employeeId; 
    }

    public void setEmployeeId(int employeeId) {  // Thêm setter cho employeeId
        this.employeeId = employeeId; 
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
