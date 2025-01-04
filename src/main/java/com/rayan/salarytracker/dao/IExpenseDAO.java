package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Expense;

import java.util.List;

public interface IExpenseDAO extends IGenericCRUD<Expense> {

    List<Expense> getExpensesBySalaryId(Long salaryId);
}
