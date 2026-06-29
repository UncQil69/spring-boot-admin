package com.cbn.admincbn.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "journal", schema = "fin")
@Getter
@Setter
@JsonPropertyOrder({ "id", "docNumber", "trsDate", "companyId", "typeId", "description", "reference", "fileName", "isPosted", "details" })
public class Journal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doc_number")
    private String docNumber;

    @Column(name = "type_id")
    private String typeId; 

    @Column(name = "trs_date")
    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDate trsDate;

    private String description;
    private String reference;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "file_upload") 
    private String fileName;

    @Column(name = "is_posted")
    private Boolean isPosted = false;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<JournalDetail> details = new ArrayList<>();

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}