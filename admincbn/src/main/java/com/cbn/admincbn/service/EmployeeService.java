package com.cbn.admincbn.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cbn.admincbn.entity.Employee;
import com.cbn.admincbn.repository.EmployeeRepository;
import com.dto.EmployeeRequestDTO;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    
    public Employee saveEmployee(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployeeByName(String name, EmployeeRequestDTO dto) {
        Optional<Employee> existingEmployee = employeeRepository.findByNamaKaryawan(name);
        
        if (existingEmployee.isPresent()) {
            Employee employeeToUpdate = existingEmployee.get();
            
            Long currentId = employeeToUpdate.getPerson_id();
            
            BeanUtils.copyProperties(dto, employeeToUpdate);
            
            employeeToUpdate.setPerson_id(currentId);
            
            return employeeRepository.save(employeeToUpdate);
        } else {
            throw new RuntimeException("Karyawan dengan nama '" + name + "' tidak ditemukan");
        }
    }

    public List<Employee> getEmployeesByFilter(Long personId, String personName, String email) {
        return employeeRepository.findByFilter(personId, personName, email);
    }

    public Page<Employee> findAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }
}