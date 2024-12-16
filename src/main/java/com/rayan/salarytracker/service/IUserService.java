package com.rayan.salarytracker.service;

import java.util.List;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;

import jakarta.persistence.EntityNotFoundException;

public interface IUserService {
    
    UserReadOnlyDTO insertUser(UserInsertDTO userInsertDTO) throws AppServerException;

    void deleteUser(Long id) throws EntityNotFoundException;

    UserReadOnlyDTO getUserById(Long id) throws EntityNotFoundException;

    List<UserReadOnlyDTO> getAllUsers();

    UserReadOnlyDTO findUserByEmail(String email) throws EntityNotFoundException;

    boolean isUserValid(String email, String plainPassword);

    boolean isEmailExists(String email);
}
