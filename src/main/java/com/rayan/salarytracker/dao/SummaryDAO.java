package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.model.Summary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class SummaryDAO extends GenericCRUDImpl<Summary> {
    public SummaryDAO() {
        this.setPersistenceClass(Summary.class);
    }

    private static final Logger LOGGER = LogManager.getLogger(SummaryDAO.class.getName());


    public boolean calcSummary(int expenseAmount, Long salaryId) {
        String jpql = "UPDATE Summary s " +
                "SET s.totalExpense = s.totalExpense + :expenseAmount, " +
                "    s.remainingSalary = s.remainingSalary - :expenseAmount " +
                "WHERE s.salary.id = :salaryId";
        try {
            EntityManager em = JPAHelperUtil.getEntityManager(); // Assume transaction is already active
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


}
