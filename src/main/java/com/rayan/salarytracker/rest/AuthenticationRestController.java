package com.rayan.salarytracker.rest;

import java.security.Principal;

import com.rayan.salarytracker.authentication.AuthenticationProvider;
import com.rayan.salarytracker.authentication.AuthenticationResponseDTO;
import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityInvalidArgumentsException;
import com.rayan.salarytracker.core.util.validation.ValidatorUtil;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserLoginDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.security.JWTService;
import com.rayan.salarytracker.service.UserService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@ApplicationScoped
public class AuthenticationRestController {

    private UserService userService;
    private AuthenticationProvider authenticationProvider;
    private JWTService jwtService;

    @Inject
    public AuthenticationRestController(UserService userService, AuthenticationProvider authenticationProvider,
            JWTService jwtService) {
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
    }

    public AuthenticationRestController() {
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserInsertDTO userInsertDTO)
            throws AppServerException, EntityInvalidArgumentsException {
        // Validation
        ValidatorUtil.validateDTO(userInsertDTO);

        UserReadOnlyDTO userReadOnlyDTO = userService.insertUser(userInsertDTO);
        return Response.status(Response.Status.CREATED)
                .entity(userReadOnlyDTO)
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserLoginDTO userLoginDTO, @Context Principal principal) {
        System.out.println(userLoginDTO.getEmail() + "   " + userLoginDTO.getPassword());
        // Authentication
        boolean isAuthenticated = authenticationProvider.authenticate(userLoginDTO);
        if (!isAuthenticated) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // If principal != null then user has already logged in.
        if (principal != null) {
            String email = principal.getName();
            if (userLoginDTO.getEmail().equals(email)) {
                return Response.status(Response.Status.OK).entity("Already authenticated").build();
            }
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.findUserByEmail(userLoginDTO.getEmail());
        String username = userReadOnlyDTO.getName();

        // Create a JWT token for current user
        String token = jwtService.generateToken(userLoginDTO.getEmail(), username);
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(token);

        return Response.status(Response.Status.OK).entity(authenticationResponseDTO).build();
    }
}
