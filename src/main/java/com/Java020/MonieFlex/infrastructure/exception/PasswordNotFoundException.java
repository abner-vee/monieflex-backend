package com.Java020.MonieFlex.infrastructure.exception;

public class PasswordNotFoundException extends RuntimeException{
    public PasswordNotFoundException(String message) {
        super(message);
    }
}
