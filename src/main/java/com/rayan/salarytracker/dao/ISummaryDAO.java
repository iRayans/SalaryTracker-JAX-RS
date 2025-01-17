package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Summary;

import java.util.Optional;

public interface ISummaryDAO extends IGenericCRUD<Summary> {

    Optional<Summary> getSummaryBySalaryId(Long salaryId);

    boolean calcSummary(int expenseAmount, Long salaryId);
}
