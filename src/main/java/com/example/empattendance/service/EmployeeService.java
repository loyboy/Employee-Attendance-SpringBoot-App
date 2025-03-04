package com.example.empattendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.empattendance.model.Employee;
import com.example.empattendance.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

  @Autowired 
  private EmployeeRepository employeeRepository;

  /**
   * Get all employees.
   *
   * @return List of all employees
   */
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAllWithDepartments();
  }

  /**
   * Get employee by ID.
   *
   * @param id ID of the employee to be retrieved
   * @return Employee with the specified ID
   */
  public Optional<Employee> getEmployeeById(Long id) {
    return employeeRepository.findById(id);
  }

  /**
   * Save an employee.
   *
   * @param employee Employee to be saved
   * @return Saved employee
   */
  public Employee saveEmployee(Employee employee) {
    return employeeRepository.save(employee);
  }

  /**
   * Delete an employee.
   *
   * @param id ID of the employee to be updated
   */
  public void deleteEmployee(Long id) {
    employeeRepository.deleteById(id);
  }

  public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
    return employeeRepository.findByDepartmentId(departmentId);
  }
}
