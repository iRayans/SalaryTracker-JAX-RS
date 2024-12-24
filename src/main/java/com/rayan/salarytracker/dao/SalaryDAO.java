package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Salary;

import java.util.Optional;

public class SalaryDAO extends GenericCRUDImpl<Salary> implements ISalaryDAO {
    @Override
    public Optional<Salary> findByMonth(String month) {
        return Optional.empty();
    }
}
