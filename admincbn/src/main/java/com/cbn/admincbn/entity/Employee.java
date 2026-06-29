package com.cbn.admincbn.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employee", schema = "hrm")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long person_id;

    @Column(length = 4)
    private String company_id;

    @Column(length = 50, nullable = false)
    private String employee_id;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String person_name;

    @Column(columnDefinition = "varchar(50)")
    private String email;

    @Column(length = 20)
    private String phone;

    private LocalDate date_of_birth;

    @Column(length = 255)
    private String town_of_birth;

    @Column(columnDefinition = "text")
    private String address;

    @Column(name = "is_active")
    private Boolean is_active = true;

    // PSA Information
    private Integer psa_id;
    
    @Column(length = 4)
    private String psa_code;
    
    @Column(length = 255)
    private String psa_name;

    // Job Function Information
    private Integer jobfunction_id;
    
    @Column(length = 6)
    private String jobfunction_code;
    
    @Column(length = 255)
    private String jobfunction_name;

    private LocalDate date_of_hire;
    private LocalDate date_of_work;
    private LocalDate date_of_retire;

    // Audit Fields
    private Integer created_by;

    @Column(name = "created_time", insertable = false, updatable = false)
    private LocalDateTime created_time;

    private Integer updated_by;

    @Column(name = "updated_time", insertable = false)
    private LocalDateTime updated_time;

    private Integer deleted_by;

    @Column(name = "is_deleted")
    private Boolean is_deleted = false;

    @Column(name = "deleted_time")
    private LocalDateTime deleted_time;
}