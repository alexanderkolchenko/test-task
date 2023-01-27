package com.aptech.diplom.exception_handler.exception;

import com.aptech.diplom.exception_handler.NoSuchEntityException;

public class NoSuchCreditOfferException extends NoSuchEntityException {
    public NoSuchCreditOfferException(String message) {
        super(message);
    }
}
