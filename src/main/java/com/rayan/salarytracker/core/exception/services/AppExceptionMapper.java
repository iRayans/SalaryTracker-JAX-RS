package com.rayan.salarytracker.core.exception.services;

import com.rayan.salarytracker.core.exception.*;
import com.rayan.salarytracker.dto.ErrorMessageDTO;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/*
 * The ExceptionMapper<GenericException> is a JAX-RS
 * construct that maps exceptions of type GenericException (and its subclasses) into an HTTP response.
 */
@Provider
public class AppExceptionMapper implements ExceptionMapper<GenericException> {
    @Override
    public Response toResponse(GenericException exception) {
        // Default status
        Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof EntityInvalidArgumentsException) {
            status = Response.Status.BAD_REQUEST;
        } else if (exception instanceof EntityAlreadyExistsException) {
            status = Response.Status.CONFLICT;
        } else if (exception instanceof EntityNotFoundException) {
            status = Response.Status.NOT_FOUND;
        } else if (exception instanceof AppServerException) {
            status = Response.Status.SERVICE_UNAVAILABLE;
        }
        return Response
                .status(status)
                .entity(new ErrorMessageDTO(exception.getCode(), exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
