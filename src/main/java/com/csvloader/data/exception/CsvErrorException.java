package com.csvloader.data.exception;

public class CsvErrorException extends RuntimeException{
    public CsvErrorException(String message) {
        super(message);
    }
}
