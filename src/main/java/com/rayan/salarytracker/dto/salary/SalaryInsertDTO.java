package com.rayan.salarytracker.dto.salary;

import com.rayan.salarytracker.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalaryInsertDTO {

    @NotBlank(message = "Month must not be empty")
    private String month;

    @NotBlank(message = "Description must not be empty")
    private String description;

    @PositiveOrZero(message = "Value must be positive or zero")
    private int amount;

    private int year;
    private User user;
}
