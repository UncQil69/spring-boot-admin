package com.cbn.admincbn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbn.admincbn.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.person_name = :personName")
    Optional<Employee> findByNamaKaryawan(@Param("personName") String personName);

    @Query(value = "SELECT * FROM hrm.employee e WHERE " +
           "(:personId IS NULL OR e.person_id = :personId) AND " +
           "(:personName IS NULL OR LOWER(CAST(e.person_name AS text)) LIKE LOWER(CONCAT('%', CAST(:personName AS text), '%'))) AND " +
           "(:email IS NULL OR LOWER(CAST(e.email AS text)) LIKE LOWER(CONCAT('%', CAST(:email AS text), '%')))", 
           nativeQuery = true)
    List<Employee> findByFilter(@Param("personId") Long personId, 
                                @Param("personName") String personName, 
                                @Param("email") String email);
}