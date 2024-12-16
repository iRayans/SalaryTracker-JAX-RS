package com.rayan.salarytracker.core.exception;

import com.rayan.salarytracker.model.ErrorMessage;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntityAlreadyExistsExceptionMapper implements ExceptionMapper<EntityAlreadyExistsException> {

    @Override
    public Response toResponse(EntityAlreadyExistsException exception) {
       ErrorMessage entityAlreadyExist = new ErrorMessage(exception.getMessage(),409);
       return Response.status(Status.CONFLICT)
       .entity(entityAlreadyExist)
       .build();
    }
    
}
