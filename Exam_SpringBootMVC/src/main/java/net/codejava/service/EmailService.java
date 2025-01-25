package net.codejava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import net.codejava.model.Customers;
import net.codejava.model.Employees;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${email.from}")
    private String fromAddress;

    
    public void sendEmail(String toAddress, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);  // Lấy từ cấu hình
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    // Phương thức gửi nhắc nhở
    public void sendEmailReminder(String toAddress, String subject, String content) {
        sendEmail(toAddress, subject, content);  // Gọi phương thức chung
    }



    public void sendVerificationEmailforEmployees(Employees employee, String siteURL) {
        String toAddress = employee.getEmail();
        String fromAddress = "nguyentuyetnga.5@gmail.com"; 
        String senderName = "Happy Clean";
        String subject = "Please verify your registration";
        String verifyURL = siteURL + "/employee/verify?code=" + employee.getVerifyCode();

        String content = "Dear " + employee.getFullname() + ",\n\n"
                + "Thank you for registering. Please click the link below to verify your email address:\n"
                + verifyURL + "\n\n"
                + "Best regards,\n"
                + senderName;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
    
    public void sendVerificationEmailforCustomer(Customers customer, String siteURL) {
        String toAddress = customer.getEmail();
        String fromAddress = "nguyentuyetnga.5@gmail.com"; 
        String senderName = "Happy Clean";
        String subject = "Please verify your registration";
        String verifyURL = siteURL + "/customer/verify?code=" + customer.getVerifyCode();

        String content = "Dear " + customer.getFullname() + ",\n\n"
                + "Thank you for registering. Please click the link below to verify your email address:\n"
                + verifyURL + "\n\n"
                + "Best regards,\n"
                + senderName;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
    
    public void sendResetPasswordEmailforAdmin(Employees admin, String resetURL) {
        String toAddress = admin.getEmail();
        String fromAddress = "nguyentuyetnga.5@gmail.com"; // Replace with your email
        String senderName = "Happy Clean";
        String subject = "Reset Your Password";
        
        String content = "Dear " + admin.getFullname() + ",\n\n"
                + "We received a request to reset your password. Click the link below to change your password:\n"
                + resetURL + "\n\n"
                + "If you did not request a password reset, you can ignore this email.\n\n"
                + "Best regards,\n"
                + senderName;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
    
    public void sendResetPasswordEmailforEmployees(Employees employee, String resetURL) {
        String toAddress = employee.getEmail();
        String fromAddress = "nguyentuyetnga.5@gmail.com"; // Replace with your email
        String senderName = "Happy Clean";
        String subject = "Reset Your Password";
        
        String content = "Dear " + employee.getFullname() + ",\n\n"
                + "We received a request to reset your password. Click the link below to change your password:\n"
                + resetURL + "\n\n"
                + "If you did not request a password reset, you can ignore this email.\n\n"
                + "Best regards,\n"
                + senderName;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
    
    public void sendResetPasswordEmailforCustomers(Customers customer, String resetURL) {
        String toAddress = customer.getEmail();
        String fromAddress = "nguyentuyetnga.5@gmail.com"; // Replace with your email
        String senderName = "Happy Clean";
        String subject = "Reset Your Password";
        
        String content = "Dear " + customer.getFullname() + ",\n\n"
                + "We received a request to reset your password. Click the link below to change your password:\n"
                + resetURL + "\n\n"
                + "If you did not request a password reset, you can ignore this email.\n\n"
                + "Best regards,\n"
                + senderName;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
    
    
}