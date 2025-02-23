package com.rayan.salarytracker.core.util.validation;


import com.rayan.salarytracker.core.exception.EntityAlreadyExistsException;
import com.rayan.salarytracker.core.exception.EntityInvalidArgumentsException;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.service.IUserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class ValidatorUtil {

    private static final Logger LOGGER = LogManager.getLogger(ValidatorUtil.class.getName());

    @Inject
    IUserService userService;

    public void validateDTO(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentsException {
        if (userService.isEmailExists(userInsertDTO.getEmail())) {
            LOGGER.error("Email {} already exists", userInsertDTO.getEmail());
            throw new EntityAlreadyExistsException("User ", "User with Email: " + userInsertDTO.getEmail() + " is Already exist");
        } else if (!userInsertDTO.getPassword().equals(userInsertDTO.getConfirmPassword())) {
            LOGGER.error("Password does not match");
            throw new EntityInvalidArgumentsException("User ", "Password and confirm Password do not match!");
        }
    }
}
