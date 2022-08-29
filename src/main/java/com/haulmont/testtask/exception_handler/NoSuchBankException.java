package com.haulmont.testtask.exception_handler;

public class NoSuchBankException extends RuntimeException {
    public NoSuchBankException(String message) {
        super(message);
    }
}
