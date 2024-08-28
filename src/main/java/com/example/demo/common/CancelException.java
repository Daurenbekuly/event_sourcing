package com.example.demo.common;

public class CancelException extends RuntimeException {

    public CancelException() {
    }

    public CancelException(String message) {
        super(message);
    }
}
