package com.rayan.salarytracker.dao;

import java.util.Optional;

import com.rayan.salarytracker.model.User;

public interface IUserDAO extends IGenericCRUD<User> {
    
    Optional<User> findUserByUserEmail(String email);

    boolean isUserValid(String email, String plainPassword);

    boolean isEmailExists(String email);
}
