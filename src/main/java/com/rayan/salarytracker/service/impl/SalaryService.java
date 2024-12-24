package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.dao.ISalaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.dto.salary.SalaryInsertDTO;
import com.rayan.salarytracker.mapper.Mapper;
import com.rayan.salarytracker.model.Salary;
import com.rayan.salarytracker.service.ISalaryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@ApplicationScoped
public class SalaryService implements ISalaryService {

    private static final Logger LOGGER = LogManager.getLogger(SalaryService.class.getName());


    private ISalaryDAO salaryDAO;
    private Mapper mapper;

    @Inject
    public SalaryService(ISalaryDAO salaryDAO, Mapper mapper) {
        this.salaryDAO = salaryDAO;
        this.mapper = mapper;
    }

    public SalaryService() {
    }

    @Override
    public Salary insertSalary(SalaryInsertDTO salaryInsertDTO) throws AppServerException {
        try {
            JPAHelperUtil.beginTransaction();
            Salary salary = mapper.mapToSalary(salaryInsertDTO);

            Optional<Salary> salaryOptional = salaryDAO.insert(salary);
            if (salaryOptional.isPresent()) {
                JPAHelperUtil.commitTransaction();
                LOGGER.info("Salary of {} inserted.", salary.getMonth());
                return salary;
            } else {
                throw new AppServerException("Salary",
                        "Salary with month: " + salaryInsertDTO.getMonth() + "not inserted.");
            }
        } catch (AppServerException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Salary with month: {} not inserted.", salaryInsertDTO.getMonth());
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

}
