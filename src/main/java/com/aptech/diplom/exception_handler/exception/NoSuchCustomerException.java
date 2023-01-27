package com.aptech.diplom.exception_handler.exception;

import com.aptech.diplom.exception_handler.NoSuchEntityException;

public class NoSuchCustomerException extends NoSuchEntityException {
    public NoSuchCustomerException(String message) {
        super(message);
    }
}
