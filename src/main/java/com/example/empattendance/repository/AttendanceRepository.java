package com.example.empattendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.empattendance.model.AttendanceRecord;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByEmployeeIdAndDateOfAtt(Long employeeId, LocalDate date);
    
    @Query("SELECT a FROM AttendanceRecord a WHERE a.employee.id = :employeeId " +
           "AND (:startDate IS NULL OR a.dateOfAtt >= :startDate) " +
           "AND (:endDate IS NULL OR a.dateOfAtt <= :endDate) " +
           "ORDER BY a.dateOfAtt ASC")
    List<AttendanceRecord> findByEmployeeIdAndDateBetweenOrderByDateAsc(Long employeeId, LocalDate startDate, LocalDate endDate);
}
