package com.rayan.salarytracker.core.exception;

public class EntityAlreadyExistsException extends GenericException {

    private static final String DEFAULT_CODE = "AlreadyExists";

    public EntityAlreadyExistsException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
