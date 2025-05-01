package com.prollhub.community.controller.api;

import com.prollhub.community.exception.ErrorCode;
import com.prollhub.community.exception.ErrorResponse;
import com.prollhub.community.logic.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validate")
@RequiredArgsConstructor
public class ValidateController {

    private static final Logger log = LoggerFactory.getLogger(ValidateController.class);

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;



    /**
     * Retrieves details of the currently authenticated user. Requires authentication.
     * @return ResponseEntity containing user details or an error if not authenticated.
     */
    @GetMapping("/")
    public ResponseEntity<?> getCurrentUser(@RequestParam(required = false) String username, @RequestParam(required = false) String email) {
        boolean exists;
        ErrorCode code;
        if (username != null) {
            log.info("Validating username with value {}", username);
            exists = accountService.findByUsername(username).isPresent();
            code = ErrorCode.USERNAME_TAKEN;
        } else if (email != null) {
            log.info("validating email with value {}", email);
            exists = accountService.findByEmail(email).isPresent();
            code = ErrorCode.EMAIL_TAKEN;
        } else {
            return ResponseEntity.badRequest().body(new ErrorResponse(ErrorCode.FORMAT_ERROR, "No query parameter provided."));
        }

        return exists ? ResponseEntity.ok("") : ResponseEntity.status(code.getHttpStatus()).body(new ErrorResponse(code));
    }


}
