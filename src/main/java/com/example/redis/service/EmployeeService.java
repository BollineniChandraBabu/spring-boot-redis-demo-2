package com.example.redis.service;

import com.example.redis.modal.Employee;

public interface EmployeeService {
    Employee saveEmployeeDetails(Employee employee);
    Employee getEmployeeDetails(String email);
    void deleteEmployeeDetails(String email);
    void updateEmployeeStatus(String email,boolean isActive);
}
