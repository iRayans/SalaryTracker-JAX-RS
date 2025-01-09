package com.rayan.salarytracker.model;

import jakarta.persistence.*;
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
@Table(name = "summary")
public class Summary implements IdentifiableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_expense")
    private int totalExpense;
    @Column(name = "remaining_salary")
    private int remainingSalary;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;


    ///////////////////////////////
    // Relations start from here //
    /// ////////////////////////////

    // Many Summaries mapped to a single Salary.
    // The fetch Type set to lazy since we don't need to fetch user object with the response.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "salary_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Salary salary;
}
