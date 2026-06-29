package com.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JournalRequestDTO {
    private String description;
    private String reference;
    private String typeId;
    private String companyId;
    private List<JournalDetailDTO> details;

    @Getter
    @Setter
    public static class JournalDetailDTO {
        private Integer coaId;       
        private Integer subledgerId; 
        private String note;
        private Double debit;
        private Double credit;
    }
}