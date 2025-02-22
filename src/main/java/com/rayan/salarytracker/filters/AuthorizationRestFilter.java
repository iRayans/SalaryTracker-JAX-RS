package com.rayan.salarytracker.filters;


import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationRestFilter implements ContainerRequestFilter {

    // Paths of the form /users/**
    private static final Set<String> adminOnlyPaths = Set.of("users");

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        UriInfo uriInfo = containerRequestContext.getUriInfo();
        SecurityContext securityContext = containerRequestContext.getSecurityContext();
        /* Use containerRequestContext.getSecurityContext() to get the updated SecurityContext
         * set by JwtAuthenticationFilter. @Context SecurityContext would not have these updates.*/
        if (isAdminOnlyPath(uriInfo.getPath()) && !securityContext.isUserInRole("ADMIN")) {
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Restricted Area\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .entity(Collections.singletonMap("error", "Access Denied: You do not have permission to access this resource."))
                    .build());
        }
    }

    private boolean isAdminOnlyPath(String path) {
        return adminOnlyPaths.stream().anyMatch(path::contains);
    }
}