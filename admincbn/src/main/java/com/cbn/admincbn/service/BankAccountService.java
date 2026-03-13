package com.cbn.admincbn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbn.admincbn.entity.BankAccount;
import com.cbn.admincbn.repository.BankAccountRepository;
import com.dto.BankAccountRequest; 

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Transactional(readOnly = true)
    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

    @Transactional
    public BankAccount saveAccount(BankAccountRequest request) {
        BankAccount bank = new BankAccount();
        return mapAndSave(bank, request);
    }

    @Transactional
    public BankAccount updateById(Long id, BankAccountRequest request) {
        BankAccount bank = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID tidak ditemukan"));
        return mapAndSave(bank, request);
    }

    @Transactional
    public BankAccount updateByName(String name, BankAccountRequest request) {
        BankAccount bank = bankAccountRepository.findByBankaccName(name)
                .orElseThrow(() -> new RuntimeException("Rekening bank '" + name + "' tidak ditemukan"));
        return mapAndSave(bank, request);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new RuntimeException("ID tidak ditemukan");
        }
        bankAccountRepository.deleteById(id);
    }

    @Transactional
    public void deleteByName(String name) {
        BankAccount bank = bankAccountRepository.findByBankaccName(name)
                .orElseThrow(() -> new RuntimeException("Rekening bank '" + name + "' tidak ditemukan"));
        
        bankAccountRepository.delete(bank);
    }

    private BankAccount mapAndSave(BankAccount bank, BankAccountRequest request) {
        bank.setBankaccName(request.getBankaccName());
        bank.setBankaccNumber(request.getBankaccNumber());
        bank.setCompanyId(request.getCompanyId());
        bank.setCoaId(request.getCoaId());
        bank.setCoaCode(request.getCoaCode());
        bank.setCoaName(request.getCoaName());
        bank.setAccLabel(request.getAccLabel());
        bank.setBankTeller(request.getBankTeller());
        bank.setIsActive(request.getIsActive());
        
        return bankAccountRepository.save(bank);
    }
}