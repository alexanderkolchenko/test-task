package com.aptech.diplom.exception_handler.exception;

import com.aptech.diplom.exception_handler.NoSuchEntityException;

public class NoSuchUserException extends NoSuchEntityException {
    public NoSuchUserException(String message) {
        super(message);
    }
}

