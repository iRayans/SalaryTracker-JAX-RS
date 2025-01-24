package com.rayan.salarytracker.authentication;

import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponseDTO {

    private String token;
    private UserReadOnlyDTO user;
}
