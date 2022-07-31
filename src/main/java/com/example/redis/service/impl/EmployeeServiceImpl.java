package com.example.redis.service.impl;

import com.example.redis.config.CacheRepository;
import com.example.redis.modal.Employee;
import com.example.redis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    CacheRepository cacheRepository;

    @Override
    public Employee saveEmployeeDetails(Employee employee) {
        if(cacheRepository.addInStringWithExpiry(employee.getEmail(),employee,5L,TimeUnit.MINUTES)) {
          return (Employee) cacheRepository.get(employee.getEmail());
        }
        return null;
    }

    @Override
    public Employee getEmployeeDetails(String email) {
      return (Employee) cacheRepository.get(email);
    }

    @Override
    public void deleteEmployeeDetails(String email) {
    cacheRepository.delete(email);
    }

    @Override
    public void updateEmployeeStatus(String email, boolean isActive) {
        Employee employee= (Employee) cacheRepository.get(email);
//        employee.setActive(isActive);
        cacheRepository.addInSet(employee.getEmail(),employee);
    }

}
