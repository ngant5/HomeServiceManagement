package net.codejava.model;

import java.sql.Date;


public class EmployeeReviews {
    private int reviewId;
    private int contractDetailId;
    private int employeeId;
    private String fullname; 
    private int rating;
    private String comment;
    private Date createdAt;

    // Constructor mặc định
    public EmployeeReviews() {
    }

    // Constructor với fullname (khi không có rating)
    public EmployeeReviews(int reviewId, int contractDetailId, int employeeId, String fullname, String comment, Date createdAt) {
        this.reviewId = reviewId;
        this.contractDetailId = contractDetailId;
        this.employeeId = employeeId;
        this.fullname = fullname; // Gán giá trị cho fullname
        this.comment = comment;
        this.createdAt = createdAt;
        this.rating = 0; // Không có rating, gán mặc định là 0
    }

    // Constructor có tham số (đã có thêm employeeId)
    public EmployeeReviews(int reviewId, int contractDetailId, int employeeId, int rating, String comment, Date createdAt) {
        this.reviewId = reviewId;
        this.contractDetailId = contractDetailId;
        this.employeeId = employeeId;
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

    public String getFullname() {  // Thêm getter cho fullname
        return fullname;
    }

    public void setFullname(String fullname) {  // Thêm setter cho fullname
        this.fullname = fullname;
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

    public Date getCreatedAt() { 
        return createdAt; 
    }

    public void setCreatedAt(Date createdAt) { 
        this.createdAt = createdAt; 
    }
}
