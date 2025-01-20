package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Salary;

import java.util.List;

public interface ISalaryDAO extends IGenericCRUD<Salary> {
    List<Salary> findSalaryByUserId(Long userId);

    boolean existsByUserIdAndMonthAndYear(Long userId, String month, int year);
}
