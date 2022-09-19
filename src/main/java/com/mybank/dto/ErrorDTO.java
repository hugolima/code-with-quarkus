package com.mybank.dto;

public class ErrorDTO {

    public final Integer code;
    public final String message;

    public ErrorDTO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
