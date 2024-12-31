package net.codejava.service;

import net.codejava.model.EmployeeReviews;
import net.codejava.repository.EmployeeReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeReviewsService {

    private final EmployeeReviewsRepository employeeReviewsRepository;

    @Autowired
    public EmployeeReviewsService(EmployeeReviewsRepository employeeReviewsRepository) {
        this.employeeReviewsRepository = employeeReviewsRepository;
    }

    // Tạo hoặc cập nhật đánh giá
    public EmployeeReviews saveOrUpdateReview(int contractDetailId, int employeeId, int rating, String comment) {
        EmployeeReviews existingReview = employeeReviewsRepository.getReviewByContractDetailId(contractDetailId);
        if (existingReview == null) {
            return employeeReviewsRepository.createReview(contractDetailId, employeeId, rating, comment);
        } else {
            return employeeReviewsRepository.updateReview(existingReview.getReviewId(), employeeId, rating, comment);
        }
    }

    // Lấy thông tin đánh giá theo contractDetailId
    public EmployeeReviews getReviewByContractDetailId(int contractDetailId) {
        return employeeReviewsRepository.getReviewByContractDetailId(contractDetailId);
    }

    // Lấy tất cả đánh giá theo contractId
    public List<EmployeeReviews> getReviewByContractId(int contractId) {
        return employeeReviewsRepository.findByContractId(contractId);  // Gọi phương thức findByContractId
    }

    // Lấy đánh giá theo reviewId
    public EmployeeReviews getReviewById(int reviewId) {
        return employeeReviewsRepository.getReviewById(reviewId);
    }
}
