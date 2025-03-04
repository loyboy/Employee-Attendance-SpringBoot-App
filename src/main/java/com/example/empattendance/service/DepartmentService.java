package com.example.empattendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.empattendance.model.Department;
import com.example.empattendance.model.Employee;
import com.example.empattendance.repository.DepartmentRepository;
import com.example.empattendance.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

  @Autowired 
  private DepartmentRepository departmentRepository;

  @Autowired 
  private EmployeeRepository employeeRepository;

  /**
   * Get all departments.
   *
   * @return List of all departments
   */
  public List<Department> getAllDepartments() {
    return departmentRepository.findAll();
  }

  /**
   * Get department by ID.
   *
   * @param id ID of the department to be retrieved
   * @return Department with the specified ID
   */
  public Optional<Department> getDepartmentById(Long id) {
    return departmentRepository.findById(id);
  }

  /**
   * Save a department.
   *
   * @param department Department to be saved
   * @return Saved department
   */
  public Department saveDepartment(Department department) {
    return departmentRepository.save(department);
  }

  /**
   * Update a department.
   *
   * @param id ID of the department to be updated
   */
  public void deleteDepartment(Long id) {
    departmentRepository.deleteById(id);
  }

  /**
   * get employees by Department ID.
   *
   * @param id ID of the department to be queried
   */
  public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
    return employeeRepository.findByDepartmentId(departmentId);
  }
}
