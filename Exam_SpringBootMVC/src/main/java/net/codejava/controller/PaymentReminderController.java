package net.codejava.controller;

import net.codejava.service.ContractService;
import net.codejava.service.EmailService;
import net.codejava.model.Contracts;
import net.codejava.service.CustomerService;  // Import CustomerService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/payment-reminder")
public class PaymentReminderController {

    private final ContractService contractService;
    private final EmailService emailService;
    private final CustomerService customerService;  // Declare CustomerService
    private static final Logger logger = LoggerFactory.getLogger(PaymentReminderController.class);

    // Add CustomerService to the constructor and inject it
    @Autowired
    public PaymentReminderController(ContractService contractService, EmailService emailService, CustomerService customerService) {
        this.contractService = contractService;
        this.emailService = emailService;
        this.customerService = customerService;  // Initialize customerService
    }

    @PostMapping("/sendReminder/{contractId}")
    public ResponseEntity<String> sendPaymentReminderEmail(@PathVariable int contractId) {
        try {
            // Lấy thông tin hợp đồng và email khách hàng từ ContractService
            Contracts contract = contractService.getContractById(contractId);
            String customerEmail = customerService.getCustomerEmail(contract.getCustomerId());

            if (customerEmail == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer email not found.");
            }

            // Tạo nội dung email
            String subject = "Payment Reminder";
            String content = "Dear customer, \n\n"
                             + "This is a reminder for your contract with ID: " + contract.getContractId() 
                             + ". Please make sure to complete the payment.\n\n"
                             + "Best regards,\n"
                             + "Your Company";

            // Gửi email
            emailService.sendEmail(customerEmail, subject, content);

            return ResponseEntity.ok("Payment reminder email sent successfully.");

        } catch (Exception e) {
            logger.error("Error sending payment reminder email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
        }
    }
	/*
	 * // Phương thức lên lịch gửi nhắc nhở thanh toán định kỳ
	 * 
	 * @Scheduled(cron = "0 0 1,5 * * ?") // Định kỳ vào ngày 1 và 5 của mỗi tháng
	 * lúc 00:00 public void sendMonthlyPaymentReminders() { LocalDate today =
	 * LocalDate.now();
	 * 
	 * if (today.getDayOfMonth() == 1) { // Ngày 1 của tháng: Gửi email nhắc nhở
	 * thanh toán cho tất cả hợp đồng "by_month" List<Contracts> contracts =
	 * contractService.getAllContractsByType("by_month"); for (Contracts contract :
	 * contracts) { LocalDate paymentDueDate = today.withDayOfMonth(10); // Mặc định
	 * ngày thanh toán là ngày 10 của tháng sendPaymentReminderEmail(contract,
	 * paymentDueDate); } logger.
	 * info("Sent payment reminders to all 'by_month' contracts for the 10th of the month."
	 * ); } else if (today.getDayOfMonth() == 5) { // Ngày 5 của tháng: Kiểm tra các
	 * hợp đồng chưa thanh toán và gửi email List<Contracts> contracts =
	 * contractService.getAllContractsByType("by_month"); for (Contracts contract :
	 * contracts) { if (!isPaymentCompleted(contract)) { // Nếu hợp đồng chưa thanh
	 * toán, gửi email nhắc nhở LocalDate paymentDueDate = today.withDayOfMonth(10);
	 * // Mặc định ngày thanh toán là ngày 10 của tháng
	 * sendPaymentReminderEmail(contract, paymentDueDate);
	 * logger.info("Sent payment reminder to contractId {} for overdue payment.",
	 * contract.getContractId()); } } } }
	 */

    // Phương thức kiểm tra thanh toán đã hoàn tất chưa
    private boolean isPaymentCompleted(Contracts contract) {
        // Logic kiểm tra trạng thái thanh toán của hợp đồng
        return contract.getPaymentStatus() == 1;
    }

    // Gửi email nhắc nhở thanh toán cho hợp đồng cụ thể
    private void sendPaymentReminderEmail(Contracts contract, LocalDate paymentDueDate) {
        // Lấy email khách hàng từ ContractService
        String recipient = contractService.getCustomerEmailByContractId(contract.getContractId());  
        if (recipient != null) {
            String subject = "Payment Reminder";
            String body = String.format("Dear customer, your payment is due on %s. Please make the payment to avoid any service disruption.", paymentDueDate);
            emailService.sendEmail(recipient, subject, body);
        } else {
            logger.warn("Email not found for contractId {}", contract.getContractId());
        }
    }
}
