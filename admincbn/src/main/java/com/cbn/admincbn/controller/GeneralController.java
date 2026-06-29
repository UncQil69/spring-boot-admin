package com.cbn.admincbn.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cbn.admincbn.entity.Journal;
import com.cbn.admincbn.service.GeneralJournalService;
import com.dto.JournalRequestDTO;
import com.dto.JournalSearchRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/general")
@Tag(name = "General Journal", description = "Fitur Jurnal Umum (Input, View, Export)")
public class GeneralController {

    private final GeneralJournalService journalService;

    public GeneralController(GeneralJournalService journalService) {
        this.journalService = journalService;
    }

    @Operation(summary = "Melihat Daftar Jurnal per Tahun")
    @GetMapping("/list")
    public ResponseEntity<List<Journal>> getListByYear(
            @RequestParam int year, 
            @RequestParam(defaultValue = "COMP01") String companyId) {
        List<Journal> journals = journalService.getJournalList(year, companyId);
        return ResponseEntity.ok(journals);
    }

    @Operation(
        summary = "Melihat Semua Jurnal Yang Ada", 
        description = "Mengambil data seluruh jurnal yang ada."
    )
    @GetMapping("/page")
    public ResponseEntity<Page<Journal>> getAllJournalsWithPage(
            @RequestParam(required = false) String companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<Journal> journalPage = journalService.getAllJournalsWithPagination(companyId, page, size);
        return ResponseEntity.ok(journalPage);
    }

    @Operation(
        summary = "Tambah Jurnal Baru (General)", 
        description = "Menambahkan jurnal umum baru dengan opsi upload file ke MinIO storage"
    )
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @RequestPart("data") 
            @Parameter(description = "Data Jurnal dalam format JSON String", schema = @Schema(type = "string", format = "json", implementation = JournalRequestDTO.class)) 
            String dataJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JournalRequestDTO data = objectMapper.readValue(dataJson, JournalRequestDTO.class);
            
            journalService.createGeneralJournal(data, file);
            return ResponseEntity.ok("Journal Berhasil Dibuat dan File Terkirim ke MinIO");
            
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Format JSON salah: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Gagal memproses data: " + e.getMessage());
        }
    }

    @Operation(summary = "Export Jurnal ke Excel")
    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam int year, @RequestParam(defaultValue = "COMP01") String companyId) {
        byte[] content = journalService.exportToExcel(year, companyId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=journal_" + year + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(content);
    }

    @Operation(
        summary = "Pencarian Jurnal Dinamis", 
        description = "Mencari data jurnal."
    )
    @PostMapping("/search")
    public ResponseEntity<Page<Journal>> searchJournals(
            @org.springdoc.core.annotations.ParameterObject @ModelAttribute JournalSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Journal> results = journalService.searchJournalsWithPagination(request, page, size);
        return ResponseEntity.ok(results);
    }

    @Operation(
        summary = "Download File Lampiran Jurnal dari MinIO", 
        description = "Mengunduh berkas fisik bukti transaksi yang tersimpan di MinIO."
    )
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Nama file lengkap yang tersimpan di kolom database (e.g. UUID_bukti.pdf)", required = true)
            @RequestParam("fileName") String fileName) {
        
        Resource fileResource = journalService.downloadFileFromMinio(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileResource);
    }
}