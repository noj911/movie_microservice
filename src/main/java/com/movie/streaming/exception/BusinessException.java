package com.movie.streaming.exception;

public class BusinessException extends RuntimeException {
    private final String code;

    protected BusinessException(String message) {
        super(message);
        this.code = code;
    }
    public String getCode() {
        return code;
    }

}
