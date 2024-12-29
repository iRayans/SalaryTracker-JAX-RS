package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.model.Salary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class SalaryDAO extends GenericCRUDImpl<Salary> implements ISalaryDAO {
    public SalaryDAO() {
        this.setPersistenceClass(Salary.class);
    }


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
}
