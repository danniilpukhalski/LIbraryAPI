package com.modsen.booktrackerservice.handler;

import com.modsen.booktrackerservice.domain.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.domain.exception.ExceptionBody;
import com.modsen.booktrackerservice.domain.exception.ResourceNotFoundException;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("Resource not found: {}", e.getMessage());
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionBody handleDuplicateResourceException(DuplicateResourceException e) {
        log.error("Duplicate resource found: {}", e.getMessage());
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("No resource found: {}", e.getMessage());
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleAccessDenied(AccessDeniedException e) {
        log.error("Access denied: {}", e.getMessage());
        return new ExceptionBody("Access denied");
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleIllegalState(IllegalStateException e) {
        log.error("Illegal state: {}", e.getMessage());
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation failed: {}", e.getMessage());
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return exceptionBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Validation failed: {}", e.getMessage());
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed.");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage
                )));
        return exceptionBody;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handleAuthException(AuthException e) {
        log.error("Authorization failed: {}", e.getMessage());
        return new ExceptionBody("Failed to authorise");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleBadCredentialsException(AuthenticationException e) {
        log.error("Authentication failed: Invalid username or password");
        return new ExceptionBody("Invalid username or password");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Method not supported: {}", e.getMessage());
        return new ExceptionBody("Method not supported");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(Exception e) {
        log.error("Internal server error: {}", e.getMessage());
        return new ExceptionBody("Internal Server Error");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Data integrity violation: {}", e.getMessage());
        return new ExceptionBody("Book does not exist");
    }
}
