package com.rayan.salarytracker.core.exception;

public class GenericException extends Exception{
    private String code;

    public GenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
