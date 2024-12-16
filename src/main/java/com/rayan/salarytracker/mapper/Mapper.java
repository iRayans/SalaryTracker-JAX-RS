package com.rayan.salarytracker.mapper;

import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.model.User;
import com.rayan.salarytracker.security.PasswordUtil;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ApplicationScoped
public class Mapper {

    public User mapToUser(UserInsertDTO userInsertDTO) {
        return new User(null,userInsertDTO.getName(),userInsertDTO.getEmail(),PasswordUtil.encryptPassword(userInsertDTO.getPassword()), null, null);
    }

        public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getId(),user.getName(),user.getEmail(),user.getCreatedAt(),user.getUpdatedAt());
    }
}
