package com.rayan.salarytracker.filters;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Set;

import com.rayan.salarytracker.dao.IUserDAO;
import com.rayan.salarytracker.model.User;
import com.rayan.salarytracker.security.CustomSecurityContext;
import com.rayan.salarytracker.security.JWTService;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private final static Set<String> PUBLIC_PATHS = Set.of(
            "/auth/register",
            "/auth/login");

    private IUserDAO userDAO;
    private JWTService jwtService;

    public JwtAuthenticationFilter() {
    }

    public JwtAuthenticationFilter(IUserDAO userDAO, JWTService jwtService) {
        this.userDAO = userDAO;
        this.jwtService = jwtService;
    }

    @Context
    private SecurityContext securityContext;

    /**
     * Checks the validity of JWT for every request that does not belong to the
     * "Public paths".
     * If there is no SecurityContext associated with current user, it creates one.
     */

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        UriInfo uriInfo = containerRequestContext.getUriInfo();

        // Check if the path is public
        if (isPublicPath(uriInfo.getPath())) {
            return;
        }

        // Extract the Authorization header
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new NotAuthorizedException("Authorization Header does not exist, or is invalid.");
        }

        // Extract token
        String token = authorizationHeader.substring("Bearer ".length()).trim();

        try {
            // Validate and extract subject (email)
            String email = jwtService.extractSubject(token);

            if (email != null) {
                User user = userDAO.findUserByUserEmail(email).orElse(null);

                // Validate token and set SecurityContext if valid
                if (user != null && jwtService.isTokenValid(token, user)) {
                    containerRequestContext.setSecurityContext(new CustomSecurityContext(user));
                } else {
                    throw new NotAuthorizedException("Invalid token.");
                }
            }
        } catch (Exception e) {
            throw new NotAuthorizedException("Invalid token.");
        }
    }

    // Checks if a path belongs to public paths
    private boolean isPublicPath(String path) {
        System.out.println("Path: " + path);
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}
