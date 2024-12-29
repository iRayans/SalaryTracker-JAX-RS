package com.rayan.salarytracker.service;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.core.exception.EntityNotFoundException;
import com.rayan.salarytracker.dto.salary.SalaryInsertDTO;
import com.rayan.salarytracker.dto.salary.SalaryReadOnlyDTO;
import com.rayan.salarytracker.model.Salary;

import java.util.List;

public interface ISalaryService {

    List<SalaryReadOnlyDTO> getAllSalaries();

    SalaryReadOnlyDTO insertSalary(SalaryInsertDTO salaryInsertDTO) throws AppServerException;

    SalaryReadOnlyDTO updateSalary(Long salaryId, Salary Salary) throws AppServerException, EntityNotFoundException;

    void deleteSalary(Long salaryId) throws EntityNotFoundException;

    SalaryReadOnlyDTO getSalaryById(Long salaryId);
}
