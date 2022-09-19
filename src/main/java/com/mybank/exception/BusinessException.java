package com.mybank.exception;

public class BusinessException extends MyBankException {

    private static final long serialVersionUID = 1L;

    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}
