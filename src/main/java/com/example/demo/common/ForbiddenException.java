package com.example.demo.common;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
    }

    public ForbiddenException(Throwable e) {
        super(e);
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
