package com.rayan.salarytracker.dto.salary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalaryReadOnlyDTO {

    private Long id;
    private String month;
    private String description;
    private int amount;
    private int year;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
