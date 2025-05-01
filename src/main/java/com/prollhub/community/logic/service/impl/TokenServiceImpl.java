package com.prollhub.community.logic.service.impl;

import com.prollhub.community.exception.exceptions.AccountDisabledException;
import com.prollhub.community.exception.exceptions.TokenExpiredException;
import com.prollhub.community.exception.exceptions.TokenNotFoundException;
import com.prollhub.community.logic.service.TokenService;
import com.prollhub.community.persistency.model.Account;
import com.prollhub.community.persistency.model.InviteToken;
import com.prollhub.community.persistency.model.MagicLinkToken;
import com.prollhub.community.persistency.model.VerificationToken;
import com.prollhub.community.persistency.repository.InviteTokenRepository;
import com.prollhub.community.persistency.repository.MagicLinkTokenRepository;
import com.prollhub.community.persistency.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final InviteTokenRepository inviteTokenRepository;
    private final MagicLinkTokenRepository magicLinkTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private String createRandomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createInviteToken() {
        InviteToken token = new InviteToken(createRandomUUID());
        inviteTokenRepository.save(token);
        return token.getToken();
    }

    @Override
    public void isValidInviteToken(String token) {
        InviteToken inviteToken = inviteTokenRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token not found"));
        if (inviteToken.getExpires().isBefore(java.time.Instant.now())) {
            throw new TokenExpiredException("Token expired");
        }
    }

    @Override
    public void deleteInviteToken(String token) {
        inviteTokenRepository.findByToken(token).ifPresent(inviteToken -> inviteTokenRepository.delete(inviteToken));
    }

    @Override
    public String createMagicLinkToken(Account account) {
        if (account.isEnabled()) {
            MagicLinkToken token = new MagicLinkToken(createRandomUUID(), account);
            magicLinkTokenRepository.save(token);
            return token.getToken();
        } else {
            throw new AccountDisabledException("The account is disabled.");
        }
    }

    @Override
    public Account getAccountFromLinkToken(String token) {

        MagicLinkToken magicLinkToken = magicLinkTokenRepository.findByToken(token).orElseThrow(() ->new TokenNotFoundException("Token not found"));

        if (magicLinkToken.getExpires().isBefore(java.time.Instant.now())) {
            throw new TokenExpiredException("Token expired");
        }

        return magicLinkToken.getAccount();
    }

    @Override
    public void deleteLinkToken(String token) {
        magicLinkTokenRepository.findByToken(token).ifPresent(magicLinkToken -> magicLinkTokenRepository.delete(magicLinkToken));
    }

    @Override
    public String createVerificationToken(Account account) {
        VerificationToken token = new VerificationToken(createRandomUUID(), account);
        verificationTokenRepository.save(token);
        return token.getToken();
    }

    @Override
    public Account getAccountFromVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(() -> new TokenNotFoundException("Token not found"));

        if (verificationToken.getExpires().isBefore(java.time.Instant.now())) {
            throw new TokenExpiredException("Token expired");
        }

        return verificationToken.getAccount();
    }

    @Override
    public void deleteVerificationToken(String token) {
        verificationTokenRepository.findByToken(token).ifPresent(verificationToken -> verificationTokenRepository.delete(verificationToken));
    }
}
