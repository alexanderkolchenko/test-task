package com.haulmont.testtask.exception_handler;

public class NoSuchCreditException extends RuntimeException{
    public NoSuchCreditException(String message) {
        super(message);
    }
}
