package com.rayan.salarytracker.security;

import java.security.Principal;

import com.rayan.salarytracker.model.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.security.Principal;


    @RequestScoped
    public class CustomSecurityContext implements SecurityContext {
    
        private final User user;
    
        public CustomSecurityContext(User user) {
            this.user = user;
        }
    
        @Override
        public Principal getUserPrincipal() {
            return () -> user.getEmail(); // Return email as Principal's name
        }
    
        @Override
        public boolean isUserInRole(String role) {
            // Role-based checks not implemented
            return false;
        }
    
        @Override
        public boolean isSecure() {
            return true; // Assume HTTPS is used
        }
    
        @Override
        public String getAuthenticationScheme() {
            return "Bearer";
        }
    }

