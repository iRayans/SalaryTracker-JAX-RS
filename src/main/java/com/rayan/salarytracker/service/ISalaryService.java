package com.rayan.salarytracker.service;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityAlreadyExistsException;
import com.rayan.salarytracker.core.exception.EntityInvalidArgumentsException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.salary.SalaryInsertDTO;
import com.rayan.salarytracker.dto.salary.SalaryReadOnlyDTO;
import com.rayan.salarytracker.model.Salary;

import java.util.List;

public interface ISalaryService {

    List<SalaryReadOnlyDTO> getAllSalaries();

    SalaryReadOnlyDTO insertSalary(SalaryInsertDTO salaryInsertDTO) throws AppServerException, EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentsException;

    SalaryReadOnlyDTO updateSalary(Long salaryId, Salary Salary) throws AppServerException, EntityNotFoundException, EntityInvalidArgumentsException;

    void deleteSalary(Long salaryId, Long userId) throws EntityNotFoundException;

    List<SalaryReadOnlyDTO> getAllUserSalaries(Long salaryId) throws EntityNotFoundException;
}
