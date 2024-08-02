package com.Java020.MonieFlex.infrastructure.exception;

public class CardExpiredException extends RuntimeException{
    public CardExpiredException(String message) {
        super(message);
    }
}
