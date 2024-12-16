package com.rayan.salarytracker.dto.user;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {
    private Long id;
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
