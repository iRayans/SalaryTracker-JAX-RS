package com.rayan.salarytracker.service.impl;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dao.ISalaryDAO;
import com.rayan.salarytracker.database.JPAHelperUtil;
import com.rayan.salarytracker.dto.salary.SalaryInsertDTO;
import com.rayan.salarytracker.dto.salary.SalaryReadOnlyDTO;
import com.rayan.salarytracker.mapper.Mapper;
import com.rayan.salarytracker.model.Salary;
import com.rayan.salarytracker.service.ISalaryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public SalaryReadOnlyDTO insertSalary(SalaryInsertDTO salaryInsertDTO) throws AppServerException {
        try {
            JPAHelperUtil.beginTransaction();
            Salary salary = mapper.mapToSalary(salaryInsertDTO);

            SalaryReadOnlyDTO salaryReadOnlyDTO = salaryDAO.insert(salary)
                    .map(mapper::mapToSalaryReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("Salary",
                            "Salary with Month: " + salaryInsertDTO.getMonth() + "not inserted."));
            JPAHelperUtil.commitTransaction();
            LOGGER.info("Salary with month: {} inserted.", salaryInsertDTO.getMonth());
            return salaryReadOnlyDTO;

        } catch (AppServerException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Salary with month: {} not inserted.", salaryInsertDTO.getMonth());
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public SalaryReadOnlyDTO updateSalary(Long salaryId, Salary salary) throws AppServerException, EntityNotFoundException {

        try {
            JPAHelperUtil.beginTransaction();
            Salary existingSalary = salaryDAO.getById(salaryId)
                    .orElseThrow(() -> new EntityNotFoundException("Salary with id: " + salary.getId() + " not found."));

            existingSalary.setMonth(salary.getMonth() != null ? salary.getMonth() : existingSalary.getMonth());
            existingSalary.setAmount(salary.getAmount() != 0 ? salary.getAmount() : existingSalary.getAmount());
            existingSalary.setDescription(salary.getDescription() != null ? salary.getDescription() : existingSalary.getDescription());

            SalaryReadOnlyDTO salaryReadOnlyDTO = salaryDAO.update(existingSalary)
                    .map(mapper::mapToSalaryReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("Salary",
                            "Salary with Month: " + existingSalary.getMonth() + "not updated."));
            JPAHelperUtil.commitTransaction();
            LOGGER.info("Salary with month: {} updated.", existingSalary.getMonth());
            return salaryReadOnlyDTO;

        } catch (AppServerException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Salary with month: {} not inserted.", salary.getMonth());
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }


}
