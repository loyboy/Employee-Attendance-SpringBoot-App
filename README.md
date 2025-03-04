# Employee Attendance Management System - Backend

## Overview

The backend of the Employee Attendance Management System is built using Spring Boot. This backend provides a RESTful API for managing employee and department and attendance data, connecting to a basic MySQL database.

## Features

- **Data Initialization**: Includes sample data for departments and employees.
- **RESTful API**: Provides endpoints for CRUD operations on employees and departments.
- **Integration**: Connects to a MySQL database.
- **Exception Handling**: Custom error handling.

## Technologies

- **Spring Boot**: Framework for building production-ready applications with Java.
- **MySQL**: Relational database for structured data storage.

## Setup Instructions

#### Important: Java 17 is required to run this project.

### 1. Clone the Repository

```bash
git clone https://github.com/loyboy/Employee-Attendance-SpringBoot-App.git
cd Employee-Attendance-SpringBoot-App
```

### 2. Install Dependencies

Ensure you have [Maven](https://maven.apache.org/) and [Java JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) installed. Run the following command to install the required dependencies:

```bash
mvn install -DskipTests
```

### 3. Configure the Application

Update `src/main/resources/application.properties` with your database configuration:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/employee_xxx
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

```

Ensure the databases are set up as expected and the URLs, usernames, and passwords match your local or remote database setup.

### 4. Start the Backend Server

Run the following command to start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The backend server will be available at [http://localhost:8085](http://localhost:8085).

### 5. Access the API Endpoints

Here are some example API endpoints you can use to interact with the backend:

- **Get All Employees:**

  ```bash
  curl -X GET http://localhost:8085/api/employees
  ```

- **Get Employee by ID:**

  ```bash
  curl -X GET http://localhost:8085/api/employees/1
  ```

### 6. Data Initialization

The `DataInitializer.java` class is used to preload sample data into the database. This is particularly useful for development and testing.

### 7. Running Tests

[TODO] Still going to be done.

## Swagger API Documentation

The backend API is documented using Swagger, which provides a user-friendly interface for exploring the available endpoints. To access the Swagger UI, navigate to [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html) after starting the backend server.

If you have everything set up correctly, you should see the Swagger UI with a list of available endpoints and the ability to test them directly from the browser.


## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## More Information

For more information about this project, please refer to the comprehensive [documentation](../README.md).

## Contact

For any questions or issues, please contact [onehubdigita@gmail.com](mailto:onehubdigita@gmail.com).

---
