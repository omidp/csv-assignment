package com.csvloader.data.controller;

import com.csvloader.data.exception.CsvErrorException;
import com.csvloader.data.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CsvExceptionController {

    @ExceptionHandler(value = {CsvErrorException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleCsvException(CsvErrorException ex) {
        return new ErrorMessage(0, ex.getMessage());
    }

}
