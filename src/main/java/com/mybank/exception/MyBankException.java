package com.mybank.exception;

public class MyBankException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public final Integer code;

    public MyBankException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
