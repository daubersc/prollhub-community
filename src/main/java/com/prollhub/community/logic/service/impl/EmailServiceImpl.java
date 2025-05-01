package com.prollhub.community.logic.service.impl;

import com.prollhub.community.dto.auth.UserInfoDTO;
import com.prollhub.community.exception.MailServerNotAvailableException;
import com.prollhub.community.logic.service.EmailService;
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
    public void send(String to, String subject, String content, boolean isHtml) {
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
    public void sendTemplateEmail(UserInfoDTO user, TemplateType type, String localization, String code, boolean isNewAccount) {

        Map<String, String> rb = new HashMap<>();
        String url = "https://prollhub.com/";

        // Set URL and Resource Bundle values
        switch (type) {
            case MAGIC_LINK -> {
                url += "login?token=" + code;
                if (localization.equalsIgnoreCase("de")) {
                    rb.put("title", "Login mit Magic Link.");


                    rb.put("description", "Du kannst dich mit dem Magic Link anmelden:");
                    rb.put("expiration", "Der Link ist für die nächsten 15 Minuten gültig. Sollte er ablaufen, musst du die Anfrage erneut stellen.");

                    rb.put("link", "Log mich ein.");
                    rb.put("fallback", "Falls der Link nicht funktioniert, kopiere bitte den folgenden Link in Deinen Browser:");
                    rb.put("security", "Falls du diese Anfrage nicht gestellt hast, ignoriere sie bitte.");

                    rb.put("support", "Benötigst Du Hilfe? Kontaktiere uns unter: ");
                    rb.put("email", "info@prollhub.com");
                    rb.put("greetings", "Danke,");
                    rb.put("signature", "Das Prollhub Team");

                } else {
                    rb.put("title", "Login with Magic Link.");

                    rb.put("description", "You can login with the Magic Link below:");
                    rb.put("expiration", "This link is valid for the next 15 minutes. Upon expiration you need to request a new link.");

                    rb.put("link", "Log me in");
                    rb.put("fallback", "If your link does not work, please copy and paste the following link into your browser:");
                    rb.put("security", "If you didn't request a login link using this email address, please discard this email.");
                    rb.put("support", "If you have any questions, please contact us at ");
                    rb.put("email", "info@prollhub.com");
                    rb.put("greetings", "Thanks,");
                    rb.put("signature", "The Prollhub Team");
                }
            }
            case VERIFY_EMAIL -> {
                url += "verify-email?token=" + code;
                if (localization.equalsIgnoreCase("de")) {
                    rb.put("title", "Bestätige Deine E-Mail Adresse.");

                    if (isNewAccount) {
                        rb.put("welcome", "Willkommen bei Prollhub!");
                        rb.put("description", "Um Deine Registrierung abzuschließen und dein Konto zu schützen, bestätige bitte Deine E-Mail Adresse per Klick auf den Link:");
                        rb.put("expiration", "Der Link ist für die nächsten 24 Stunden gültig. Sollte er ablaufen, musst Du Dich erneut registrieren.");
                    } else {
                        rb.put("description", "Um deine E-Mail Änderung abzuschließen, bestätige bitte Deine E-Mail Adresse per Klick auf den Link:");
                        rb.put("expiration", "Der Link ist für die nächsten 24 Stunden gültig. Sollte er ablaufen, musst du die Anfrage erneut stellen.");
                    }
                    rb.put("link", "E-Mail Adresse bestätigen.");
                    rb.put("fallback", "Falls der Link nicht funktioniert, kopiere bitte den folgenden Link in Deinen Browser:");
                    rb.put("security", "Falls Du kein Konto bei Prollhub mit dieser E-Mail-Adresse erstellt hast, ignoriere diese E-Mail bitte. Möglicherweise hat jemand Deine E-Mail-Adresse irrtümlich eingegeben.");

                    rb.put("support", "Benötigst Du Hilfe? Kontaktiere uns unter: ");
                    rb.put("email", "info@prollhub.com");
                    rb.put("greetings", "Danke,");
                    rb.put("signature", "Das Prollhub Team");

                } else {
                    rb.put("title", "verify your email address");

                    if (isNewAccount) {
                        rb.put("welcome", "Welcome to Prollhub!");
                        rb.put("description", "To complete your registration and secure your account please verify your email by clicking the link below:");
                        rb.put("expiration", "This verification link will be valid for the next 24 hours. Upon expiration you need to register anew.");
                    } else {
                        rb.put("description", "To complete your email change please verify your email by clicking the link below:");
                        rb.put("expiration", "This verification link will be valid for the next 24 hours. Upon expiration you need issue this change again.");
                    }
                    rb.put("link", "Verify Email Address");
                    rb.put("fallback", "If your link does not work, please copy and paste the following link into your browser:");
                    rb.put("security", "If you didn't create an account with Prollhub using this email address, please discard this email. Someone may have entered your email address by mistake.");
                    rb.put("expiration", "This verification link will be valid for the next 24 hours. Upon expiration you need to register anew.");
                    rb.put("support", "If you have any questions, please contact us at ");
                    rb.put("email", "info@prollhub.com");
                    rb.put("greetings", "Thanks,");
                    rb.put("signature", "The Prollhub Team");
                }
            }
            default -> throw new IllegalArgumentException("Invalid template type: " + type);
        }

        // Prepare Model
        String subject = rb.get("title") != null ? rb.get("title") : "Verify your email address";
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getUsername());
        model.put("url", url);
        model.putAll(rb);

        Context ctx = new Context();
        ctx.setVariables(model);
        String htmlContent = templateEngine.process("email-template", ctx);
        send(user.getEmail(), subject, htmlContent, true);
    }
}
