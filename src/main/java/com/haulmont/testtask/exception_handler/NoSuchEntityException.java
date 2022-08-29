package com.haulmont.testtask.exception_handler;

public abstract class NoSuchEntityException extends RuntimeException {
    public NoSuchEntityException(String message) {
        super(message);
    }
}
