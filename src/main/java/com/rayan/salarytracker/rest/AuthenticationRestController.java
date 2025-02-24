package com.rayan.salarytracker.rest;

import com.rayan.salarytracker.authentication.AuthenticationProvider;
import com.rayan.salarytracker.authentication.AuthenticationResponseDTO;
import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityAlreadyExistsException;
import com.rayan.salarytracker.core.exception.EntityInvalidArgumentsException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.core.util.validation.ValidatorUtil;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserLoginDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.security.JWTService;
import com.rayan.salarytracker.service.IUserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/auth")
public class AuthenticationRestController {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationRestController.class.getName());

    private IUserService userService;
    private AuthenticationProvider authenticationProvider;
    private JWTService jwtService;
    private ValidatorUtil validatorUtil;

    @Inject
    public AuthenticationRestController(IUserService userService, AuthenticationProvider authenticationProvider,
                                        JWTService jwtService, ValidatorUtil validatorUtil) {
        this.userService = userService;
        this.authenticationProvider = authenticationProvider;
        this.jwtService = jwtService;
        this.validatorUtil = validatorUtil;
    }

    public AuthenticationRestController() {
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(UserInsertDTO userInsertDTO)
            throws AppServerException, EntityInvalidArgumentsException, EntityAlreadyExistsException {
        // Validation
        validatorUtil.validateDTO(userInsertDTO);

        UserReadOnlyDTO userReadOnlyDTO = userService.insertUser(userInsertDTO);
        return Response.status(Response.Status.CREATED)
                .entity(userReadOnlyDTO)
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(UserLoginDTO userLoginDTO) throws EntityNotFoundException, EntityInvalidArgumentsException {
        // Authentication
        boolean isAuthenticated = authenticationProvider.authenticate(userLoginDTO);
        if (!isAuthenticated) {
            LOGGER.error("email or password incorrect");
            throw new EntityInvalidArgumentsException("User", "email or password incorrect");
        }


        UserReadOnlyDTO userReadOnlyDTO = userService.findUserByEmail(userLoginDTO.getEmail());
        String username = userReadOnlyDTO.getName();
        String role = userReadOnlyDTO.getRole();
        // Create a JWT token for current user
        String token = jwtService.generateToken(userLoginDTO.getEmail(), username, role);
        UserReadOnlyDTO user = userService.findUserByEmail(userLoginDTO.getEmail());
        AuthenticationResponseDTO authenticationResponseDTO = new AuthenticationResponseDTO(token, user);

        LOGGER.info("Logged in user {} ", userLoginDTO.getEmail());
        return Response.status(Response.Status.OK).entity(authenticationResponseDTO).build();
    }
}
