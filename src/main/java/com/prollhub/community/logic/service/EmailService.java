package com.prollhub.community.logic.service;

import com.prollhub.community.dto.auth.UserInfoDTO;

public interface EmailService {

    public void send(String to, String subject, String content);

    public void send(String to, String subject, String content, boolean isHtml);

    public void sendVerificationEmail(UserInfoDTO user, String verificationCode);

    public void sendMagicLink(UserInfoDTO user, String accessToken);

}
