package com.rayan.salarytracker.dao;

import com.rayan.salarytracker.model.Summary;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SummaryDAO extends GenericCRUDImpl<Summary> {
    public SummaryDAO() {
        this.setPersistenceClass(Summary.class);
    }
}
