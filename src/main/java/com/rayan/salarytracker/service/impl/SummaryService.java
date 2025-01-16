package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.SummaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.model.Salary;
import com.rayan.salarytracker.model.Summary;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class SummaryService {
    private static final Logger LOGGER = LogManager.getLogger(SummaryService.class.getName());

    private SummaryDAO summaryDAO;

    public SummaryService() {
    }

    @Inject
    public SummaryService(SummaryDAO summaryDAO) {
        this.summaryDAO = summaryDAO;

    }

    public void initializeSummary(Salary salary) throws EntityNotFoundException {
        // initialize Summary for given month "Salary".
        Summary summary = new Summary();
        summary.setSalary(salary);
        // Set remaining salary as salary
        summary.setRemainingSalary(salary.getAmount());
        summaryDAO.insert(summary)
                .orElseThrow(() -> new EntityNotFoundException("Summary", "Summary initialized failed."));
        LOGGER.info("Summary for month: {} initialized successfully.", salary.getMonth());
    }

    public void updateSummary(int expenseAmount, Long salaryId) throws AppServerException {
        try {
            LOGGER.info("Updating summary for salaryId: {}", salaryId);
            JPAHelperUtil.beginTransaction(); // Start the transaction here
            boolean updated = summaryDAO.calcSummary(expenseAmount, salaryId);
            if (updated) {
                LOGGER.info("Summary updated successfully for salaryId: {}", salaryId);
            } else {
                LOGGER.warn("No rows were updated for salaryId: {}", salaryId);
            }
            JPAHelperUtil.commitTransaction(); // Commit the transaction
        } catch (Exception e) {
            JPAHelperUtil.rollbackTransaction(); // Rollback on error
            LOGGER.error("Error while updating summary for salaryId: {}", salaryId, e);
            throw new RuntimeException(e);
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }
}
