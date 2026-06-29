package com.cbn.admincbn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coa", schema = "fin")
@Getter
@Setter
public class Coa {

    @Id
    private Integer id;

    @Column(name = "coa_code")
    private String coaCode;

    @Column(name = "coa_name")
    private String coaName;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}