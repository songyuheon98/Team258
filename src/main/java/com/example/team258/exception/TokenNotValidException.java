package com.example.team258.exception;

public class TokenNotValidException extends RuntimeException {
    public TokenNotValidException(String msg){
        super(msg);
    }
}
