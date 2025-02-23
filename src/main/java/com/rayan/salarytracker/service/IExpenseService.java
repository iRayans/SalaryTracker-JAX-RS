package com.rayan.salarytracker.service;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.expense.ExpenseInsertDTO;
import com.rayan.salarytracker.dto.expense.ExpenseReadOnlyDTO;
import com.rayan.salarytracker.model.Expense;

import java.util.List;

public interface IExpenseService {
    List<ExpenseReadOnlyDTO> getAllExpenses(Long salaryId, Long userId);

    ExpenseReadOnlyDTO insertExpense(Long salaryId, ExpenseInsertDTO expenseInsertDTO) throws AppServerException, EntityNotFoundException;

    ExpenseReadOnlyDTO updateExpense(Long expenseId, Expense expense) throws AppServerException, EntityNotFoundException;

    void deleteExpense(Long expenseId) throws AppServerException, EntityNotFoundException;

    public boolean salaryHasExpense(Long salaryId);
}
