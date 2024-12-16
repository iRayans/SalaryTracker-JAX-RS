package com.rayan.salarytracker.core.exception;

import com.rayan.salarytracker.model.ErrorMessage;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        System.out.println("EntityNotFoundException++");
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 404);
        return Response.status(Status.NOT_FOUND)
                .entity(errorMessage)
                .build();
    }

}
