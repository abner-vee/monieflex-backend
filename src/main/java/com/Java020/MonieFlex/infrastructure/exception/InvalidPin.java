package com.Java020.MonieFlex.infrastructure.exception;

public class InvalidPin extends RuntimeException{

    public InvalidPin(String message) {
        super(message);
    }
}
