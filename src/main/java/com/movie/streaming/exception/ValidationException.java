package com.movie.streaming.exception;

import java.util.Map;

public class ValidationException extends BusinessException {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Erreur de validation");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
