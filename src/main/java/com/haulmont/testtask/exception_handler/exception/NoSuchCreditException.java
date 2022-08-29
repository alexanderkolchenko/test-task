package com.haulmont.testtask.exception_handler.exception;

import com.haulmont.testtask.exception_handler.NoSuchEntityException;

public class NoSuchCreditException extends NoSuchEntityException {
    public NoSuchCreditException(String message) {
        super(message);
    }
}
