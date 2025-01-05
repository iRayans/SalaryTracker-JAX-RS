package com.rayan.salarytracker.dto.expense;

import com.rayan.salarytracker.model.Salary;
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
    private String budgetRuleAllocation;
    private String bank;
    private Boolean status = false;
    private Salary salary;
}
