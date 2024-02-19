package com.example.exceptionhandlerapi.controller;

import com.example.exceptionhandlerapi.dao.EmployeeDao;
import com.example.exceptionhandlerapi.entity.Employee;
import com.example.exceptionhandlerapi.exception.IdNotFoundException;
import com.example.exceptionhandlerapi.exception.NoMediaTypeException;
import com.example.exceptionhandlerapi.exception.NotAdminException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @JacksonXmlRootElement
    record EmployeeResponse(int id,
                            @JsonProperty("first_name")
                            String firstName,
                            @JsonProperty("last_name") String lastName,
                            String email,
                            double salary) {
    }

    record EmployeeRequest(@JsonProperty("first_name")
                           @NotBlank(message = "FirstName cannot be blank.")
                           @NotEmpty(message = "FirstName cannot be empty!")
                           @Size(min = 2, max = 16, message = "FirstName must be between 2 and 16")
                           String firstName,
                           @JsonProperty("last_name") String lastName,
                           String email,
                           double salary) {
    }

    private EmployeeResponse toDto(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getSalary()
        );
    }

    private Employee toEntity(EmployeeRequest request) {
        return new Employee(
                request.firstName,
                request.lastName,
                request.email,
                request.salary
        );
    }
    @PostMapping("/create-employee")
    public EmployeeResponse createEmployee(@RequestBody @Valid EmployeeRequest request) {
        if ("admin".equalsIgnoreCase(request.firstName))
        {
            throw new NotAdminException();
        }
        return toDto(employeeDao.save(toEntity(request)));
    }

    @GetMapping("/list-employee")
    public List<EmployeeResponse> listAllEmployee() {
        return employeeDao.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @GetMapping(value = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE,
    MediaType.APPLICATION_XML_VALUE})
    private EmployeeResponse getEmployeeById(@PathVariable("id") int id){
        return employeeDao.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IdNotFoundException(id));
    }
    //1.xml , 1.json
    @GetMapping(value = "/{id}/{type}",produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    private ResponseEntity<EmployeeResponse> getEmployeeByIdXMLORJSON(@PathVariable("id") int id,
                                                    @PathVariable("type") String type){

        EmployeeResponse employeeResponse =  employeeDao.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IdNotFoundException(id));
        if ("xml".equalsIgnoreCase(type)){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .body(employeeResponse);
        }
        else if ("json".equalsIgnoreCase(type)){
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(employeeResponse);
        }
        else
            throw new NoMediaTypeException();
    }
}