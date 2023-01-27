package com.aptech.diplom.exception_handler.exception;

import com.aptech.diplom.exception_handler.NoSuchEntityException;

public class NoSuchBankException extends NoSuchEntityException {
    public NoSuchBankException(String message) {
        super(message);
    }
}
