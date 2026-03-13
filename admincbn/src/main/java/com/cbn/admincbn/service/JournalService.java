package com.cbn.admincbn.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cbn.admincbn.entity.Journal;
import com.cbn.admincbn.repository.JournalRepository;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Transactional
    public Journal saveJournal(Journal journal) {
        if (journal.getStatus() == null) {
            journal.setStatus("DRAFT");
        }
        
        return journalRepository.save(journal);
    }
}