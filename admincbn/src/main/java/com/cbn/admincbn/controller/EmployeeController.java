package com.cbn.admincbn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbn.admincbn.entity.Employee;
import com.cbn.admincbn.service.EmployeeService;
import com.dto.EmployeeRequestDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/employee")
@Tag(name = "Employee Management", description = "Endpoint untuk mengelola data karyawan")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService; 

    @PostMapping("/add")
    @Operation(summary = "Tambah Karyawan Baru", description = "Menyimpan data karyawan ke database hrm.employee")
    public ResponseEntity<Map<String, Object>> addEmployee(@RequestBody EmployeeRequestDTO request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Employee savedEmployee = employeeService.saveEmployee(request);

            response.put("status", "success");
            response.put("message", "Data karyawan berhasil ditambahkan");
            response.put("person_id", savedEmployee.getPerson_id()); 

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Gagal menambahkan data: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter Data Employee", description = "Mencari karyawan berdasarkan person_id, person_name, atau email")
    public ResponseEntity<Map<String, Object>> filterEmployees(
            @RequestParam(required = false) Long personId,
            @RequestParam(required = false) String personName,
            @RequestParam(required = false) String email) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            List<Employee> data = employeeService.getEmployeesByFilter(personId, personName, email);
            
            response.put("status", "success");
            response.put("total_data", data.size());
            response.put("data", data);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Gagal memfilter data: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Tampilkan Semua Karyawan", description = "Mengambil semua data karyawan yang ada")
    public ResponseEntity<Map<String, Object>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Employee> employeePage = employeeService.findAllEmployees(pageable);
            
            response.put("status", "success");
            response.put("data", employeePage.getContent()); 
            
            Map<String, Object> pagination = new HashMap<>();
            pagination.put("current_page", employeePage.getNumber()); 
            pagination.put("display_page", employeePage.getNumber() + 1); 
            pagination.put("total_items", employeePage.getTotalElements());
            pagination.put("total_pages", employeePage.getTotalPages()); 
            pagination.put("size", employeePage.getSize()); 
            pagination.put("is_first", employeePage.isFirst());
            pagination.put("is_last", employeePage.isLast());
            
            response.put("pagination", pagination);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Gagal mengambil data: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit")
    @Operation(summary = "Edit Employee berdasarkan Nama", description = "Mengubah data karyawan berdasarkan nama")
    public ResponseEntity<Map<String, Object>> updateEmployee(
            @RequestParam String targetName, 
            @RequestBody EmployeeRequestDTO request) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            Employee updatedEmployee = employeeService.updateEmployeeByName(targetName, request);

            response.put("status", "success");
            response.put("message", "Data karyawan '" + targetName + "' berhasil diupdate");
            response.put("data", updatedEmployee);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Gagal mengupdate data: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}