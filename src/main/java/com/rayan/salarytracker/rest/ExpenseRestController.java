package com.rayan.salarytracker.rest;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.expense.ExpenseInsertDTO;
import com.rayan.salarytracker.dto.expense.ExpenseReadOnlyDTO;
import com.rayan.salarytracker.service.impl.ExpenseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/expenses")
@ApplicationScoped
public class ExpenseRestController {

    private ExpenseService expenseService;

    @Inject
    public ExpenseRestController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public ExpenseRestController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response insertExpense(@PathParam("id") Long salaryId, ExpenseInsertDTO expenseInsertDTO) throws AppServerException, EntityNotFoundException {
        ExpenseReadOnlyDTO expenseReadOnlyDTO = expenseService.insertExpense(salaryId, expenseInsertDTO);
        return Response.status(Response.Status.CREATED).entity(expenseReadOnlyDTO).build();
    }
}
