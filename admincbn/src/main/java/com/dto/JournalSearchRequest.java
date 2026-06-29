package com.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalSearchRequest {
    private Integer year;
    private String docNumber;
    private LocalDate date;
    private String description;
    private Integer subledgerId;
    private Integer coaId;
    private String companyId;
}