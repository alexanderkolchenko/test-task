package com.haulmont.testtask.exception_handler;

public class NoSuchCreditPaymentsException extends RuntimeException{
    public NoSuchCreditPaymentsException(String message) {
        super(message);
    }
}
