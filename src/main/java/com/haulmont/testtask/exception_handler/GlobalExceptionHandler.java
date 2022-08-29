package com.haulmont.testtask.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BankIncorrectData> handleException(NoSuchBankException e) {
        BankIncorrectData data = new BankIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CreditIncorrectData> handleException(NoSuchCreditException e) {
        CreditIncorrectData data = new CreditIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CreditOfferIncorrectData> handleException(NoSuchCreditOfferException e) {
        CreditOfferIncorrectData data = new CreditOfferIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomerIncorrectData> handleException(NoSuchCustomerException e) {
        CustomerIncorrectData data = new CustomerIncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
