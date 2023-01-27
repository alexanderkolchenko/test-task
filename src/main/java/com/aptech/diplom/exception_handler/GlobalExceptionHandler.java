package com.aptech.diplom.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler<T extends NoSuchEntityException> {

    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(T e) {
        IncorrectData data = new IncorrectData();
        data.setInfo(e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }
}
