package com.example.empattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.empattendance.model.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  /**
   * Find all employees with their departments.
   *
   * @return List of all employees with their departments
   */
  @Query("SELECT e FROM Employee e JOIN FETCH e.department")
  List<Employee> findAllWithDepartments();

  List<Employee> findByDepartmentId(Long departmentId);
}
