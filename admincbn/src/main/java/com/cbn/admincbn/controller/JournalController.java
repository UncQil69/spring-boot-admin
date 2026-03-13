package com.cbn.admincbn.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbn.admincbn.entity.Journal;
import com.cbn.admincbn.repository.JournalRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/journal")
@Tag(name = "Penambahan Jurnal", description = "Endpoint untuk pengelolaan jurnal keuangan")
public class JournalController {

    @Autowired
    private JournalRepository journalRepository;

    @Operation(summary = "Tambah Jurnal Baru", description = "Menyimpan data pelaporan keuangan ke tabel journal")
    @PostMapping("/add")
    public ResponseEntity<Journal> createJournal(@RequestBody Journal journal) {
        Journal savedJournal = journalRepository.save(journal);
        return new ResponseEntity<>(savedJournal, HttpStatus.CREATED);
    }
}