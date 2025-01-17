package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.SummaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.dto.summary.SummaryReadOnlyDTO;
import com.rayan.salarytracker.mapper.Mapper;
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
    private Mapper mapper;

    public SummaryService() {
    }

    @Inject
    public SummaryService(SummaryDAO summaryDAO, Mapper mapper) {
        this.summaryDAO = summaryDAO;
        this.mapper = mapper;
    }

    public SummaryReadOnlyDTO getSummary(Long salaryId) throws EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();
            SummaryReadOnlyDTO summaryReadOnlyDTO = summaryDAO.getSummaryBySalaryId(salaryId)
                    .map(mapper::mapToSummaryReadOnlyDTO)
                    .orElseThrow(() -> new EntityNotFoundException("Summary", " with salary Id: " + salaryId + " was not found."));
            JPAHelperUtil.commitTransaction();
            return summaryReadOnlyDTO;
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Summary for salaryId: {} not found.", salaryId);
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    public void initializeSummary(Salary salary) throws EntityNotFoundException {
        Summary summary = new Summary();
        summary.setSalary(salary);
        summary.setRemainingSalary(salary.getAmount());
        summaryDAO.insert(summary)
                .orElseThrow(() -> new EntityNotFoundException("Summary", "Summary initialized failed."));
        LOGGER.info("Summary for month: {} initialized successfully.", salary.getMonth());
    }

    public void updateSummary(int expenseAmount, Long salaryId) throws AppServerException {
        try {
            LOGGER.info("Updating summary for salaryId: {}", salaryId);
            JPAHelperUtil.beginTransaction();
            boolean updated = summaryDAO.calcSummary(expenseAmount, salaryId);
            if (updated) {
                LOGGER.info("Summary updated successfully for salaryId: {}", salaryId);
            } else {
                LOGGER.warn("No rows were updated for salaryId: {}", salaryId);
            }
            JPAHelperUtil.commitTransaction();
        } catch (Exception e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Error while updating summary for salaryId: {}", salaryId, e);
            throw new RuntimeException(e);
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }
}
