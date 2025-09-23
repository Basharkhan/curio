package com.curio.blog.exception;

public class InvalidCredentialsException extends  RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
