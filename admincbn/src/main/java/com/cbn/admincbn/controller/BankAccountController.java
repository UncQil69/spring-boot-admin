package com.cbn.admincbn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cbn.admincbn.entity.BankAccount;
import com.cbn.admincbn.service.BankAccountService;
import com.dto.BankAccountRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/bank-accounts")
@Tag(name = "Bank Account Management", description = "Endpoint untuk Manajemen Rekening Bank")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Operation(summary = "Ambil semua rekening bank yang ada")
    @GetMapping
    public Map<String, Object> getAll() {
        List<BankAccount> listBank = bankAccountService.getAllAccounts();
        return createResponse("Data berhasil diambil", listBank);
    }

    @Operation(summary = "Tambah rekening bank baru")
    @PostMapping
    public Map<String, Object> save(@RequestBody BankAccountRequest request) {
        BankAccount savedData = bankAccountService.saveAccount(request);
        return createResponse("Rekening bank berhasil ditambahkan", savedData);
    }

    @Operation(summary = "Edit rekening bank berdasarkan ID atau Nama Rekening Lama")
    @PutMapping("/update")
    public Map<String, Object> update(
            @Parameter(description = "ID rekening yang ingin diupdate") 
            @RequestParam(required = false) Long id,
            
            @Parameter(description = "Nama rekening lama jika ID tidak tahu") 
            @RequestParam(required = false) String searchName,
            
            @RequestBody BankAccountRequest request) {
        
        BankAccount updated;

        if (id != null) {
            updated = bankAccountService.updateById(id, request);
        } else if (searchName != null && !searchName.isEmpty()) {
            updated = bankAccountService.updateByName(searchName, request);
        } else {
            throw new RuntimeException("Mohon masukkan ID atau Nama Rekening lama untuk melakukan update");
        }

        return createResponse("Data berhasil diperbarui", updated);
    }

    @Operation(summary = "Hapus rekening bank berdasarkan ID atau Nama Rekening")
    @DeleteMapping("/delete")
    public Map<String, Object> delete(
            @Parameter(description = "ID rekening yang ingin dihapus") 
            @RequestParam(required = false) Long id,
            
            @Parameter(description = "Nama rekening yang ingin dihapus") 
            @RequestParam(required = false) String bankaccName) {
        
        if (id != null) {
            bankAccountService.deleteById(id);
        } else if (bankaccName != null && !bankaccName.isEmpty()) {
            bankAccountService.deleteByName(bankaccName);
        } else {
            throw new RuntimeException("Mohon masukkan ID atau Nama Rekening untuk menghapus data");
        }
        
        return createResponse("Rekening bank berhasil dihapus", null);
    }

    private Map<String, Object> createResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}