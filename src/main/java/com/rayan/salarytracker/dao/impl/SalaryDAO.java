package com.rayan.salarytracker.dao.impl;

import com.rayan.salarytracker.dao.ISalaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.model.Salary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class SalaryDAO extends GenericCRUDImpl<Salary> implements ISalaryDAO {
    public SalaryDAO() {
        this.setPersistenceClass(Salary.class);
    }

    private static final Logger LOGGER = LogManager.getLogger(SalaryDAO.class.getName());

    @Override
    public List<Salary> findSalaryByUserId(Long userId) {
        String jpql = "SELECT s FROM Salary s WHERE s.user.id = :userId";
        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            TypedQuery<Salary> query = em.createQuery(jpql, Salary.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean existsByUserIdAndMonthAndYear(Long userId, String month, int year) {
        String jpql = "SELECT COUNT(s) FROM Salary s WHERE s.user.id = :userId AND s.month = :month AND s.year = :year";
        try {
            EntityManager em = JPAHelperUtil.getEntityManager();
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("userId", userId);
            query.setParameter("month", month);
            query.setParameter("year", year);
            Long count = query.getSingleResult();

            return count > 0;
        } catch (Exception e) {
            LOGGER.error("Error in existsByUserIdAndMonthAndYear for userId: {}", userId, e);
            throw e;
        }
    }
}
