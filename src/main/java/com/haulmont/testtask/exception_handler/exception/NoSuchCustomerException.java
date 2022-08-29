package com.haulmont.testtask.exception_handler.exception;

import com.haulmont.testtask.exception_handler.NoSuchEntityException;

public class NoSuchCustomerException extends NoSuchEntityException {
    public NoSuchCustomerException(String message) {
        super(message);
    }
}
