package com.rayan.salarytracker.service;

import com.rayan.salarytracker.core.exception.AppServerException;
import com.rayan.salarytracker.dto.salary.SalaryInsertDTO;
import com.rayan.salarytracker.model.Salary;

public interface ISalaryService {

    Salary insertSalary(SalaryInsertDTO salaryInsertDTO) throws AppServerException;
}
