package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Salary;

import java.util.Optional;

public interface ISalaryDAO extends IGenericCRUD<Salary> {
    Optional<Salary> findByMonth(String month);
}
