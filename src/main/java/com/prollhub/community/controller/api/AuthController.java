package com.prollhub.community.controller.api;

import com.prollhub.community.dto.SuccessResponse;
import com.prollhub.community.dto.WarningResponse;
import com.prollhub.community.dto.auth.LoginRequest;
import com.prollhub.community.dto.auth.RegisterRequest;
import com.prollhub.community.dto.auth.UserInfoDTO;
import com.prollhub.community.exception.exceptions.DuplicateEmailException;
import com.prollhub.community.exception.exceptions.DuplicateUsernameException;
import com.prollhub.community.exception.ErrorCode;
import com.prollhub.community.exception.ErrorResponse;
import com.prollhub.community.exception.exceptions.TokenExpiredException;
import com.prollhub.community.exception.exceptions.TokenNotFoundException;
import com.prollhub.community.logic.service.*;
import com.prollhub.community.persistency.model.Account;
import com.prollhub.community.persistency.model.InviteToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;
    private final LoginAttemptService loginAttemptService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final MagicLinkService magicLinkService;

    @PostMapping("/magic-link")
    public ResponseEntity<?> sendMagicLink(@RequestBody LoginRequest loginRequest, Locale locale) {

       try {
           magicLinkService.processMagicLinkRequest(loginRequest.getEmail(), locale);
       } catch (Exception ignoreMe) { }


        // We don't want to give out the information whether the email address exists or not
        WarningResponse<Void> response = new WarningResponse<>(
                HttpStatus.ACCEPTED,
                "MAIL_PENDING",
                "An email is sent if the email exists in the systems.", null);


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    /**
     * Handles user login reuqests via REST API.
     * @param loginRequest the loginRequest containing username and password
     * @return the expected loginResponse entity
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, Locale locale) {
        log.info("Attempted login for user {}.", loginRequest.getEmail());
        ErrorCode code;
        ErrorResponse err;

        // We need to retrieve the account's username in order to login
        Account acc = accountService.findByEmail(loginRequest.getEmail()).orElse(null);

        // Check for existence
        if (acc == null || loginRequest.getPassword() == null) {
            log.warn("Login failed for user '{}': Invalid credentials", loginRequest.getEmail());
            code = ErrorCode.BAD_CREDENTIALS;
            return ResponseEntity.status(code.getHttpStatus()).body(new ErrorResponse(code));
        }

        // Too many attempts -> block login
        if (loginAttemptService.isBlocked(loginRequest.getEmail())) {
            emailService.sendTemplateEmail(new UserInfoDTO(acc), EmailService.TemplateType.ACCOUNT_LOCKED, locale.getLanguage(), null, false);
            code = ErrorCode.LOCKED;
            return ResponseEntity.status(code.getHttpStatus()).body(new ErrorResponse(code));
        }

        // account disabled -> block login
        if (!acc.isEnabled()) {
            code = ErrorCode.LOCKED;
            return ResponseEntity.status(code.getHttpStatus()).body(new ErrorResponse(code));
        }


        try {
            UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    acc.getUsername(),
                    loginRequest.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(passwordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Object principal = authentication.getPrincipal();

            if (!(principal instanceof Account authenticatedAccount)) {
                log.error("Authentication principal is not an instance of Account: {}", principal.getClass());
                code = ErrorCode.INTERNAL_SERVER_ERROR;
                return ResponseEntity.status(code.getHttpStatus()).body(new ErrorResponse(code));
            }

            log.info("User {} logged in successfully.", authenticatedAccount.getUsername());
            loginAttemptService.loginSucceeded(loginRequest.getEmail());
            UserInfoDTO userInfoDTO = new UserInfoDTO(authenticatedAccount);

            SuccessResponse<UserInfoDTO> response = new SuccessResponse<>(userInfoDTO);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            // Handle incorrect username/password specifically
            log.warn("Login failed for user '{}': Invalid credentials", loginRequest.getEmail());
            loginAttemptService.loginFailed(loginRequest.getEmail());
            code = ErrorCode.BAD_CREDENTIALS;

        } catch (AuthenticationException e) {
            // Handle other authentication errors (e.g., account locked, disabled)
            log.error("Authentication failed for user '{}': {}", loginRequest.getEmail(), e.getMessage());
            code = ErrorCode.LOCKED;
        } catch (Exception e) {
            // Catch unexpected errors
            log.error("An unexpected error occurred during login for user '{}': {}", loginRequest.getEmail(), e.getMessage(), e);
            code = ErrorCode.INTERNAL_SERVER_ERROR;
        }

        err = new ErrorResponse(code);
        return ResponseEntity.status(code.getHttpStatus()).body(err);
    }

    /**
     * Handles user registration requests.
     * @param request DTO containing user details for registration.
     * @return ResponseEntity indicating success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request, Locale locale) {
        log.info("Received registration request for username: {}", request.getUsername());
        ErrorCode code = null;

        try {
            tokenService.isValidInviteToken(request.getToken());
        } catch (TokenNotFoundException e) {
            log.warn("Registration failed for non-existing token {}", request.getToken());
            code = ErrorCode.TOKEN_INVALID;
        } catch (TokenExpiredException e) {
            code = ErrorCode.TOKEN_EXPIRED;
        }

        // Token valid?
        if (code != null) {
            ErrorResponse errorResponse = new ErrorResponse(code);
            return ResponseEntity.status(code.getHttpStatus()).body(errorResponse);
        }

        Account registeredAccount = null;

        try {

            registeredAccount = accountService.registerNewAccount(request);

        } catch (DuplicateUsernameException e) {
            log.warn("Registration failed for duplicated username {}", request.getUsername());
            code = ErrorCode.USERNAME_TAKEN;

        } catch (DuplicateEmailException e) {
            log.warn("Registration failed for duplicated email {}", request.getEmail());
            code = ErrorCode.EMAIL_TAKEN;

        } catch (Exception e) {
            // Catch unexpected errors during registration
            log.error("Unexpected error during registration for {}: {}", request.getUsername(), e.getMessage(), e);
            code = ErrorCode.INTERNAL_SERVER_ERROR;
        }

        // Registering worked ?
        if (code != null) {
            ErrorResponse errorResponse = new ErrorResponse(code);
            return ResponseEntity.status(code.getHttpStatus()).body(errorResponse);
        }

        String token = tokenService.createVerificationToken(registeredAccount);

        try {
            emailService.sendTemplateEmail(new UserInfoDTO(registeredAccount), EmailService.TemplateType.VERIFY_EMAIL, locale.getLanguage(), token, true);
            WarningResponse<Void> response = new WarningResponse<>(
                    HttpStatus.CREATED,
                    "VERIFICATION_REQUIRED",
                    "User registered successfully but needs email verification to be enabled",
                    null);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Skip in case of exception - this is some server issue then (todo evaluate if this can be more granular)
            accountService.activateAccount(registeredAccount);
            tokenService.deleteVerificationToken(token);

            SuccessResponse<UserInfoDTO> response = new SuccessResponse<>(HttpStatus.CREATED, new UserInfoDTO(registeredAccount));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            tokenService.deleteInviteToken(request.getToken());
        }







    }

    /**
     * Handles user logout requests. Requires user to be authenticated.
     * Invalidates the current session.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ResponseEntity indicating logout success.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Get the current authentication context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            log.info("User '{}' logged out successfully.", authentication.getName());
        } else {
            log.warn("Logout attempt received, but no user was authenticated.");
        }
        return ResponseEntity.ok("User logged out successfully!");
    }

    /**
     * Retrieves details of the currently authenticated user. Requires authentication.
     * @return ResponseEntity containing user details or an error if not authenticated.
     */
    @GetMapping("/self")
    public ResponseEntity<?> getCurrentUser() {
        // Get authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated and not anonymous
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            log.warn("Attempt to access /self endpoint without authentication.");
            WarningResponse<UserInfoDTO> warningResponse = new WarningResponse<>(HttpStatus.OK, "AUTHENTICATION_FAILED", "No authenticated user found", null);
            return ResponseEntity.ok(warningResponse);
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Account currentAccount)) {
            log.error("/me endpoint principal is not an instance of Account: {}", principal.getClass());
            ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
            ErrorResponse errorResponse = new ErrorResponse(errorCode, "Unexpected user details type.");
            return ResponseEntity.status(errorCode.getHttpStatus()).body(errorResponse);
        }
        log.info("Fetching details for currently authenticated user: {}", currentAccount.getUsername());

        // --- Change: Create UserInfoDTO from the current Account ---
        UserInfoDTO userInfoDTO = new UserInfoDTO(currentAccount);
        // ----------------------------------------------------------

        // --- Change: Use SuccessResponse with UserInfoDTO ---
        SuccessResponse<UserInfoDTO> response = new SuccessResponse<>(
                HttpStatus.OK,
                userInfoDTO
        );

        return ResponseEntity.ok(response);
    }

}
