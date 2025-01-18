package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.enums.ExpenseAction;
import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.ISummaryDAO;
import com.rayan.salarytracker.dao.SummaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.dto.summary.SummaryReadOnlyDTO;
import com.rayan.salarytracker.mapper.Mapper;
import com.rayan.salarytracker.model.Salary;
import com.rayan.salarytracker.model.Summary;
import com.rayan.salarytracker.service.ISummaryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ApplicationScoped
public class SummaryService implements ISummaryService {
    private static final Logger LOGGER = LogManager.getLogger(SummaryService.class.getName());

    private ISummaryDAO summaryDAO;
    private Mapper mapper;

    public SummaryService() {
    }

    @Inject
    public SummaryService(SummaryDAO summaryDAO, Mapper mapper) {
        this.summaryDAO = summaryDAO;
        this.mapper = mapper;
    }

    @Override
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

    /*
     * This method responsible for initialize Summary for  Salary
     */
    @Override
    public void initializeSummary(Salary salary) throws EntityNotFoundException {
        Summary summary = new Summary();
        summary.setSalary(salary);
        summary.setRemainingSalary(salary.getAmount());
        summaryDAO.insert(summary)
                .orElseThrow(() -> new EntityNotFoundException("Summary", "Summary initialized failed."));
        LOGGER.info("Summary for month: {} initialized successfully.", salary.getMonth());
    }

    @Override
    public void updateSummary(int expenseAmount, Long salaryId, ExpenseAction action) throws AppServerException {
        try {
            LOGGER.info("Updating summary for salaryId: {}", salaryId);
            boolean updated = summaryDAO.updateSummary(expenseAmount, salaryId, action);
            if (updated) {
                LOGGER.info("Summary updated successfully for salaryId: {}", salaryId);
            } else {
                LOGGER.warn("No rows were updated for salaryId: {}", salaryId);
            }
        } catch (Exception e) {
            LOGGER.error("Error while updating summary for salaryId: {}", salaryId, e);
            throw new RuntimeException(e);
        } finally {
        }
    }
}
