package com.example.demo.common;

public class FatalException extends RuntimeException {

    public FatalException() {
    }

    public FatalException(String message) {
        super(message);
    }
}
