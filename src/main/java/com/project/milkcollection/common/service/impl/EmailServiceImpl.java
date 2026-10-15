package com.project.milkcollection.common.service.impl;

import com.project.milkcollection.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendPasswordResetEmail(
            String email,
            String resetToken
    ) {

        String resetLink =
                "http://localhost:8082/reset-password?token="
                        + resetToken;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Reset Your Password");

        message.setText("""
                Hello,

                We received a request to reset your password.

                Click the link below to reset your password:

                %s

                This link is valid for a limited time.

                If you did not request a password reset,
                please ignore this email.

                Regards,
                Milk Collection Management System
                """.formatted(resetLink));

        mailSender.send(message);
    }
}