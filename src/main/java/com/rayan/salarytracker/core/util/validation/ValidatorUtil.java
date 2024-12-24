package com.rayan.salarytracker.core.util.validation;


import com.rayan.salarytracker.core.exception.EntityAlreadyExistsException;
import com.rayan.salarytracker.core.exception.PasswordsDoNotMatchException;
import com.rayan.salarytracker.dao.IUserDAO;
import com.rayan.salarytracker.dao.UserDAO;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.mapper.Mapper;
import com.rayan.salarytracker.service.IUserService;
import com.rayan.salarytracker.service.impl.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ValidatorUtil {

    private static final Logger LOGGER = LogManager.getLogger(ValidatorUtil.class.getName());


    private static final IUserDAO userDAO = new UserDAO();
    private static final Mapper mapper = new Mapper();
    private static final IUserService userService = new UserService(userDAO, mapper);


    public static void validateDTO(UserInsertDTO userInsertDTO) {
        // Logical Validation
        if (userService.isEmailExists(userInsertDTO.getEmail())) {
            throw new EntityAlreadyExistsException(
                    "User with Email: " + userInsertDTO.getEmail() + " is Already exist");
        } else if (!userInsertDTO.getPassword().equals(userInsertDTO.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException(
                    "Password do not match!");
        }

    }
}
