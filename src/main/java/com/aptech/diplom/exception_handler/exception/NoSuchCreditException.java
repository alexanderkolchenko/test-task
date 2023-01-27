package com.aptech.diplom.exception_handler.exception;

import com.aptech.diplom.exception_handler.NoSuchEntityException;

public class NoSuchCreditException extends NoSuchEntityException {
    public NoSuchCreditException(String message) {
        super(message);
    }
}
