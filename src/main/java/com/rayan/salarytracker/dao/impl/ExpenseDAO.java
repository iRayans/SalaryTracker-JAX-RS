package com.rayan.salarytracker.dao.impl;

import com.rayan.salarytracker.dao.IExpenseDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.model.Expense;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class ExpenseDAO extends GenericCRUDImpl<Expense> implements IExpenseDAO {
    public ExpenseDAO() {
        this.setPersistenceClass(Expense.class);
    }

    @Override
    public List<Expense> getExpensesBySalaryId(Long salaryId, Long userId) {
        String jpql = "SELECT e FROM Expense e JOIN e.salary s WHERE e.salary.id = :salaryId AND s.user.id = :userId";
        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            TypedQuery<Expense> query = em.createQuery(jpql, Expense.class);
            query.setParameter("salaryId", salaryId);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
