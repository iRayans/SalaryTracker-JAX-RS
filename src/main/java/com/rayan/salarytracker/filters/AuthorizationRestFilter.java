package com.rayan.salarytracker.filters;

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;


@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationRestFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("AuthorizationRestFilter");
        UriInfo uriInfo = requestContext.getUriInfo();
        SecurityContext securityContext = requestContext.getSecurityContext();
    }
    
}
