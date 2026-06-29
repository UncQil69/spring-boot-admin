package com.cbn.admincbn.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cbn.admincbn.entity.Coa;
import com.cbn.admincbn.entity.Journal;
import com.cbn.admincbn.entity.JournalDetail;
import com.cbn.admincbn.entity.Subledger;
import com.cbn.admincbn.repository.JournalRepository;
import com.dto.JournalRequestDTO;
import com.dto.JournalSearchRequest;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralJournalService {

    private final JournalRepository journalRepository;
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public List<Journal> getJournalList(int year, String companyId) {
        return journalRepository.findByYearAndCompanyId(year, companyId);
    }

    public Page<Journal> getAllJournalsWithPagination(String companyId, int page, int size) {
        if (size < 1) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("trsDate").descending().and(Sort.by("id").descending()));
        if (companyId != null && !companyId.isEmpty()) {
            return journalRepository.findAllByCompanyId(companyId, pageable);
        }
        return journalRepository.findAll(pageable);
    }

    public Page<Journal> searchJournalsWithPagination(JournalSearchRequest request, int page, int size) {
        if (size < 1) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("trsDate").descending().and(Sort.by("id").descending()));

        return journalRepository.findAll((Specification<Journal>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            if (request.getCompanyId() != null && !request.getCompanyId().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("companyId"), request.getCompanyId()));
            }

            if (request.getYear() != null) {
                predicates.add(criteriaBuilder.equal(
                    criteriaBuilder.function("to_char", String.class, root.get("trsDate"), criteriaBuilder.literal("YYYY")),
                    String.valueOf(request.getYear())
                ));
            }

            if (request.getDocNumber() != null && !request.getDocNumber().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("docNumber")), 
                    "%" + request.getDocNumber().toLowerCase() + "%"
                ));
            }

            if (request.getDate() != null) {
                predicates.add(criteriaBuilder.equal(root.get("trsDate"), request.getDate()));
            }

            if (request.getDescription() != null && !request.getDescription().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), 
                    "%" + request.getDescription().toLowerCase() + "%"
                ));
            }

            if (request.getCoaId() != null || request.getSubledgerId() != null) {
                Join<Journal, JournalDetail> detailsJoin = root.join("details");
                predicates.add(criteriaBuilder.equal(detailsJoin.get("isDeleted"), false));

                if (request.getCoaId() != null) {
                    Join<JournalDetail, Coa> coaJoin = detailsJoin.join("coa"); 
                    predicates.add(criteriaBuilder.equal(coaJoin.get("id"), request.getCoaId()));
                }

                if (request.getSubledgerId() != null) {
                    Join<JournalDetail, Subledger> subledgerJoin = detailsJoin.join("subledger");
                    predicates.add(criteriaBuilder.equal(subledgerJoin.get("id"), request.getSubledgerId()));
                }
                query.distinct(true);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }

    @Transactional
    public void createGeneralJournal(JournalRequestDTO data, MultipartFile file) {
        String fileName = null;
        
        if (file != null && !file.isEmpty()) {
            fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            try {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
                );
                log.info("Berhasil upload berkas ke MinIO dengan nama: {}", fileName);
            } catch (Exception e) {
                log.error("Gagal melakukan upload berkas ke MinIO", e);
                throw new RuntimeException("Gagal upload ke MinIO: " + e.getMessage());
            }
        }

        try {
            saveToDatabase(data, fileName);
            log.info("Berhasil menyimpan data jurnal umum ke database PostgreSQL.");
        } catch (Exception e) {
            log.error("CRITICAL ERROR: Gagal menyimpan data jurnal ke PostgreSQL. Error: ", e);
            throw new RuntimeException("Gagal menyimpan ke database: " + e.getMessage());
        }
    }

    public void saveToDatabase(JournalRequestDTO data, String fileName) {
        Journal journal = new Journal();
        
        journal.setDocNumber("JV-" + System.currentTimeMillis());

        journal.setCompanyId(data.getCompanyId());
        journal.setTrsDate(LocalDate.now()); 
        journal.setDescription(data.getDescription());
        journal.setReference(data.getReference());
        journal.setTypeId(data.getTypeId());
        journal.setIsDeleted(false);
        journal.setIsPosted(false);
        
        if (fileName != null) {
            journal.setFileName(fileName);
        }

        List<JournalDetail> details = new ArrayList<>();
        if (data.getDetails() != null) {
            for (JournalRequestDTO.JournalDetailDTO d : data.getDetails()) {
                JournalDetail detail = new JournalDetail();
                detail.setJournal(journal);
                detail.setCompanyId(data.getCompanyId()); 
                
                detail.setCoaId(d.getCoaId()); 
                detail.setSubledgerId(d.getSubledgerId()); 
                
                if (d.getCoaId() != null) {
                    detail.setCoaCode(String.valueOf(d.getCoaId()));
                } else {
                    detail.setCoaCode("UNKNOWN");
                }
                
                if (d.getNote() != null && !d.getNote().isEmpty()) {
                    detail.setCoaName(d.getNote());
                } else {
                    detail.setCoaName("JURNAL DETAIL COA");
                }
                
                detail.setNote(d.getNote());
                detail.setDebit(d.getDebit() != null ? BigDecimal.valueOf(d.getDebit()) : BigDecimal.ZERO);
                detail.setCredit(d.getCredit() != null ? BigDecimal.valueOf(d.getCredit()) : BigDecimal.ZERO);
                detail.setIsDeleted(false);
                details.add(detail);
            }
        }
        
        journal.setDetails(details);
        journalRepository.save(journal);
    }

    public byte[] exportToExcel(int year, String companyId) {
        List<Journal> journals = journalRepository.findByYearAndCompanyId(year, companyId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Journal_" + year);
            Row headerRow = sheet.createRow(0);
            String[] columns = {"DATE", "DESCRIPTION", "COA", "SUBLEDGER", "DEBIT", "CREDIT"};
            for (int i = 0; i < columns.length; i++) {
                headerRow.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (Journal j : journals) {
                if (j.getDetails() != null) {
                    for (JournalDetail det : j.getDetails()) {
                        Row row = sheet.createRow(rowIdx++);
                        row.createCell(0).setCellValue(j.getTrsDate() != null ? j.getTrsDate().toString() : "");
                        row.createCell(1).setCellValue(det.getNote() != null ? det.getNote() : j.getDescription());
                        row.createCell(2).setCellValue(det.getCoaId() != null ? det.getCoaId().doubleValue() : 0.0);
                        row.createCell(3).setCellValue(det.getSubledgerId() != null ? det.getSubledgerId().doubleValue() : 0.0);
                        row.createCell(4).setCellValue(det.getDebit() != null ? det.getDebit().doubleValue() : 0.0);
                        row.createCell(5).setCellValue(det.getCredit() != null ? det.getCredit().doubleValue() : 0.0);
                    }
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Gagal export excel: " + e.getMessage());
        }
    }

    public Resource downloadFileFromMinio(String fileName) {
        try {
            InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()
            );
            return new InputStreamResource(stream);
        } catch (Exception e) {
            log.error("Gagal mengambil file dari MinIO: {}", fileName, e);
            throw new RuntimeException("Berkas tidak ditemukan di storage server: " + e.getMessage());
        }
    }
}