package com.example.empattendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Employee Attendance Register System API",
        version = "1.0.0",
        description = "API documentation for managing employees attendance within a medical institution",
        contact = @Contact(
            name = "Employee Attendance Register",
            email = "onehubdigita@gmail.com",
            url = ""
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        ),
        termsOfService = ""
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT Authorization header using the Bearer scheme"
)
public class EmployeeAttendanceManagementApplication {

  /**
   * The main entry point of this application.
   *
   * @param args Command-line arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(EmployeeAttendanceManagementApplication.class, args);
  }

  /**
   * Configure OpenAPI with JWT Bearer authentication.
   *
   * @return OpenAPI custom configuration.
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        // Add security requirement globally
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
            // Fully qualified name for SecurityScheme to avoid name conflict
            .addSecuritySchemes("bearerAuth",
                new io.swagger.v3.oas.models.security.SecurityScheme()
                    .name("bearerAuth")
                    .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }
}
