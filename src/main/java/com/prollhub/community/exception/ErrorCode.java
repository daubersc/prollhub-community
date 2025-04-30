package com.prollhub.community.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    /*
    400 - Bad Request
    The server cannot or will not process the request due to something that is perceived to be a client error
    (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).
     */
    FORMAT_ERROR( HttpStatus.BAD_REQUEST, "Format error in path {}."),
    VALIDATION_ERROR( HttpStatus.BAD_REQUEST, "The request failed due to input validation."),

    /*
    401 Unauthorized
    Although the HTTP standard specifies "unauthorized", semantically this response means "unauthenticated". That is,
    the client must authenticate itself to get the requested response.
     */
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password provided"),
    LOCKED(HttpStatus.UNAUTHORIZED, "Your account is currently disabled"),
    UNAUTHORIZED( HttpStatus.UNAUTHORIZED, "You need to login to access this resource"),

    // 402 - Payment required (NYI)

    /*
    403 - Forbidden
    The client does not have access rights to the content; that is, it is unauthorized, so the server is refusing to
    give the requested resource. Unlike 401 Unauthorized, the client's identity is known to the server.
     */
    ROLE_INVALID(HttpStatus.FORBIDDEN, "You have insufficient permissions to access this resource"),

    /*
    404 - Not Found
    The server cannot find the requested resource. In the browser, this means the URL is not recognized. In an API,
    this can also mean that the endpoint is valid but the resource itself does not exist. Servers may also send this
    response instead of 403 Forbidden to hide the existence of a resource from an unauthorized client. This response
    code is probably the most well known due to its frequent occurrence on the web.
     */

    /*
    405 - Method not allowed
    The request method is known by the server but is not supported by the target resource. For example, an API may not
    allow DELETE on a resource, or the TRACE method entirely.
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "The requested method is not allowed for this resource"),

    // 409 Conflict
    USERNAME_TAKEN(HttpStatus.CONFLICT, "The username {} is already taken."),
    EMAIL_TAKEN(HttpStatus.CONFLICT, "The email {} is already taken"),

    // 424 Failed Dependency
    FAILED_DEPENDENCY(HttpStatus.FAILED_DEPENDENCY, "The request failed due to a dependency failure."),
    MAIL_SERVER_NOT_AVAILABLE(HttpStatus.FAILED_DEPENDENCY, "The mail server is not available, please try again later."),
    DB_SERVER_NOT_AVAILABLE(HttpStatus.FAILED_DEPENDENCY, "The database server is not available, please try again later."),

    // 500 - generic
    INTERNAL_SERVER_ERROR( HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");


    private final HttpStatus httpStatus;
    private final String defaultMessage;

}
