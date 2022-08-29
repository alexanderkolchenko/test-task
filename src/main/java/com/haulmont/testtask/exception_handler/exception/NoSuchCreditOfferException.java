package com.haulmont.testtask.exception_handler.exception;

import com.haulmont.testtask.exception_handler.NoSuchEntityException;

public class NoSuchCreditOfferException extends NoSuchEntityException {
    public NoSuchCreditOfferException(String message) {
        super(message);
    }
}
