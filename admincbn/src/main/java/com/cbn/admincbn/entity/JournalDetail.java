package com.cbn.admincbn.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "journal_detail", schema = "fin")
@Getter
@Setter
public class JournalDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false)
    @JsonBackReference 
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coa_id", insertable = false, updatable = false)
    private Coa coa;

    @Column(name = "coa_id")
    private Integer coaId;

    @Column(name = "coa_code", nullable = false)
    private String coaCode;

    @Column(name = "coa_name", nullable = false)
    private String coaName;

    @jakarta.persistence.Transient
    private String subledgerCode;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subledger_id", insertable = false, updatable = false)
    private Subledger subledger;

    @Column(name = "subledger_id")
    private Integer subledgerId; 

    private String note;

    @Column(precision = 18, scale = 2)
    private BigDecimal debit;

    @Column(precision = 18, scale = 2)
    private BigDecimal credit;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    public void setCoaId(Integer coaId) {
        this.coaId = coaId;
    }

    public void setSubledgerId(Integer subledgerId) {
        this.subledgerId = subledgerId;
    }
}