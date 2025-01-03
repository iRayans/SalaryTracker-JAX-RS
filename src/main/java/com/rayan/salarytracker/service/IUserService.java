package com.rayan.salarytracker.service;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;

import java.util.List;


public interface IUserService {

    UserReadOnlyDTO insertUser(UserInsertDTO userInsertDTO) throws AppServerException;

    void deleteUser(Long id) throws EntityNotFoundException;

    UserReadOnlyDTO getUserById(Long id) throws EntityNotFoundException;

    List<UserReadOnlyDTO> getAllUsers();

    UserReadOnlyDTO findUserByEmail(String email) throws EntityNotFoundException;

    boolean isUserValid(String email, String plainPassword) throws EntityNotFoundException;

    boolean isEmailExists(String email);
}
