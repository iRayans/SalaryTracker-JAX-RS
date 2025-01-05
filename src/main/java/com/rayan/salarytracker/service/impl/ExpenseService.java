package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.ExpenseDAO;
import com.rayan.salarytracker.dao.SalaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.dto.expense.ExpenseInsertDTO;
import com.rayan.salarytracker.dto.expense.ExpenseReadOnlyDTO;
import com.rayan.salarytracker.mapper.Mapper;
import com.rayan.salarytracker.model.Expense;
import com.rayan.salarytracker.model.Salary;
import com.rayan.salarytracker.service.IExpenseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@ApplicationScoped
public class ExpenseService implements IExpenseService {
    private static final Logger LOGGER = LogManager.getLogger(ExpenseService.class.getName());

    private ExpenseDAO expenseDAO;
    private Mapper mapper;
    private SalaryDAO salaryDAO;

    @Inject
    public ExpenseService(ExpenseDAO expenseDAO, Mapper mapper, SalaryDAO salaryDAO) {
        this.expenseDAO = expenseDAO;
        this.mapper = mapper;
        this.salaryDAO = salaryDAO;
    }

    public List<ExpenseReadOnlyDTO> getAllExpenses(Long salaryId) {
        JPAHelperUtil.beginTransaction();
        List<Expense> expenses = expenseDAO.getExpensesBySalaryId(salaryId);
        JPAHelperUtil.commitTransaction();
        return expenses.stream().map(mapper::mapToExpenseReadOnlyDTO).toList();
    }

    public ExpenseService() {
    }

    @Override
    public ExpenseReadOnlyDTO insertExpense(Long salaryId, ExpenseInsertDTO expenseInsertDTO) throws AppServerException, EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();
            Salary salary = salaryDAO.getById(salaryId)
                    .orElseThrow(() -> new EntityNotFoundException("Salary", "Salary with id: " + salaryId + " not found."));
            Expense expense = mapper.mapToExpense(expenseInsertDTO);
            expense.setSalary(salary);
            ExpenseReadOnlyDTO expenseReadOnlyDTO = expenseDAO.insert(expense)
                    .map(mapper::mapToExpenseReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("Expense",
                            "Expense: " + expenseInsertDTO.getDescription() + "not inserted."));
            JPAHelperUtil.commitTransaction();
            LOGGER.info("Expense with Id:  {} inserted.", expenseReadOnlyDTO.getId());
            return expenseReadOnlyDTO;

        } catch (AppServerException | EntityNotFoundException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Expense: {} was not inserted ", expenseInsertDTO.getDescription());
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }

    }

    @Override
    public ExpenseReadOnlyDTO updateExpense(Long expenseId, Expense expense) throws AppServerException, EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();
            Expense existingExpense = expenseDAO.getById(expenseId)
                    .orElseThrow(() -> new EntityNotFoundException("Expense", "Expense: " + expenseId + " not found."));
            // Update only provided fields
            updateFields(existingExpense, expense);
            ExpenseReadOnlyDTO expenseReadOnlyDTO = expenseDAO.update(existingExpense)
                    .map(mapper::mapToExpenseReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("Expense", "Expense: " + expenseId + " not updates."));
            JPAHelperUtil.commitTransaction();
            LOGGER.info("Expense with Id:  {} updated.", expenseReadOnlyDTO.getId());
            return expenseReadOnlyDTO;
        } catch (AppServerException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Expense: {} was not updated ", expenseId, e);
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }


    private void updateFields(Expense existing, Expense updated) {
        existing.setAmount(updated.getAmount() > 0 ? updated.getAmount() : existing.getAmount());
        existing.setDescription(updated.getDescription() != null ? updated.getDescription() : existing.getDescription());
        existing.setBank(updated.getBank() != null ? updated.getBank() : existing.getBank());
        existing.setStatus(updated.getStatus() != null ? updated.getStatus() : existing.getStatus());
        existing.setBudgetRuleAllocation(updated.getBudgetRuleAllocation() != null ? updated.getBudgetRuleAllocation() : existing.getBudgetRuleAllocation());
    }

}
