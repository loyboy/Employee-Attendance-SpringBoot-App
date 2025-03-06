package com.example.empattendance.events;

import com.example.empattendance.model.AttendanceRecord;
import com.example.empattendance.model.Employee;
import org.springframework.context.ApplicationEvent;

public class AttendanceRegisteredEvent extends ApplicationEvent {
    private final Employee employee;
    private final AttendanceRecord attendance;

    public AttendanceRegisteredEvent(Object source, Employee employee, AttendanceRecord attendance) {
        super(source);
        this.employee = employee;
        this.attendance = attendance;
    }

    public Employee getEmployee() {
        return employee;
    }

    public AttendanceRecord getAttendance() {
        return attendance;
    }
}