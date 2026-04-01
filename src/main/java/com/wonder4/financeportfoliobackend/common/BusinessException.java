package com.wonder4.financeportfoliobackend.common;

/**
 * Custom Business Logic Interruption Exception Safely stops operations when logical constraints are
 * unfulfilled (e.g. attempting to fetch a user that does not exist)
 */
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    // Defaults to 400 BAD REQUEST contextually
    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public int getCode() {
        return code;
    }
}
