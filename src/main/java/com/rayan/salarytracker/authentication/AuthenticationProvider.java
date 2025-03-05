package com.rayan.salarytracker.authentication;

import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.user.UserLoginDTO;
import com.rayan.salarytracker.service.IUserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthenticationProvider {

    @Inject
    private IUserService userService;

    public boolean authenticate(UserLoginDTO userLoginDTO) throws EntityNotFoundException {
        return userService.isUserValid(userLoginDTO.getEmail(), userLoginDTO.getPassword());
    }
}