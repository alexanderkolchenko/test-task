package com.haulmont.testtask.exception_handler;

public class NoSuchCustomerException extends RuntimeException{
    public NoSuchCustomerException(String message) {
        super(message);
    }
}
