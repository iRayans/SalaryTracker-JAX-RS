package com.rayan.salarytracker.rest;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.dto.salary.SalaryInsertDTO;
import com.rayan.salarytracker.dto.salary.SalaryReadOnlyDTO;
import com.rayan.salarytracker.model.Salary;
import com.rayan.salarytracker.model.User;
import com.rayan.salarytracker.service.impl.SalaryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

@Path("/salaries")
@ApplicationScoped
public class SalaryRestController {
    @Context
    SecurityContext securityContext;

    SalaryService salaryService;

    @Inject
    public SalaryRestController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    public SalaryRestController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertSalary(SalaryInsertDTO salaryInsertDTO) throws AppServerException {
        User user = getLoggedinUser();
        salaryInsertDTO.setUser(user);

        SalaryReadOnlyDTO salary = salaryService.insertSalary(salaryInsertDTO);
        return Response.status(Response.Status.CREATED).entity(salary).build();
    }

    // TODO: use one method for insert/update a salary.
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response updateSalary(@PathParam("id") Long salaryId, Salary salary) throws AppServerException {
        // Handle null exception...
        User user = getLoggedinUser();
        salary.setUser(user);

        SalaryReadOnlyDTO salaryReadOnlyDTO = salaryService.updateSalary(salaryId, salary);
        return Response.status(Response.Status.CREATED).entity(salaryReadOnlyDTO).build();
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
