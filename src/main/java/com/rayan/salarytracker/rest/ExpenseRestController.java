package com.rayan.salarytracker.rest;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.expense.ExpenseInsertDTO;
import com.rayan.salarytracker.dto.expense.ExpenseReadOnlyDTO;
import com.rayan.salarytracker.dto.summary.SummaryReadOnlyDTO;
import com.rayan.salarytracker.model.Expense;
import com.rayan.salarytracker.service.impl.ExpenseService;
import com.rayan.salarytracker.service.impl.SummaryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/expenses")
@ApplicationScoped
public class ExpenseRestController {

    private ExpenseService expenseService;
    private SummaryService summaryService;

    @Inject
    public ExpenseRestController(ExpenseService expenseService, SummaryService summaryService) {
        this.expenseService = expenseService;
        this.summaryService = summaryService;
    }

    public ExpenseRestController() {
    }

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
        List<ExpenseReadOnlyDTO> expenseReadOnlyDTOList = expenseService.getAllExpenses(salaryId);
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
}
