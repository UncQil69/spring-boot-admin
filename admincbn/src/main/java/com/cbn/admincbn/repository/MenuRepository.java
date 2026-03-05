package com.cbn.admincbn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cbn.admincbn.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<Menu> findByTitle(String title);

    boolean existsByTitle(String title);

    @Transactional
    @Modifying
    void deleteByTitle(String title);
}