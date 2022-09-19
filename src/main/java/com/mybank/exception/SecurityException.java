package com.mybank.exception;

public class SecurityException extends MyBankException {

    private static final long serialVersionUID = 1L;

    public SecurityException(Integer code, String message) {
        super(code, message);
    }
}
