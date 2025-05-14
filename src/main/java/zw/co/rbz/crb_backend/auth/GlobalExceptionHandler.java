package zw.co.rbz.crb_backend.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 🔐 JWT Token Validation Errors
    @ExceptionHandler(JwtException.class)
    public void handleJwtException(HttpServletRequest request, HttpServletResponse response, JwtException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(
                "invalid_token",
                "Invalid/Expired Authentication Credentials"
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    // 🚫 Access Denied (e.g., wrong role)
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(
                "access_denied",
                ex.getMessage()
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    // 🧑‍⚖️ General Authentication Issues
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(
                "unauthorized",
                ex.getMessage()
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    // ❌ Resource Not Found
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleResourceNotFoundException(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(
                "not_found",
                "The requested resource was not found"
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    // ⚠️ General Server Errors
    @ExceptionHandler(Exception.class)
    public void handleGeneralException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(
                "server_error",
                "An unexpected error occurred"
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    // 📥 Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationExceptions(
            HttpServletRequest request,
            HttpServletResponse response,
            MethodArgumentNotValidException ex) throws IOException {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.put(error.getObjectName(), error.getDefaultMessage()));

        ErrorResponse errorResponse = ErrorResponse.withDetails(
                "validation_error",
                "Input validation failed",
                errors
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}



/*
Sample Errors
1. Expired/Invalid Tokens
{
  "timestamp": "2025-05-12T07:25:26",
  "error": "invalid_token",
  "error_description": "Jwt expired at 2025-05-12T07:25:26Z"
}

2.Access Denied
{
  "timestamp": "2025-05-12T07:25:26",
  "error": "access_denied",
  "error_description": "User not granted access"
}

3. End Point Not found
{
  "timestamp": "2025-05-12T07:25:26",
  "error": "not_found",
  "error_description": "The requested resource was not found"
}

4. Unexpected Server Error
{
  "timestamp": "2025-05-12T07:25:26",
  "error": "server_error",
  "error_description": "An unexpected error occurred"
}

5. Request Data Validation
{
  "timestamp": "2025-05-12T07:25:26",
  "error": "validation_error",
  "error_description": "Input validation failed",
  "details": {
    "email": "must be a valid email address",
    "username": "size must be between 3 and 50"
  }
}
 */
