package com.rayan.salarytracker.core.util.validation;


import com.rayan.salarytracker.core.exception.EntityAlreadyExistsException;
import com.rayan.salarytracker.core.exception.EntityInvalidArgumentsException;
import com.rayan.salarytracker.dao.IUserDAO;
import com.rayan.salarytracker.dao.impl.UserDAO;
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


    public static void validateDTO(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentsException {
        // Logical Validation
        if (userService.isEmailExists(userInsertDTO.getEmail())) {
            throw new EntityAlreadyExistsException("User ", "User with Email: " + userInsertDTO.getEmail() + " is Already exist");
        } else if (!userInsertDTO.getPassword().equals(userInsertDTO.getConfirmPassword())) {
            throw new EntityInvalidArgumentsException("User ", "Password and confirm Password do not match!");
        }
    }
}
