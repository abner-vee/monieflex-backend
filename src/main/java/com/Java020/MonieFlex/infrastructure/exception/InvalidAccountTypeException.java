package com.Java020.MonieFlex.infrastructure.exception;

public class InvalidAccountTypeException extends RuntimeException{
    public InvalidAccountTypeException(String message) {
        super(message);
    }
}
