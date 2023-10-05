package com.example.team258.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
