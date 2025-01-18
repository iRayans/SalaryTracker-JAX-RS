package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.core.enums.ExpenseAction;
import com.rayan.salarytracker.model.Summary;

import java.util.Optional;

public interface ISummaryDAO extends IGenericCRUD<Summary> {

    Optional<Summary> getSummaryBySalaryId(Long salaryId);

    boolean updateSummary(int expenseAmount, Long salaryId, ExpenseAction action);

}
