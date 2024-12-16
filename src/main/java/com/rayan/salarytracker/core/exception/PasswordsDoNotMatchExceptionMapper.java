package com.rayan.salarytracker.core.exception;

import com.rayan.salarytracker.model.ErrorMessage;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PasswordsDoNotMatchExceptionMapper implements ExceptionMapper<PasswordsDoNotMatchException> {

    @Override
    public Response toResponse(PasswordsDoNotMatchException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 400); 
        return Response.status(Status.BAD_REQUEST)
                .entity(errorMessage)
                .build();
    }
}