package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.SalaryDAO;
import com.rayan.salarytracker.dao.SummaryDAO;
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
    private SalaryDAO salaryDAO;

    public SummaryService() {
    }

    @Inject
    public SummaryService(SummaryDAO summaryDAO, SalaryDAO salaryDAO, Mapper mapper) {
        this.summaryDAO = summaryDAO;
        this.salaryDAO = salaryDAO;
        this.mapper = mapper;
    }

    public void insertSummary(Salary salary) throws EntityNotFoundException {
        Summary summary = new Summary();
        summary.setSalary(salary);

        summaryDAO.insert(summary)
                .orElseThrow(() -> new EntityNotFoundException("Summary", "Summary insert failed."));
        LOGGER.info("Summary inserted successfully.");
    }
}
