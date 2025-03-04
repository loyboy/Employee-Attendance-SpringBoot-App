package com.example.empattendance.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceExcuseRequest {
    @NotNull
    private Long employeeId;
    @NotNull
    private LocalDate date;
    private String notes;
}
