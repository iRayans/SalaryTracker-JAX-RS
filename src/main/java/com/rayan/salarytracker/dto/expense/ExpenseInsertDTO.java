package com.rayan.salarytracker.dto.expense;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseInsertDTO {
    @NotBlank(message = "Description must not be empty")
    private String description;

    @PositiveOrZero(message = "Value must be positive or zero")
    private int amount;

    @NotEmpty(message = "Budget Role must not be empty")
    private String budgetRole;

    private String bank;
    private boolean status;
}
