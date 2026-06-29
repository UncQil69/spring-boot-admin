package com.cbn.admincbn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subledger", schema = "fin")
@Getter
@Setter
public class Subledger {

    @Id
    private Integer id;

    @Column(name = "subledger_name")
    private String subledgerName;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}