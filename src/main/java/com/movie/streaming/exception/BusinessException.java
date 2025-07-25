package com.movie.streaming.exception;

public class BusinessException extends RuntimeException {
    private final String code;

    protected BusinessException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR"; // Default error code
    }

    protected BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }
    public String getCode() {
        return code;
    }

}
