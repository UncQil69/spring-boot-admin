package com.cbn.admincbn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbn.admincbn.entity.Journal;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
}