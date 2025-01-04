package com.rayan.salarytracker.dao;

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
    public List<Expense> getExpensesBySalaryId(Long salaryId) {
        String jpql = "SELECT e FROM Expense e WHERE e.salary.id = :salaryId";
        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            TypedQuery<Expense> query = em.createQuery(jpql, Expense.class);
            query.setParameter("salaryId", salaryId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
}
