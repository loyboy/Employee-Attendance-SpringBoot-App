package com.example.empattendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String gender;
    @NotNull
    private Long   departmentId;
   
    private String address;
    @NotNull
    private String employmentType;
}
