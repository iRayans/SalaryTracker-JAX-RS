package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Expense;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExpenseDAO extends GenericCRUDImpl<Expense> {
    public ExpenseDAO() {
        this.setPersistenceClass(Expense.class);
    }
}
