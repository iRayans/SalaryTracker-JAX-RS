package com.rayan.salarytracker.service;

import com.rayan.salarytracker.core.enums.ExpenseAction;
import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.summary.SummaryReadOnlyDTO;
import com.rayan.salarytracker.model.Salary;

public interface ISummaryService {

    SummaryReadOnlyDTO getSummary(Long salaryId) throws EntityNotFoundException;

    void initializeSummary(Salary salary) throws EntityNotFoundException, AppServerException;

    void updateSummary(int expenseAmount, Long salaryId, ExpenseAction action) throws AppServerException;
}
