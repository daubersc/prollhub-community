package com.prollhub.community.controller.web;

import com.prollhub.community.exception.ErrorCode;
import com.prollhub.community.exception.ErrorResponse;
import com.prollhub.community.exception.TokenExpiredException;
import com.prollhub.community.exception.TokenNotFoundException;
import com.prollhub.community.logic.service.AccountService;
import com.prollhub.community.logic.service.TokenService;

import com.prollhub.community.persistency.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class MailController {
    // TODO: Integrate into HTML (see also integrationExample.html


    private final AccountService accountService;
    @Value("${app.base-domain}")
    private String baseDomain;

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public MailController(TokenService tokenService, UserDetailsService userDetailsService, AccountService accountService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.accountService = accountService;
    }


    @RequestMapping(value = {"/login"})
    public String handleMagicLink(@RequestParam("token") String token, Model model) {
        ErrorCode errorCode;


        // Get the account
        try {
            Account account = tokenService.getAccountFromLinkToken(token);

            // Authenticate
            UserDetails userDetails = userDetailsService.loadUserByUsername(account.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            tokenService.deleteLinkToken(token);

            // Return view.
            return "dashboard";

        } catch (TokenNotFoundException e) {
            errorCode = ErrorCode.TOKEN_INVALID;
            model.addAttribute("error", errorCode.name());
            return "index";

        } catch (TokenExpiredException e) {
            errorCode = ErrorCode.TOKEN_EXPIRED;
            model.addAttribute("error", errorCode.name());
            return "index";
        }
    }

    @RequestMapping(value = {"/verify-email"})
    public String handleEmailVerification(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        ErrorCode errorCode;


        // Get the account
        try {
            Account account = tokenService.getAccountFromVerificationToken(token);
            accountService.activateAccount(account);

            // Authenticate
            UserDetails userDetails = userDetailsService.loadUserByUsername(account.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // delete the token
            tokenService.deleteVerificationToken(token);

            // Return view.
            return "dashboard";

        } catch (TokenNotFoundException e) {
            errorCode = ErrorCode.TOKEN_INVALID;
            model.addAttribute("error", errorCode.name());
            return "index";

        } catch (TokenExpiredException e) {
            errorCode = ErrorCode.TOKEN_EXPIRED;
            model.addAttribute("error", errorCode.name());
            return "index";
        }
    }

}
