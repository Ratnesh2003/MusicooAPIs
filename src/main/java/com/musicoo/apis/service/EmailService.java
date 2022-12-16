package com.musicoo.apis.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private JavaMailSender javaMailSender;

    public void sendMessageWithAttachment(String from, String to, String subject, String msg) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setText(msg, true);
        javaMailSender.send(message);

    }
}
