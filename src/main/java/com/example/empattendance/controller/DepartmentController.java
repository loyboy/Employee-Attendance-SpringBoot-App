package com.example.empattendance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.empattendance.dto.CustomApiDataResponse;
import com.example.empattendance.dto.CustomApiResponse;
import com.example.empattendance.dto.DepartmentRequest;
import com.example.empattendance.model.Department;
import com.example.empattendance.model.Employee;
import com.example.empattendance.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department APIs", description = "API Operations related to managing departments")
public class DepartmentController {

  @Autowired 
  private DepartmentService departmentService;

  /**
   * Get all departments API.
   *
   * @return ResponseEntity containing a CustomApiDataResponse with the list of departments,
   * a success flag, and a message. Returns HttpStatus.OK if successful.
   */
  @Operation(summary = "Get all departments", description = "Retrieve a list of all departments")
  @GetMapping
  public ResponseEntity<?> getAllDepartments() {
    List<Department> allDepartments = departmentService.getAllDepartments();
    return new ResponseEntity<>(new CustomApiDataResponse<List<Department>>(true, "All Departments list has been retrieved.", allDepartments), HttpStatus.OK);
  }

  /**
   * Get all employees by department API.
   * @param id ID of the department to be retrieved
   *
   * @return ResponseEntity containing a CustomApiDataResponse with the list of departments,
   * a success flag, and a message. Returns HttpStatus.OK if successful or HttpStatus.NOT_FOUND if not found.
   */
  @Operation(summary = "Get all employees by department", description = "Retrieve employees by department")
  @ApiResponses(
    value = {
      @ApiResponse(responseCode = "200", description = "Employees found within a certain Department ID"),
      @ApiResponse(responseCode = "404", description = "Employee not found within a certain Department ID")
    }
  )
  @GetMapping("/{id}/employees")
  public ResponseEntity<?> getEmployeesByDepartment(@PathVariable Long id) {
    return departmentService.getDepartmentById(id)
    .map(department -> {
      List<Employee> employees = departmentService.getEmployeesByDepartmentId(id);
      return new ResponseEntity<>(new CustomApiDataResponse<>(true, "Employees retreived based on Department ID: " + id, employees), HttpStatus.OK);
    })
    .orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Department was found with ID: " + id, null), HttpStatus.NOT_FOUND));
  }

  /**
   * Get department by ID API.
   *
   * @param id ID of the department to be retrieved
   * @return ResponseEntity containing a CustomApiDataResponse with the copy of the retrieved department,
   * a success flag, and a message. Returns HttpStatus.OK if successful or HttpStatus.NOT_FOUND if not found.
   */
  @Operation(
      summary = "Get department by ID",
      description = "Retrieve a specific department by its ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Department found"),
        @ApiResponse(responseCode = "404", description = "Department not found")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<?> getDepartmentById(@Parameter(description = "ID of the department to be retrieved") @PathVariable Long id) {
    return departmentService.getDepartmentById(id)
                .map(department -> new ResponseEntity<>(new CustomApiDataResponse<Department>(true, "Department details found.", department), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Department was found with ID: " + id, null), HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new department API.
   *
   * @param department Department object to be created
   * @return ResponseEntity containing a CustomApiDataResponse with the copy of a created employee,
   * a success flag, and a message. Returns status 201 if successful.
   */
  @Operation(summary = "Create a new department", description = "Create a new department record")
  @ApiResponse(responseCode = "201", description = "Department created successfully")
  @PostMapping
  public ResponseEntity<?> createDepartment(@RequestBody DepartmentRequest department) {
    if (department.getName().equals("") || department.getName() == null) {
      return new ResponseEntity<>(new CustomApiResponse(false, "Name cannot be empty."), HttpStatus.BAD_REQUEST);
    }
    Department newDepartment = new Department();
    newDepartment.setName(department.getName());
    Department savedDepartment = departmentService.saveDepartment(newDepartment);
    return new ResponseEntity<>(new CustomApiDataResponse<Department>(true, "Department details saved.", savedDepartment), HttpStatus.CREATED);
  }

  /**
   * Update an existing department API.
   *
   * @param id ID of the department to be updated
   * @param departmentDetails Updated department object
   * @return ResponseEntity containing a CustomApiDataResponse with the copy of updated department,
   * a success flag, and a message. Returns status 200 if successful and status 404 if not successful.
   */
  @Operation(
      summary = "Update an existing department",
      description = "Update an existing department's details")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Department updated"),
        @ApiResponse(responseCode = "404", description = "Department not found")
      })
  @PutMapping("/{id}")
  public ResponseEntity<?> updateDepartment(@Parameter(description = "ID of the department to be updated") @PathVariable Long id, @RequestBody DepartmentRequest departmentDetails) {     
    
    return departmentService.getDepartmentById(id)
    .map(department -> { 
      department.setName(departmentDetails.getName());
      Department updatedDepartment = departmentService.saveDepartment(department);
      return new ResponseEntity<>(new CustomApiDataResponse<Department>(true, "Department details updated.", updatedDepartment), HttpStatus.OK);
    })
    .orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Department was found with ID: " + id, null), HttpStatus.NOT_FOUND));
  }

  /**
   * Delete a department API.
   *
   * @param id ID of the department to be deleted
   * @return ResponseEntity containing a CustomApiDataResponse with the copy of deleted department,
   * a success flag, and a message. Returns status 200 if successful and status 404 if not successful.
   */
  @Operation(summary = "Delete a department", description = "Delete a department record by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Department deleted"),
        @ApiResponse(responseCode = "404", description = "Department not found")
      }
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteDepartment(@Parameter(description = "ID of the department to be deleted") @PathVariable Long id) {
      return departmentService.getDepartmentById(id).map(department -> { 
                departmentService.deleteDepartment(id);
                return new ResponseEntity<>(new CustomApiDataResponse<Department>(true, "Department details deleted.", department), HttpStatus.OK); 
              }).orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Department was found with ID: " + id, null), HttpStatus.NOT_FOUND));
  }
}
