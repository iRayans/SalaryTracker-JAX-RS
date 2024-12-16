package com.rayan.salarytracker.rest;

import java.util.List;

import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.service.UserService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/users")
public class UserManagementRestController {

    private UserService userService;

    public UserManagementRestController() {}

    @Inject
    public UserManagementRestController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") Long id){
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
