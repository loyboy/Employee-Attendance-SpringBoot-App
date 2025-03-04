package com.example.empattendance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attendancerecords")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private LocalDate dateOfAtt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType type;
    
    private LocalTime signInTime;
    
    private LocalTime signOutTime;
    
    private String notes;        
    
}
