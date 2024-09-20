package net.codejava.model;

import java.util.Date;

public class EmployeeReview {
    private int reviewId;
    private int contractDetailId; 
    private int rating;
    private String comment;
    private Date createdAt;

    
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
    public Date getCreatedAt() { 
    	return createdAt;
    	}
    public void setCreatedAt(Date createdAt) {
    	this.createdAt = createdAt; 
    	}
}

