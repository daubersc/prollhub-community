package com.prollhub.community.logic.service;

import com.prollhub.community.dto.auth.UserInfoDTO;

public interface EmailService {



    public enum TemplateType {
        VERIFY_EMAIL,
        MAGIC_LINK
    }

    public void send(String to, String subject, String content);

    public void send(String to, String subject, String content, boolean isHtml);

    public void sendTemplateEmail(UserInfoDTO user, TemplateType type, String localization, String code, boolean isNewAccount);

}
