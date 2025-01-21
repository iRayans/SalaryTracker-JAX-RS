package com.rayan.salarytracker.dao.impl;

import com.rayan.salarytracker.core.enums.ExpenseAction;
import com.rayan.salarytracker.dao.ISummaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.model.Summary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@ApplicationScoped
public class SummaryDAO extends GenericCRUDImpl<Summary> implements ISummaryDAO {
    public SummaryDAO() {
        this.setPersistenceClass(Summary.class);
    }

    private static final Logger LOGGER = LogManager.getLogger(SummaryDAO.class.getName());


    @Override
    public Optional<Summary> getSummaryBySalaryId(Long salaryId) {
        String jpql = "SELECT s FROM Summary s WHERE s.salary.id = :salaryId";
        try {
            // Get EntityManager.
            EntityManager em = JPAHelperUtil.getEntityManager();
            TypedQuery<Summary> query = em.createQuery(jpql, Summary.class);
            query.setParameter("salaryId", salaryId);
            Summary summary = query.getSingleResult();
            return Optional.of(summary);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean updateSummary(int expenseAmount, Long salaryId, ExpenseAction action) {
        String jpql = getJpql(action);
        LOGGER.info("Updating summary for action: {} ", action);

        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            Query query = em.createQuery(jpql);
            query.setParameter("expenseAmount", expenseAmount);
            query.setParameter("salaryId", salaryId);

            int rowsUpdated = query.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            LOGGER.error("Error in calcSummary for salaryId: {}", salaryId, e);
            throw e;
        }
    }

    private static String getJpql(ExpenseAction action) {
        String jpql = null;

        if (action == ExpenseAction.ADD) {
            jpql = "UPDATE Summary s " +
                    "SET s.totalExpense = s.totalExpense + :expenseAmount, " +
                    "    s.remainingSalary = s.remainingSalary - :expenseAmount " +
                    "WHERE s.salary.id = :salaryId";
        } else if (action == ExpenseAction.DELETE) {
            jpql = "UPDATE Summary s " +
                    "SET s.totalExpense = s.totalExpense - :expenseAmount, " +
                    "    s.remainingSalary = s.remainingSalary + :expenseAmount " +
                    "WHERE s.salary.id = :salaryId";
        } else {
            throw new IllegalArgumentException("Invalid action: " + action);
        }
        return jpql;
    }


}
