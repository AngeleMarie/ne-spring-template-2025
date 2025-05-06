package rw.rra.management.vehicles.commons;

import rw.rra.management.vehicles.commons.exceptions.BadRequestException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> handleIllegalArgumentExceptions(IllegalArgumentException ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        log.error("Illegal argument exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationErrors(MethodArgumentNotValidException ex){
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach( error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setProperty("validationErrors", errors);
        log.error("Validation error occurred: {}", errors);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<ProblemDetail> handleBadRequestException(BadRequestException ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        log.error("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    ResponseEntity<ProblemDetail> handleRateLimitException(RequestNotPermitted ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded. Try again later.");
        log.warn("Rate limit exceeded: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(problemDetail);
    }

    @ExceptionHandler(CallNotPermittedException.class)
    ResponseEntity<ProblemDetail> handleCircuitBreakerException(CallNotPermittedException ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Service is temporarily unavailable due to high load. Please try again later.");
        log.warn("Circuit breaker triggered: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problemDetail);
    }

    @ExceptionHandler(DisabledException.class)
    ResponseEntity<ProblemDetail> handleDisabledException(DisabledException ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        log.error("User disabled: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
    }

    @ExceptionHandler(MessagingException.class)
    ResponseEntity<ProblemDetail> handleMessagingException(MessagingException ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Messaging error occurred");
        log.error("Messaging error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex){
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        log.error("Unexpected error occurred: {}", ex.getMessage());
        log.error("Detailed exception:", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
