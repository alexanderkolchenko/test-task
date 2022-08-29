package com.haulmont.testtask.exception_handler;

public class NoSuchCreditOfferException extends RuntimeException{
    public NoSuchCreditOfferException(String message) {
        super(message);
    }
}
