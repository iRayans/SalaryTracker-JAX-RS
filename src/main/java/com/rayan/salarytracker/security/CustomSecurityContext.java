package com.rayan.salarytracker.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.security.Principal;

import com.rayan.salarytracker.model.User;

@RequestScoped
@NoArgsConstructor
@AllArgsConstructor
public class CustomSecurityContext implements SecurityContext {

    // User is injected by the constructor
    private User user;

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return role.equals(user.getRole().name());
    }

    @Override
    public boolean isSecure() {
        // http and not https is used
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return "Bearer";
    }
}