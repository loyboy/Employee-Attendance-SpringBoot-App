package com.example.empattendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.empattendance.exception.BadRequestException;
import com.example.empattendance.exception.ResourceNotFoundException;
import com.example.empattendance.model.AttendanceRecord;
import com.example.empattendance.model.AttendanceType;
import com.example.empattendance.model.Employee;
import com.example.empattendance.repository.AttendanceRepository;
import com.example.empattendance.repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

  @Autowired 
  private EmployeeRepository employeeRepository;

  @Autowired 
  private AttendanceRepository attendanceRepository;

    /**
     * Sign in Employee.
     *
     * @param employeeId ID of the employee to submit register
     * @param notes notes to add into register
     * @return an AttendanceRecord
     */
    @Transactional
    public AttendanceRecord signIn(Long employeeId, String notes) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        LocalDate today = LocalDate.now();
        Optional<AttendanceRecord> existingRecord = attendanceRepository.findByEmployeeIdAndDateOfAtt(employeeId, today);
        
        if (existingRecord.isPresent()) {
            AttendanceRecord record = existingRecord.get();
            if (record.getType() == AttendanceType.PRESENT && record.getSignInTime() != null) {
                throw new IllegalStateException("Employee already signed in today");
            }
        }
        
        AttendanceRecord record = new AttendanceRecord();
        record.setEmployee(employee);
        record.setDateOfAtt(today);
        record.setType(AttendanceType.PRESENT);
        record.setSignInTime(LocalTime.now());
        record.setNotes(notes);
        
        return attendanceRepository.save(record);
    }

    /**
     * Sign out Employee.
     *
     * @param recordId ID of the recordId from register
     * @param notes notes to add into register
     * @return an AttendanceRecord
     */
    @Transactional
    public AttendanceRecord signOut(Long recordId, String notes) {
        AttendanceRecord record = attendanceRepository.findById(recordId)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + recordId));
        
        if (record.getSignInTime() == null) {
            throw new BadRequestException("Cannot sign out without signing in first");
        }
        
        if (record.getSignOutTime() != null) {
            throw new BadRequestException("Employee already signed out");
        }

        record.setSignOutTime(LocalTime.now());
        
        if (notes != null && !notes.isEmpty()) {
            String existingNotes = record.getNotes();
            record.setNotes(existingNotes != null ? existingNotes + " | " + notes : notes);
        }
        
        return attendanceRepository.save(record);
    }

    /**
     * Record Sick Leave.
     *
     * @param employeeId ID of the recordId from register
     * @param date Date of the record
     * @param notes notes to add into sick leave register
     * @return an AttendanceRecord
     */
    @Transactional
    public AttendanceRecord recordSickLeave(Long employeeId, LocalDate date, String notes) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        LocalDate recordDate = (date != null) ? date : LocalDate.now();
        
        Optional<AttendanceRecord> existingRecord = attendanceRepository.findByEmployeeIdAndDateOfAtt(employeeId, recordDate);
        
        if (existingRecord.isPresent()) {
            AttendanceRecord record = existingRecord.get();
            record.setType(AttendanceType.SICK_LEAVE);
            record.setSignInTime(null);
            record.setSignOutTime(null);
            record.setNotes(notes);
            return attendanceRepository.save(record);
        } else {
            AttendanceRecord record = new AttendanceRecord();
            record.setEmployee(employee);
            record.setDateOfAtt(date);
            record.setType(AttendanceType.SICK_LEAVE);
            record.setNotes(notes);
            return attendanceRepository.save(record);
        }
    }

    /**
     * Record Absence of Employee.
     *
     * @param employeeId ID of the recordId from register
     * @param date Date of the record
     * @param notes notes to add into sick leave register
     * @return an AttendanceRecord
     */
    @Transactional
    public AttendanceRecord recordAbsence(Long employeeId, LocalDate date, String notes) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        
        LocalDate recordDate = (date != null) ? date : LocalDate.now();
        
        Optional<AttendanceRecord> existingRecord = attendanceRepository.findByEmployeeIdAndDateOfAtt(employeeId, recordDate);
        
        if (existingRecord.isPresent()) {
            AttendanceRecord record = existingRecord.get();
            record.setType(AttendanceType.ABSENT);
            record.setSignInTime(null);
            record.setSignOutTime(null);
            record.setNotes(notes);
            return attendanceRepository.save(record);
        } else {
            AttendanceRecord record = new AttendanceRecord();
            record.setEmployee(employee);
            record.setDateOfAtt(recordDate);
            record.setType(AttendanceType.ABSENT);
            record.setNotes(notes);
            return attendanceRepository.save(record);
        }
    }

    public List<AttendanceRecord> getAttendanceRecordsByEmployeeAndDateRange(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BadRequestException(
                "Start date must be before or equal to end date"
            );
        }
        return attendanceRepository.findByEmployeeIdAndDateBetweenOrderByDateAsc(
            employeeId, startDate, endDate
        );
    }

 
}

