package com.rayan.salarytracker.security;

import java.security.Principal;

import com.rayan.salarytracker.model.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@RequestScoped
@NoArgsConstructor
@AllArgsConstructor
public class CustomSecurityContext implements SecurityContext {

    private User user;

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        // Role-based checks not implemented
        return false;
    }

    @Override
    public boolean isSecure() {
        return false; // Assume HTTPS is used
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}
