package com.rayan.salarytracker.service;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.expense.ExpenseInsertDTO;
import com.rayan.salarytracker.dto.expense.ExpenseReadOnlyDTO;

import java.util.List;

public interface IExpenseService {
    List<ExpenseReadOnlyDTO> getAllExpenses();

    ExpenseReadOnlyDTO insertExpense(Long salaryId, ExpenseInsertDTO expenseInsertDTO) throws AppServerException, EntityNotFoundException;
}
