package com.cbn.admincbn.entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "journal")
@Getter 
@Setter
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer companyId;
    private Integer typeId;
    private String docNumber;
    private LocalDate trsDate;
    private String description;
    private String reference;
    private Boolean isPosted = false;
    private String status;
    private LocalDateTime postedDate;
    private Integer postedUserId;
    private String postedUserName;
    private Integer bankaccId;
    private String bankaccName;
    private String bankaccNumber;
    private BigDecimal debit;
    private BigDecimal credit;
    private String pathFileUpload;
    private String fileUpload;
    
    @Column(updatable = false)
    private String createdBy;
    private LocalDateTime createdTime = LocalDateTime.now();
    
    private String updatedBy;
    private LocalDateTime updatedTime;
    
    private Boolean isDeleted = false;
    private String deletedBy;
    private LocalDateTime deletedTime;
    
    private String uniqId = UUID.randomUUID().toString();
}