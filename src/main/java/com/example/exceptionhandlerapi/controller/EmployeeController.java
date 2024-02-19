package com.example.exceptionhandlerapi.controller;

import com.example.exceptionhandlerapi.dao.EmployeeDao;
import com.example.exceptionhandlerapi.entity.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeDao employeeDao;

    @GetMapping("/creation")
    public String creation() {
        List.of(
                        new Employee("John", "Doe", "john@gmail.com", 2000),
                        new Employee("Thomas", "Hardy", "thomas@gmail.com", 2000)
                )
                .forEach(employeeDao::save);
        return "Successfully created";
    }

record EmployeeResponse(int id,
                        @JsonProperty("first_name")
                        String firstName,
                        @JsonProperty("last_name") String lastName,
                        String email,
                        double salary) {}

record EmployeeRequest(@JsonProperty("first_name")
                       @NotBlank(message = "FirstName cannot be blank.")
                       @NotEmpty(message = "FirstName cannot be empty!")
                       @Size(min = 2,max = 16,message = "FirstName must be between 2 and 16")
                       String firstName,
                       @JsonProperty("last_name") String lastName,
                       String email,
                       double salary) {}

private EmployeeResponse toDto(Employee employee){
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getSalary()
        );
}
private Employee toEntity(EmployeeRequest request){
        return new Employee(
                request.firstName,
                request.lastName,
                request.email,
                request.salary
        );
}
@PostMapping("/create-employee")
    public EmployeeResponse createEmployee(@RequestBody @Valid EmployeeRequest request) {
        return toDto(employeeDao.save(toEntity(request)));
    }
}