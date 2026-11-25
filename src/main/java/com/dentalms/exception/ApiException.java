package com.dentalms.exception;

import org.springframework.http.HttpStatus;

/** Gabim i kontrolluar i biznesit: status HTTP + kod i qendrueshem + mesazh. */
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public static ApiException notFound(String code, String message) {
        return new ApiException(HttpStatus.NOT_FOUND, code, message);
    }

    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }
}
