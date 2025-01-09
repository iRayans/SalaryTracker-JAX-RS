package com.rayan.salarytracker.dto.summary;

import com.rayan.salarytracker.model.Salary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SummaryInsertDTO {
    private int totalExpense;
    private int remainingSalary;
    private Salary salary;
}
