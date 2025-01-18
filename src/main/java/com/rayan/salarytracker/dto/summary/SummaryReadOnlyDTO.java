package com.rayan.salarytracker.dto.summary;

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
    private String month;
    private int salaryAmount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

