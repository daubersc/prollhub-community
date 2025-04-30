package com.prollhub.community.logic.service.impl;

import com.prollhub.community.dto.auth.UserInfoDTO;
import com.prollhub.community.exception.MailServerNotAvailableException;
import com.prollhub.community.logic.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String from;



    @Override
    public void send(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void send(String to, String subject, String content, boolean isHtml)  {
        if (isHtml) {
            try {
                MimeMessage msg = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(content, true);
                mailSender.send(msg);

            } catch (Exception e) {
                throw new MailServerNotAvailableException(e.getMessage());
            }
        } else {
            send(to, subject, content);
        }

    }

    @Override
    public void sendVerificationEmail(UserInfoDTO user, String verificationCode) {
        String subject = "Verify your email Address";

        // Data passed to html
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getUsername());
        model.put("verificationCode", verificationCode);

        Context ctx = new Context();
        ctx.setVariables(model);


            // Process the template
        String htmlContent = templateEngine.process("verify-email", ctx);
        send(user.getEmail(), subject, htmlContent, true);
    }

    @Override
    public void sendMagicLink(UserInfoDTO user, String token) {
        String subject = "Login with Magic Link";

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getUsername());
        model.put("token", token);


        Context ctx = new Context();
        ctx.setVariables(model);

        String htmlContent = templateEngine.process("magic-link", ctx);
        send(user.getEmail(), subject, htmlContent, true);


    }



}
