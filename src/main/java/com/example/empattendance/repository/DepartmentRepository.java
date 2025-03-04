package com.example.empattendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.empattendance.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {}
