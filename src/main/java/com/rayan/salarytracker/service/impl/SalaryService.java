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

import java.util.ArrayList;
import java.util.List;

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
    public List<SalaryReadOnlyDTO> getAllSalaries() {
        try {
            JPAHelperUtil.beginTransaction();
            List<Salary> salaries = salaryDAO.getAll();
            JPAHelperUtil.commitTransaction();
            return salaries.stream().map(mapper::mapToSalaryReadOnlyDTO).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SalaryReadOnlyDTO> getAllUserSalaries(Long userId) {
        List<SalaryReadOnlyDTO> salaryReadOnlyDTOs = new ArrayList<>();
        try {
            JPAHelperUtil.beginTransaction();

            // Fetch salaries and map to DTOs
            List<Salary> salaries = salaryDAO.findSalaryByUserId(userId);
            if (salaries.isEmpty()) {
                throw new EntityNotFoundException("No salaries found for user ID: " + userId);
            }
            salaryReadOnlyDTOs = salaries.stream()
                    .map(mapper::mapToSalaryReadOnlyDTO)
                    .toList();

            JPAHelperUtil.commitTransaction();
            LOGGER.info("Successfully retrieved salaries for user ID: {}", userId);
            return salaryReadOnlyDTOs;

        } catch (EntityNotFoundException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.warn("No salaries found for user ID: {}", userId, e);
            throw e;
        } catch (Exception e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("An error occurred while retrieving salaries for user ID: {}", userId, e);
            throw new RuntimeException("Error retrieving salaries for user ID: " + userId, e);
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
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

            // Update only provided fields
            updateFields(existingSalary, salary);

            SalaryReadOnlyDTO salaryReadOnlyDTO = salaryDAO.update(existingSalary)
                    .map(mapper::mapToSalaryReadOnlyDTO)
                    .orElseThrow(() -> new AppServerException("Salary",
                            "Salary with id: " + existingSalary.getId() + "not updated."));
            JPAHelperUtil.commitTransaction();
            LOGGER.info("Salary with id: {} updated.", existingSalary.getId());
            return salaryReadOnlyDTO;

        } catch (AppServerException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Salary with id: {} not inserted.", salary.getMonth());
            throw e;
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    @Override
    public void deleteSalary(Long salaryId, Long userId) throws EntityNotFoundException {
        try {
            JPAHelperUtil.beginTransaction();

            // Fetch the salary and validate ownership
            Salary salary = salaryDAO.getById(salaryId)
                    .filter(s -> s.getUser().getId().equals(userId))
                    .orElseThrow(() -> new EntityNotFoundException("Salary with id: " + salaryId + " not found or does not belong to user with id: " + userId));

            // Delete the salary
            salaryDAO.delete(salaryId);
            JPAHelperUtil.commitTransaction();

            LOGGER.info("Salary with id: {} deleted successfully by user with id: {}.", salaryId, userId);
        } catch (EntityNotFoundException e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Failed to delete salary with id: {} by user with id: {}. Reason: {}", salaryId, userId, e.getMessage(), e);
            throw e; // Rethrow exception to notify caller
        } catch (Exception e) {
            JPAHelperUtil.rollbackTransaction();
            LOGGER.error("Unexpected error while deleting salary with id: {} by user with id: {}.", salaryId, userId, e);
            throw new RuntimeException("An unexpected error occurred during deletion.", e);
        } finally {
            JPAHelperUtil.closeEntityManager();
        }
    }

    private void updateFields(Salary existing, Salary updated) {
        existing.setMonth(updated.getMonth() != null ? updated.getMonth() : existing.getMonth());
        existing.setAmount(updated.getAmount() != 0 ? updated.getAmount() : existing.getAmount());
        existing.setDescription(updated.getDescription() != null ? updated.getDescription() : existing.getDescription());
    }
}
