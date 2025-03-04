package com.example.empattendance.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.*;

import com.example.empattendance.dto.AttendanceExcuseRequest;
import com.example.empattendance.dto.AttendanceRequest;
import com.example.empattendance.dto.CustomApiDataResponse;
import com.example.empattendance.dto.CustomApiResponse;
import com.example.empattendance.exception.BadRequestException;
import com.example.empattendance.exception.ResourceNotFoundException;
import com.example.empattendance.model.AttendanceRecord;
import com.example.empattendance.service.AttendanceService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attendances")
@Tag(name = "Attendance APIs", description = "API Operations related to managing attendance register")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    /**
     * Sign In Attendance API.
     *
     * @param request AttendanceRequest object dto
     * @return ResponseEntity containing a CustomApiDataResponse with the new Attendance Record,
     * a success flag, and a message. Returns HttpStatus.CREATED or 201 status code if successful or HttpStatus.NOT_FOUND is employee not found.
     */
    @Operation(summary = "Sign in an Employee", description = "Sign in")  
    @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Attendance Sign in created"),
        @ApiResponse(responseCode = "404", description = "Employee not found unfortunately."),
      }
    )  
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody AttendanceRequest request) {
        try{
            AttendanceRecord record = attendanceService.signIn(
                request.getEmployeeId(), 
                request.getNotes()
            );
            return new ResponseEntity<>(new CustomApiDataResponse<>(true, "Employee signed in succesfully.", record), HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomApiDataResponse<>(false, "Employee not found unfortunately.", null), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Sign Out Attendance API.
     *
     * @param recordId the Attendance record identifier
     * @param request AttendanceRequest object dto
     * @return ResponseEntity containing a CustomApiDataResponse with the new Attendance Record,
     * a success flag, and a message. Returns HttpStatus.OK or 200 status code if successful or HttpStatus.NOT_FOUND if no employee is found or HttpStatus.BAD_REQUEST if something went wrong.
     */
    @Operation(summary = "Sign out an Employee", description = "Sign out")
    @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Employee signed out succesfully."),
        @ApiResponse(responseCode = "404", description = "Employee not found unfortunately."),
        @ApiResponse(responseCode = "400", description = "Employee could not sign out unfortunately.")
      }
    )
    @PutMapping("/sign-out/{recordId}")
    public ResponseEntity<?> signOut(@PathVariable Long recordId, @Valid @RequestBody AttendanceRequest request) {
        try {
            AttendanceRecord record = attendanceService.signOut(recordId, request.getNotes());
            return new ResponseEntity<>(new CustomApiDataResponse<>(true, "Employee signed out succesfully.", record), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomApiDataResponse<>(false, "Employee not found unfortunately.", null), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(new CustomApiDataResponse<>(false, "Employee could not sign out unfortunately.", null), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Clock in with Sick Leave in Attendance API.
     *
     * @param request AttendanceRequest object dto
     * @return ResponseEntity containing a CustomApiDataResponse with the new Attendance Record,
     * a success flag, and a message. Returns HttpStatus.OK or 200 status code if successful or HttpStatus.NOT_FOUND if no employee is found.
     */
    @Operation(summary = "Register Sick Leave for an Employee", description = "Sick leave registration")
    @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Employee attendance registered with Sick leave excuse."),
        @ApiResponse(responseCode = "404", description = "Employee not found unfortunately.")
      }
    )
    @PostMapping("/sick-leave")
    public ResponseEntity<?> reportSickLeave(@Valid @RequestBody AttendanceExcuseRequest request) {
        try {
            AttendanceRecord record = attendanceService.recordSickLeave(
                request.getEmployeeId(),
                request.getDate(),
                request.getNotes() != null ? request.getNotes() : ""
            );
            return new ResponseEntity<>(new CustomApiDataResponse<>(true, "Employee registered with Sick leave excuse, succesfully.", record), HttpStatus.OK);
        }
        catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomApiDataResponse<>(false, "Employee not found unfortunately.", null), HttpStatus.NOT_FOUND);
        }
    }
    
     /**
     * Clock in with Absence in Attendance API.
     *
     * @param request AttendanceExcuseRequest object dto
     * @return ResponseEntity containing a CustomApiDataResponse with the absence Attendance Record,
     * a success flag, and a message. Returns HttpStatus.OK or 200 status code if successful or HttpStatus.NOT_FOUND if no employee is found.
     */
    @Operation(summary = "Register Absence for an Employee", description = "Absence registration")
    @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Employee attendance registered with Absent excuse."),
        @ApiResponse(responseCode = "404", description = "Employee not found unfortunately."),
      }
    )
    @PostMapping("/absence")
    public ResponseEntity<?> reportAbsence(@Valid @RequestBody AttendanceExcuseRequest request) {
        try {
            AttendanceRecord record = attendanceService.recordAbsence(
                request.getEmployeeId(),
                request.getDate(),
                request.getNotes() != null ? request.getNotes() : ""
            );
            return new ResponseEntity<>(new CustomApiDataResponse<>(true, "Employee registered with Absent excuse, succesfully.", record), HttpStatus.CREATED);
        }
        catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomApiDataResponse<>(false, "Employee not found unfortunately.", null), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieve Employee Attendance date range Attendance API.
     *
     * @param employeeId Employee ID (path variable)
     * @param startDate Start date 
     * @return ResponseEntity containing a CustomApiDataResponse with the absence Attendance Record,
     * a success flag, and a message. Returns HttpStatus.OK or 200 status code if successful or HttpStatus.NOT_FOUND if no employee is found or HttpStatus.BAD_REQUEST if start date is greater than end date.
     */
    @Operation(summary = " Retrieve Employee Attendance date range", description = "Attendance viewing date range")
    @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Employee attendance retrieved."),
        @ApiResponse(responseCode = "404", description = "Employee not found unfortunately."),
      }
    )
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployeeAttendanceReport(@PathVariable Long employeeId, @RequestParam(required = false) String startDate,@RequestParam(required = false) String endDate) {       
        LocalDate startDateVal = null;
        LocalDate endDateVal = null;
        if (startDate != null && !startDate.isEmpty()) {
            try {
                startDateVal = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException e) {
                return new ResponseEntity<>(new CustomApiResponse(false, "StartDate should be a valid LocalDate in ISO format (YYYY-MM-DD). "), HttpStatus.BAD_REQUEST);
            }
        }
    
        if (endDate != null && !endDate.isEmpty()) {
            try {
                endDateVal = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException e) {
                return new ResponseEntity<>(new CustomApiResponse(false, "EndDate should be a valid LocalDate in ISO format (YYYY-MM-DD). "), HttpStatus.BAD_REQUEST);
            }
        }

        try{
            List<AttendanceRecord> records = attendanceService.getAttendanceRecordsByEmployeeAndDateRange(
                employeeId, 
                startDateVal, 
                endDateVal
            );
            return new ResponseEntity<>(new CustomApiDataResponse<>(true, "Employee attendance retrieved succesfully.", records), HttpStatus.CREATED);
        }
        catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new CustomApiDataResponse<>(false, "Employee not found unfortunately.", null), HttpStatus.NOT_FOUND);
        }
        catch (BadRequestException e) {
            return new ResponseEntity<>(new CustomApiResponse(false, "Start date must be before or equal to end date."), HttpStatus.BAD_REQUEST);
        }
    }
}
