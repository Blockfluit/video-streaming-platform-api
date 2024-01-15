package nl.nielsvanbruggen.videostreamingplatform.global.exception;


import com.sun.jdi.InternalException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleUsernameNotFoundException(HttpServletRequest req, UsernameNotFoundException usernameNotFoundException) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(usernameNotFoundException.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AlreadyInUseException.class)
    public ResponseEntity<ErrorInfo> handleAlreadyInUseException(HttpServletRequest req, AlreadyInUseException alreadyInUseException) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(alreadyInUseException.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> handleIllegalArgumentException(HttpServletRequest req, IllegalArgumentException illegalArgumentException) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(illegalArgumentException.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ErrorInfo> handleInternalException(HttpServletRequest req, InternalException internalException) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(internalException.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorInfo> handleExpiredJwtException(HttpServletRequest req, ExpiredJwtException expiredJwtException) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(expiredJwtException.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleResourceNotFoundException(HttpServletRequest req, ResourceNotFoundException resourceNotFoundException) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(resourceNotFoundException.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleDefaultExceptionHandler(HttpServletRequest req, Exception exception) {
        ErrorInfo info = ErrorInfo.builder()
                .url(req.getRequestURL().toString())
                .timestamp(Instant.now())
                .message(exception.getMessage())
                .build();

        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }
}
