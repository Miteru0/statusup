package com.statusup.statusup.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "Email Verification";
        String verificationUrl = "http://https://statusup-347c42d4df93.herokuapp.com/register/verify?token=" + token;
        String message = "Please verify your email by clicking the link: " + verificationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }

}
