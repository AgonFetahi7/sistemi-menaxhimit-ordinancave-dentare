package com.dentalms.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Format i njejte per te gjitha gabimet: { success:false, error, message, details? } */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> body(HttpStatus status, String code, String message,
                                                     Map<String, String> details) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("success", false);
        m.put("error", code);
        m.put("message", message);
        if (details != null && !details.isEmpty()) m.put("details", details);
        return ResponseEntity.status(status).body(m);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApi(ApiException ex) {
        return body(ex.getStatus(), ex.getCode(), ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> details = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(f -> details.put(f.getField(), f.getDefaultMessage()));
        return body(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed.", details);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> handleBadInput(Exception ex) {
        return body(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST",
                "Request body or parameter is malformed (check JSON and date format).", null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        return body(HttpStatus.CONFLICT, "CONSTRAINT_VIOLATION",
                "Operation violates a database constraint (duplicate value or record still in use).", null);
    }
}
