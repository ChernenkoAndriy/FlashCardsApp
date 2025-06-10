package com.example.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset Request - SLEEVE");

            // Використовуємо query параметр замість path параметра
            String resetUrl = baseUrl + "/resetpassword/:___?token=" + token;
            String emailBody = createPasswordResetEmailBody(resetUrl);

            message.setText(emailBody);
            mailSender.send(message);

            System.out.println("Password reset email sent to: " + toEmail);
            System.out.println("Reset URL: " + resetUrl);

        } catch (Exception e) {
            System.err.println("Failed to send email to: " + toEmail);
            e.printStackTrace();
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private String createPasswordResetEmailBody(String resetUrl) {
        return "Hello,\n\n" +
                "You have requested to reset your password for your SLEEVE account.\n\n" +
                "Please click on the following link to reset your password:\n" +
                resetUrl + "\n\n" +
                "This link will expire in 1 hour for security reasons.\n\n" +
                "If you did not request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "SLEEVE Team";
    }

    // Метод для тестування
    public void sendTestEmail(String toEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Test Email - SLEEVE");
            message.setText("This is a test email to verify email configuration.");

            mailSender.send(message);
            System.out.println("Test email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Failed to send test email to: " + toEmail);
            e.printStackTrace();
            throw new RuntimeException("Failed to send test email", e);
        }
    }
}