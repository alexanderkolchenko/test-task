package com.haulmont.testtask.exception_handler.exception;

import com.haulmont.testtask.exception_handler.NoSuchEntityException;

public class NoSuchBankException extends NoSuchEntityException {
    public NoSuchBankException(String message) {
        super(message);
    }
}
