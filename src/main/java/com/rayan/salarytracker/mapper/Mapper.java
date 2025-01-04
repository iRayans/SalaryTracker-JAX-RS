package com.rayan.salarytracker.mapper;

import com.rayan.salarytracker.core.enums.BudgetRuleAllocation;
import com.rayan.salarytracker.core.enums.RoleType;
import com.rayan.salarytracker.dto.expense.ExpenseInsertDTO;
import com.rayan.salarytracker.dto.expense.ExpenseReadOnlyDTO;
import com.rayan.salarytracker.dto.salary.SalaryInsertDTO;
import com.rayan.salarytracker.dto.salary.SalaryReadOnlyDTO;
import com.rayan.salarytracker.dto.user.UserInsertDTO;
import com.rayan.salarytracker.dto.user.UserReadOnlyDTO;
import com.rayan.salarytracker.model.Expense;
import com.rayan.salarytracker.model.Salary;
import com.rayan.salarytracker.model.User;
import com.rayan.salarytracker.security.PasswordUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ApplicationScoped
public class Mapper {

    // ======================================
    // =          User Mapper               =
    // ======================================

    public User mapToUser(UserInsertDTO userInsertDTO) {
        return new User(null, userInsertDTO.getUsername(), userInsertDTO.getEmail(), PasswordUtil.encryptPassword(userInsertDTO.getPassword()), RoleType.valueOf(userInsertDTO.getRole()), null, null);
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name(), user.getCreatedAt(), user.getUpdatedAt());
    }

    // ======================================
    // =          Salary Mapper             =
    // ======================================

    public Salary mapToSalary(SalaryInsertDTO salaryInsertDTO) {
        return new Salary(null, salaryInsertDTO.getMonth(), salaryInsertDTO.getDescription(), salaryInsertDTO.getAmount(), null, null, salaryInsertDTO.getUser());
    }

    public SalaryReadOnlyDTO mapToSalaryReadOnlyDTO(Salary salary) {
        return new SalaryReadOnlyDTO(salary.getId(), salary.getMonth(), salary.getDescription(), salary.getAmount(), salary.getCreatedAt(), salary.getUpdatedAt());
    }


    // ======================================
    // =          Expense Mapper            =
    // ======================================
    public Expense mapToExpense(ExpenseInsertDTO expenseInsertDTO) {
        return new Expense(
                null,
                expenseInsertDTO.getDescription(),
                expenseInsertDTO.getAmount(),
                BudgetRuleAllocation.valueOf(expenseInsertDTO.getBudgetRuleAllocation()),
                expenseInsertDTO.getBank(),
                expenseInsertDTO.isStatus(),
                null,
                null,
                expenseInsertDTO.getSalary()
        );
    }

    public ExpenseReadOnlyDTO mapToExpenseReadOnlyDTO(Expense expense) {
        return new ExpenseReadOnlyDTO(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getBudgetRuleAllocation().name(),
                expense.isStatus(),
                expense.getBank(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }

}
