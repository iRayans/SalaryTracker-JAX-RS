package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.IUserDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.mapper.Mapper;
import com.rayan.salarytracker.model.User;
import com.rayan.salarytracker.service.IUserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ApplicationScoped
public class UserService implements IUserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class.getName());

    @Inject
    private IUserDAO userDAO;
    @Inject
    private Mapper mapper;

    @Override
    public UserReadOnlyDTO insertUser(UserInsertDTO userInsertDTO) throws AppServerException {
        try {
            JPAHelperUtil.beginTransaction();
            if (userInsertDTO.getRole() == null) {
                userInsertDTO.setRole("USER");
            }
            User user = mapper.mapToUser(userInsertDTO);

            UserReadOnlyDTO userReadOnlyDTO = userDAO.insert(user)
                    .map(mapper::mapToUserReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("User ", "User with email: " + userInsertDTO.getEmail() + "not inserted."));
            JPAHelperUtil.commitTransaction();

            LOGGER.info("User with Email {} inserted.", userInsertDTO.getEmail());
            return userReadOnlyDTO;
        } catch (Exception e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("User with Email: {} not inserted.", userInsertDTO.getEmail());
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();
            getUserById(id);
            userDAO.delete(id);
            JPAHelperUtil.commitTransaction();
            LOGGER.info("User with id: {} was deleted.", id);

        } catch (Exception e) {
            LOGGER.error("User with id {} was not deleted.", id);
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
                    .orElseThrow(() -> new EntityNotFoundException("User", "User with id " + id + " was not found."));
            JPAHelperUtil.commitTransaction();
            LOGGER.info("User with id {} was found.", id);

            return userReadOnlyDTO;
        } catch (Exception e) {
            LOGGER.error("User with id {} was not found.", id);
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
                    .orElseThrow(() -> new EntityNotFoundException("User", " with email " + email + " was not found."));
            JPAHelperUtil.commitTransaction();
            return userReadOnlyDTO;
        } catch (Exception e) {
            LOGGER.warn("User with email: {} not found.", email);
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
