package com.example.empattendance.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import com.example.empattendance.dto.CustomApiDataResponse;
import com.example.empattendance.dto.CustomApiResponse;
import com.example.empattendance.dto.EmployeeRequest;
import com.example.empattendance.model.Department;
import com.example.empattendance.model.Employee;
import com.example.empattendance.model.EmployeeType;
import com.example.empattendance.model.Gender;
import com.example.empattendance.service.DepartmentService;
import com.example.empattendance.service.EmployeeService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employees APIs", description = "API Operations related to managing employees")
public class EmployeeController {

  @Autowired 
  private EmployeeService employeeService;

  @Autowired 
  private DepartmentService departmentService;

  /**
 * Retrieves a list of all employees and returns it in a custom API response.
 *
 * @return ResponseEntity containing a CustomApiDataResponse with the list of employees,
 * a success flag, and a message. Returns HttpStatus.OK if successful.
 */
  @Operation(summary = "Get all employees", description = "Retrieve a list of all employees")
  @GetMapping
  public ResponseEntity<?> getAllEmployees() {
    List<Employee> allEmployees = employeeService.getAllEmployees();
    return new ResponseEntity<>(new CustomApiDataResponse<List<Employee>>(true, "All employees list retrieved.", allEmployees), HttpStatus.OK);
  }

  /**
   * Get employee by ID API.
   *
   * @param id ID of the employee to be retrieved
   * @return ResponseEntity containing a CustomApiDataResponse with the employee,
   * a success flag, and a message. Returns HttpStatus.OK if successful.
   */
  @Operation(
      summary = "Get employee by ID",
      description = "Retrieve a specific employee by their ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
    return employeeService.getEmployeeById(id)
                .map(employee -> new ResponseEntity<>(new CustomApiDataResponse<Employee>(true, "Employee details found.", employee), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Employee was found with ID: " + id, null), HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new employee API.
   *
   * @param employee New employee object dto
   * @return ResponseEntity containing a CustomApiDataResponse with the copy of saved employee,
   * a success flag, and a message. Returns HttpStatus.CREATED if successful.
   */
  @Operation(summary = "Create a new employee", description = "Create a new employee record")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Employee created"),
        @ApiResponse(responseCode = "400", description = "Bad Request in Payload.")
      }
  )
  @PostMapping
  public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeRequest employee) {
    boolean isValidGender = Arrays.stream(Gender.values()).anyMatch(gender -> gender.name().equals( employee.getGender().toUpperCase() ));
    if(!isValidGender){
      return new ResponseEntity<>(new CustomApiResponse(false, "Gender enum value is not correct. It is either MALE or FEMALE."), HttpStatus.BAD_REQUEST);
    }
    boolean isValidEmpType = Arrays.stream(EmployeeType.values()).anyMatch(empTyp -> empTyp.name().equals( employee.getEmploymentType().toUpperCase() ));
    if(!isValidEmpType){
      return new ResponseEntity<>(new CustomApiResponse(false, "Employment Type enum value is not correct. It is either MEDICAL or NON_MEDICAL."), HttpStatus.BAD_REQUEST);
    }

    return departmentService.getDepartmentById(employee.getDepartmentId()).map(department -> {
      Employee newEmployee = new Employee();
      newEmployee.setFirstName(employee.getFirstName());
      newEmployee.setLastName(employee.getLastName());
      newEmployee.setGender( Gender.valueOf( employee.getGender().toUpperCase() ));
      newEmployee.setType( EmployeeType.valueOf( employee.getEmploymentType().toUpperCase() ));
      newEmployee.setDepartment(department);
      if (employee.getAddress() != null) {
        newEmployee.setAddress(employee.getAddress());
      }
      Employee createdEmployee = employeeService.saveEmployee(newEmployee);
      return new ResponseEntity<>(new CustomApiDataResponse<>(true, "New employee details created.", createdEmployee), HttpStatus.CREATED);
    }).orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Department was found with ID: " + employee.getDepartmentId(), null), HttpStatus.NOT_FOUND));
  }

  /**
   * Update an existing employee API.
   *
   * @param id ID of the employee to be updated
   * @param employeeDetails employee details payload
   * @return ResponseEntity containing a CustomApiDataResponse with the copy of updated employee,
   * a success flag, and a message. Returns status 200 if successful and status 404 if not successful.
   */
  @Operation(
      summary = "Update an existing employee",
      description = "Update an existing employee's details")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Employee updated"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "400", description = "Bad Request within the Payload")
      }
  )
  @PutMapping("/{id}")
  public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeDetails) {
    if (employeeDetails.getGender() != null) {
      boolean isValidGender = Arrays.stream(Gender.values()).anyMatch(gender -> gender.name().equals( employeeDetails.getGender().toUpperCase() ));
      if(!isValidGender){
        return new ResponseEntity<>(new CustomApiResponse(false, "Gender enum value is not correct. It is either MALE or FEMALE."), HttpStatus.BAD_REQUEST);
      }
    }

    if (employeeDetails.getEmploymentType() != null) {
      boolean isValidEmpType = Arrays.stream(EmployeeType.values()).anyMatch(empTyp -> empTyp.name().equals( employeeDetails.getEmploymentType().toUpperCase() ));
      if(!isValidEmpType){
        return new ResponseEntity<>(new CustomApiResponse(false, "Employment Type enum value is not correct. It is either MEDICAL or NON_MEDICAL."), HttpStatus.BAD_REQUEST);
      }
    }

    if (employeeDetails.getDepartmentId() != null){
      Optional<Department> foundDepartment = departmentService.getDepartmentById(employeeDetails.getDepartmentId());
      if (!foundDepartment.isPresent()){
        return new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Department was found with ID: " + employeeDetails.getDepartmentId(), null), HttpStatus.NOT_FOUND);
      }
    }

    return employeeService.getEmployeeById(id)
                .map(employee -> { 
                  if (employeeDetails.getFirstName() != null) {
                    employee.setFirstName(employeeDetails.getFirstName());
                  }
                  if (employeeDetails.getLastName() != null) {
                      employee.setLastName(employeeDetails.getLastName());
                  }
                  if (employeeDetails.getGender() != null) {
                      employee.setGender( Gender.valueOf( employeeDetails.getGender().toUpperCase() ) );
                  }
                  if (employeeDetails.getAddress() != null) {
                      employee.setAddress(employeeDetails.getAddress());
                  }
                  if (employeeDetails.getEmploymentType() != null) {
                      employee.setType( EmployeeType.valueOf( employeeDetails.getEmploymentType().toUpperCase() ) );
                  }
                  if (employeeDetails.getDepartmentId() != null) {
                      Optional<Department> foundDepartment = departmentService.getDepartmentById(employeeDetails.getDepartmentId());
                      employee.setDepartment(foundDepartment.get());
                  }
              
                  Employee updatedEmployee = employeeService.saveEmployee(employee);
                  return new ResponseEntity<>(new CustomApiDataResponse<Employee>(true, "Employee details updated.", updatedEmployee), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Employee was found with ID: " + id, null), HttpStatus.NOT_FOUND));
    
  }

  /**
   * Delete an employee API.
   *
   * @param id ID of the employee to be deleted
   * @return ResponseEntity containing a CustomApiDataResponse with the copy of deleted employee,
   * a success flag, and a message. Returns status 200 if successful and status 404 if not successful.
   */
  @Operation(summary = "Delete an employee", description = "Delete an employee record by ID")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "Employee deleted"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
      return employeeService.getEmployeeById(id).map(employee -> { 
                employeeService.deleteEmployee(id);
                return new ResponseEntity<>(new CustomApiDataResponse<Employee>(true, "Employee details deleted.", employee), HttpStatus.OK); 
              }).orElseGet(() -> new ResponseEntity<>(new CustomApiDataResponse<>(false, "No Employee was found with ID: " + id, null), HttpStatus.NOT_FOUND));
  }
}
