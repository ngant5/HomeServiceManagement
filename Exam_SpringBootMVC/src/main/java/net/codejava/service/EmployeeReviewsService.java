package net.codejava.service;

import net.codejava.model.EmployeeReviews;
import net.codejava.repository.EmployeeReviewsRepository;

public class EmployeeReviewsService {

    private EmployeeReviewsRepository employeeReviewsRepository;

    public EmployeeReviewsService(EmployeeReviewsRepository employeeReviewsRepository) {
        this.employeeReviewsRepository = employeeReviewsRepository;
    }

    // Tạo hoặc cập nhật đánh giá
    public EmployeeReviews saveOrUpdateReview(int contractDetailId, int rating, String comment) {
        // Kiểm tra nếu đã có đánh giá, nếu có thì cập nhật, nếu không thì tạo mới
        EmployeeReviews existingReview = employeeReviewsRepository.getReviewByContractDetailId(contractDetailId);
        if (existingReview == null) {
            return employeeReviewsRepository.createReview(contractDetailId, rating, comment);
        } else {
            return employeeReviewsRepository.updateReview(existingReview.getReviewId(), rating, comment);
        }
    }

    // Lấy thông tin đánh giá theo contractDetailId
    public EmployeeReviews getReviewByContractDetailId(int contractDetailId) {
        return employeeReviewsRepository.getReviewByContractDetailId(contractDetailId);
    }

    // Lấy đánh giá theo reviewId
    public EmployeeReviews getReviewById(int reviewId) {
        return employeeReviewsRepository.getReviewById(reviewId);
    }
}
