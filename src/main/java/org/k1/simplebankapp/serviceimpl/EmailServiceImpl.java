package org.k1.simplebankapp.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.k1.simplebankapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.sender.name:}")
    private String senderName;

    @Value("${spring.mail.sender.mail:}")
    private String senderEmail;

    @Qualifier("taskExecutor")
    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public void send(String email, String subject, String message) {
        MimeMessage mime = mailSender.createMimeMessage();
        String from = senderEmail;
        try {
            log.info("Sending email to: " + email);
            log.info("Sending email from: " + from);
            log.info("Sending email with subject: " + subject);
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);
            helper.setFrom(from, senderName);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(mime);
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
        }

    }

    @Override
    public void sendAsync(final String to, final String subject, final String message) {
        taskExecutor.execute(() -> send(to, subject, message));
    }

}

