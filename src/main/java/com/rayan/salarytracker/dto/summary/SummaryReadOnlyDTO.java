package com.rayan.salarytracker.dto.summary;

import com.rayan.salarytracker.model.Salary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SummaryReadOnlyDTO {
    private Long id;
    private int totExpenses;
    private int remainSalary;
    private Salary salary;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

