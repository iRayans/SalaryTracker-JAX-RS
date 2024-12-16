package com.rayan.salarytracker.model;


import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Description must not be empty")
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private int amount;
    @Column(name = "category")
    private String category;
    @Column(name = "bank")
    private String bank;
    @Column(name = "status")
    private String status;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    ///////////////////////////////
    // Relations start from here //
    ///////////////////////////////
    
    // Many Expenses mapped to a single Salary.
    // The fetch Type set to lazy since we don't need to fetch user object with the response.
    @ManyToOne(fetch = FetchType.LAZY, optional = false) 
    @JoinColumn(name = "salary_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // When delete a salary all expenses belong to that salary will be deleted.
    @JsonIgnore
    private Salary salary;
}