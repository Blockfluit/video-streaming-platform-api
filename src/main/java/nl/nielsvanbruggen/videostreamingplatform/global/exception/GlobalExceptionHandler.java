package nl.nielsvanbruggen.videostreamingplatform.global.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        return new ResponseEntity<>(singleMessageToErrorMap(usernameNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AlreadyInUseException.class)
    public ResponseEntity<?> handleAlreadyInUseException(AlreadyInUseException alreadyInUseException) {
        return new ResponseEntity<>(singleMessageToErrorMap(alreadyInUseException.getMessage()), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return new ResponseEntity<>(singleMessageToErrorMap(illegalArgumentException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    public static Map<String, List<String>> singleMessageToErrorMap(String message) {
        List<String> errors = new ArrayList<>();
        errors.add(message);
        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);
        return result;
    }
}
