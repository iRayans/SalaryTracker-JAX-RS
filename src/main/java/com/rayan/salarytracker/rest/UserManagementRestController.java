package com.rayan.salarytracker.rest;

import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.service.impl.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/users")
public class UserManagementRestController {

    private UserService userService;

    public UserManagementRestController() {
    }

    @Inject
    public UserManagementRestController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") Long id) {
        UserReadOnlyDTO userReadOnlyDTO = userService.getUserById(id);
        return Response.status(Response.Status.OK).entity(userReadOnlyDTO).build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<UserReadOnlyDTO> users = userService.getAllUsers();
        return Response.status(Response.Status.OK).entity(users).build();
    }

    @DELETE
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Long id) throws EntityNotFoundException {
        UserReadOnlyDTO userReadOnlyDTO = userService.getUserById(id);
        userService.deleteUser(id);
        return Response.status(Response.Status.OK).entity(userReadOnlyDTO).build();
    }

}
