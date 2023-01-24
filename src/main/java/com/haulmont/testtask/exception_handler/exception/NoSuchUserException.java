package com.haulmont.testtask.exception_handler.exception;

import com.haulmont.testtask.exception_handler.NoSuchEntityException;

public class NoSuchUserException extends NoSuchEntityException {
    public NoSuchUserException(String message) {
        super(message);
    }
}

