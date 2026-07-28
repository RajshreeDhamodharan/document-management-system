package com.example.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ==========================================
    // Welcome Email
    // ==========================================
    public void sendRegistrationEmail(String toEmail, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Welcome to Document Management System");

        message.setText(
                "Dear " + name + ",\n\n"
                + "Your account has been created successfully.\n\n"
                + "You can now log in and access the Document Management System.\n\n"
                + "Regards,\n"
                + "Document Management System");

        mailSender.send(message);
    }

    // ==========================================
    // Forgot Password OTP
    // ==========================================
    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Password Reset OTP");

        message.setText(
                "Dear User,\n\n"
                + "Your One-Time Password (OTP) is:\n\n"
                + otp
                + "\n\nThis OTP is valid for 10 minutes.\n\n"
                + "Regards,\n"
                + "Document Management System");

        mailSender.send(message);
    }

    // ==========================================
    // Document Submitted
    // ==========================================
    public void sendSubmissionEmail(String toEmail,
                                    String documentTitle) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Document Submitted");

        message.setText(
                "Dear User,\n\n"
                + "Your document \"" + documentTitle
                + "\" has been submitted successfully.\n\n"
                + "It is now waiting for review.\n\n"
                + "Regards,\n"
                + "Document Management System");

        mailSender.send(message);
    }

    // ==========================================
    // Document Under Review
    // ==========================================
    public void sendReviewEmail(String toEmail,
                                String documentTitle) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Document Under Review");

        message.setText(
                "Dear User,\n\n"
                + "Your document \"" + documentTitle
                + "\" is currently under review.\n\n"
                + "Regards,\n"
                + "Document Management System");

        mailSender.send(message);
    }

    // ==========================================
    // Document Approved
    // ==========================================
    public void sendApprovalEmail(String toEmail,
                                  String documentTitle) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Document Approved");

        message.setText(
                "Congratulations!\n\n"
                + "Your document \"" + documentTitle
                + "\" has been approved successfully.\n\n"
                + "Regards,\n"
                + "Document Management System");

        mailSender.send(message);
    }

    // ==========================================
    // Document Rejected
    // ==========================================
    public void sendRejectionEmail(String toEmail,
                                   String documentTitle,
                                   String remarks) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Document Rejected");

        message.setText(
                "Dear User,\n\n"
                + "Your document \"" + documentTitle
                + "\" has been rejected.\n\n"
                + "Remarks:\n"
                + remarks
                + "\n\nPlease update the document and submit it again.\n\n"
                + "Regards,\n"
                + "Document Management System");

        mailSender.send(message);
    }

    // ==========================================
    // Document Archived
    // ==========================================
    public void sendArchiveEmail(String toEmail,
                                 String documentTitle) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Document Archived");

        message.setText(
                "Dear User,\n\n"
                + "Your document \"" + documentTitle
                + "\" has been archived successfully.\n\n"
                + "Regards,\n"
                + "Document Management System");

        mailSender.send(message);
    }
}