package com.rayan.salarytracker.service;

import java.util.List;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.IUserDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.mapper.Mapper;
import com.rayan.salarytracker.model.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService implements IUserService {

    private IUserDAO userDAO;
    private Mapper mapper;

    public UserService() {
    }

    @Inject
    public UserService(IUserDAO userDAO, Mapper mapper) {
        this.userDAO = userDAO;
        this.mapper = mapper;
    }

    @Override
    public UserReadOnlyDTO insertUser(UserInsertDTO userInsertDTO) throws AppServerException {

        try {
            JPAHelperUtil.beginTransaction();
            User user = mapper.mapToUser(userInsertDTO);

            UserReadOnlyDTO userReadOnlyDTO = userDAO.insert(user)
                    .map(mapper::mapToUserReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("User",
                            "User with email: " + userInsertDTO.getEmail() + "not inserted."));
            JPAHelperUtil.commitTransaction();
            return userReadOnlyDTO;
        } catch (AppServerException e) {
            JPAHelperUtil.rollbackTransaction();
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();
            userDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " Not found."));
            userDAO.delete(id);
            JPAHelperUtil.commitTransaction();
        } catch (Exception e) {
            JPAHelperUtil.rollbackTransaction();
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public UserReadOnlyDTO getUserById(Long id) throws EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();
            UserReadOnlyDTO userReadOnlyDTO = userDAO.getById(id)
                    .map(mapper::mapToUserReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found."));
            JPAHelperUtil.commitTransaction();
            return userReadOnlyDTO;
        } catch (Exception e) {
            JPAHelperUtil.rollbackTransaction();
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public List<UserReadOnlyDTO> getAllUsers() {
        JPAHelperUtil.beginTransaction();
        List<User> users = userDAO.getAll();
        JPAHelperUtil.commitTransaction();
        return users.stream().map(mapper::mapToUserReadOnlyDTO).toList();
    }
    @Override
    public UserReadOnlyDTO findUserByEmail(String email) throws EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();
            UserReadOnlyDTO userReadOnlyDTO = userDAO.findUserByUserEmail(email)
                    .map(mapper::mapToUserReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found."));
            JPAHelperUtil.commitTransaction();
            return userReadOnlyDTO;
        } catch (EntityNotFoundException e) {
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public boolean isUserValid(String email, String plainPassword) {
        try {
            JPAHelperUtil.beginTransaction();
            boolean isValid = userDAO.isUserValid(email, plainPassword);
            JPAHelperUtil.commitTransaction();
            return isValid;

        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        try {
            JPAHelperUtil.beginTransaction();
            boolean emailExists = userDAO.isEmailExists(email);
            JPAHelperUtil.commitTransaction();
            return emailExists;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }
}
