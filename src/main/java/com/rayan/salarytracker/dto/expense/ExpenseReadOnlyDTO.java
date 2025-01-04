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
    private String amount;
    private String budgetRule;
    private String bank;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
