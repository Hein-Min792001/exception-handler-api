package com.example.exceptionhandlerapi.dao;

import com.example.exceptionhandlerapi.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDao extends JpaRepository<Employee,Integer> {
}
