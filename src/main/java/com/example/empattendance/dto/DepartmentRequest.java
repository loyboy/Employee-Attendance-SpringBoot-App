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
public class DepartmentRequest {
    @NotNull
    private String name;
}
