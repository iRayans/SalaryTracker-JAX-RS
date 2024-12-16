package com.rayan.salarytracker.core.exception;

public class EntityAlreadyExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EntityAlreadyExistsException(String message){
        super(message);
    }
}
