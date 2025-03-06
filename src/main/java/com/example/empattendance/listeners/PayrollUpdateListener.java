package com.example.empattendance.listeners;

import com.example.empattendance.events.AttendanceRegisteredEvent;
import com.example.empattendance.model.AttendanceRecord;
import com.example.empattendance.model.Employee;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PayrollUpdateListener {

    @EventListener
    public void handleAttendanceRegisteredEvent(AttendanceRegisteredEvent event) {
        Employee employee = event.getEmployee();
        AttendanceRecord attendance = event.getAttendance();
        
        // Simulate updating payroll calculations
        System.out.println("Updating payroll for Employee: " + employee.getFirstName() + " " + employee.getLastName());
        System.out.println("Attendance Type: " + attendance.getType() + " at " + attendance.getDateOfAtt());
    }
}

