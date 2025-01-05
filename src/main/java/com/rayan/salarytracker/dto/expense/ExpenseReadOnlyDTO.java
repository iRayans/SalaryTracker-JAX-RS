package com.rayan.salarytracker.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseReadOnlyDTO {
    private Long id;
    private String description;
    private int amount;
    private String budgetRule;
    private Boolean status;
    private String bank;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
