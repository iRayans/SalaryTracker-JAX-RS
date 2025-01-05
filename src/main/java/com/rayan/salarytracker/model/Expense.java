package com.rayan.salarytracker.model;


import com.rayan.salarytracker.core.enums.BudgetRuleAllocation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "expenses")
public class Expense implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Description must not be empty")
    @Column(name = "description")
    private String description;
    @Column(name = "amount")
    private int amount;
    // Not needed for now
//    @Column(name = "category")
//    private String category;
    @Enumerated(EnumType.STRING)
    private BudgetRuleAllocation budgetRuleAllocation;
    @Column(name = "bank")
    private String bank;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    ///////////////////////////////
    // Relations start from here //
    /// ////////////////////////////

    // Many Expenses mapped to a single Salary.
    // The fetch Type set to lazy since we don't need to fetch user object with the response.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salary_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Salary salary;
}