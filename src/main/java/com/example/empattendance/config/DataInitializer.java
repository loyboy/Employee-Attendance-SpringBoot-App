package com.example.empattendance.config;

import com.example.empattendance.model.Department;
import com.example.empattendance.model.Employee;
import com.example.empattendance.model.EmployeeType;
import com.example.empattendance.model.Gender;
import com.example.empattendance.repository.AttendanceRepository;
import com.example.empattendance.repository.DepartmentRepository;
import com.example.empattendance.repository.EmployeeRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Configuration
public class DataInitializer implements CommandLineRunner {

  @Autowired 
  private DepartmentRepository departmentRepository;

  @Autowired 
  private EmployeeRepository employeeRepository;

  @Autowired 
  private AttendanceRepository attRepository;

  private final Faker faker = new Faker();
  private final Random random = new Random();

  /**
   * This method is called when the application starts.
   *
   * @param args Command line arguments
   */
  @Override
  public void run(String... args) {
    attRepository.deleteAll();
    employeeRepository.deleteAll();
    departmentRepository.deleteAll();
   
    // Create fake departments
    List<Department> departments = new ArrayList<>();
    Set<String> departmentNames = new HashSet<>();
    for (int i = 1; i <= 12; i++) {
      String name = faker.company().industry();
      if (departmentNames.add(name)) {
        Department department = new Department();
        department.setName(name);
        departments.add(department);
      }
    }
    departmentRepository.saveAll(departments);

    // Create fake employees
    List<Employee> employees = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      Employee employee = new Employee();
      employee.setFirstName(faker.name().firstName());
      employee.setLastName(faker.name().lastName());
      Gender[] genders = Gender.values();
      employee.setGender(genders[random.nextInt(genders.length)]);
      EmployeeType[] types = EmployeeType.values();
      employee.setType(types[random.nextInt(types.length)]);      
      employee.setAddress(faker.address().fullAddress());
      employee.setDepartment(departments.get(random.nextInt(departments.size())));
      employees.add(employee);
    }
    employeeRepository.saveAll(employees);

    System.out.println("Fake data initialized successfully, replacing any existing data!");
  }
}
