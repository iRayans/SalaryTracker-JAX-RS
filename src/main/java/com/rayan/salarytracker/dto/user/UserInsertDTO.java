package com.rayan.salarytracker.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UserInsertDTO {

    private String name;
    
    @Email(message = "Please enter a valid email")
    private String email;

    // Validates password: at least 8 characters, includes lowercase, uppercase,
    // digit, and special character (!@#%&*).
    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[!@#%&*]).{8,}$", message = "Invalid password.")
    private String password;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\\d)(?=.*?[!@#%&*]).{8,}$", message = "Invalid password.")
    private String confirmPassword;
}
