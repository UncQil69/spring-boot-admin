package com.cbn.admincbn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bank_account")
@Data
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bankacc_id")
    private Long bankaccId;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "bankacc_name")
    private String bankaccName;

    @Column(name = "bankacc_number")
    private String bankaccNumber;

    @Column(name = "coa_id")
    private Long coaId;

    @Column(name = "coa_code")
    private String coaCode;

    @Column(name = "coa_name")
    private String coaName;

    @Column(name = "acc_label")
    private String accLabel;

    @Column(name = "bank_teller")
    private Long bankTeller; 

    @Column(name = "is_active")
    private Boolean isActive;
}