package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Salary;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SalaryDAO extends GenericCRUDImpl<Salary> implements ISalaryDAO {
    @Override
    public Optional<Salary> findByMonth(String month) {
        return Optional.empty();
    }
}
