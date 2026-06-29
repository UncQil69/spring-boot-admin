package com.cbn.admincbn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbn.admincbn.entity.Journal;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long>, JpaSpecificationExecutor<Journal> {

    @Query("SELECT j FROM Journal j JOIN FETCH j.details " +
           "WHERE EXTRACT(YEAR FROM j.trsDate) = :year " +
           "AND j.companyId = :companyId " +
           "AND j.isDeleted = false")
    List<Journal> findByYearAndCompanyId(@Param("year") int year, @Param("companyId") String companyId);

    @Query(
        value = "SELECT DISTINCT j FROM Journal j JOIN FETCH j.details WHERE j.companyId = :companyId AND j.isDeleted = false",
        countQuery = "SELECT COUNT(j) FROM Journal j WHERE j.companyId = :companyId AND j.isDeleted = false"
    )
    Page<Journal> findAllByCompanyId(@Param("companyId") String companyId, Pageable pageable);
}