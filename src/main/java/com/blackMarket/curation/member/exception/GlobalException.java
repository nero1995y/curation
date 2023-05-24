package com.blackMarket.curation.member.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class GlobalException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
