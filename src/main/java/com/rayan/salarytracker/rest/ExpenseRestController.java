package com.rayan.salarytracker.rest;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.expense.ExpenseInsertDTO;
import com.rayan.salarytracker.dto.expense.ExpenseReadOnlyDTO;
import com.rayan.salarytracker.dto.summary.SummaryReadOnlyDTO;
import com.rayan.salarytracker.model.Expense;
import com.rayan.salarytracker.model.User;
import com.rayan.salarytracker.service.impl.ExpenseService;
import com.rayan.salarytracker.service.impl.SummaryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.List;

@Path("/expenses")
public class ExpenseRestController {

    @Inject
    private ExpenseService expenseService;
    @Inject
    private SummaryService summaryService;
    @Context
    private SecurityContext securityContext;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response insertExpense(@PathParam("id") Long salaryId, ExpenseInsertDTO expenseInsertDTO) throws AppServerException, EntityNotFoundException {
        System.out.println("salaryId: " + salaryId);
        ExpenseReadOnlyDTO expenseReadOnlyDTO = expenseService.insertExpense(salaryId, expenseInsertDTO);
        return Response.status(Response.Status.CREATED).entity(expenseReadOnlyDTO).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getAllExpenses(@PathParam("id") Long salaryId) {
        Long userId = getLoggedinUser().getId();
        List<ExpenseReadOnlyDTO> expenseReadOnlyDTOList = expenseService.getAllExpenses(salaryId, userId);
        return Response.status(Response.Status.OK).entity(expenseReadOnlyDTOList).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateExpense(@PathParam("id") Long expenseId, Expense expense) throws AppServerException, EntityNotFoundException {
        ExpenseReadOnlyDTO expenseReadOnlyDTO = expenseService.updateExpense(expenseId, expense);
        return Response.status(Response.Status.OK).entity(expenseReadOnlyDTO).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteExpense(@PathParam("id") Long expenseId) throws AppServerException, EntityNotFoundException {
        expenseService.deleteExpense(expenseId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/summary")
    public Response getSalarySummary(@PathParam("id") Long salaryId) throws EntityNotFoundException {
        SummaryReadOnlyDTO summaryReadOnlyDTOS = summaryService.getSummary(salaryId);
        return Response.status(Response.Status.OK).entity(summaryReadOnlyDTOS).build();
    }

    private User getLoggedinUser() {
        Principal principal = securityContext.getUserPrincipal();
        if (principal == null) {
//            LOGGER.error("No logged-in user found in SecurityContext.");
            throw new IllegalStateException("No logged-in user found.");
        }
        return (User) principal;
    }

}
