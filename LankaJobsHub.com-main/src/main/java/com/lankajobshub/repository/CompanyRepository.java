package com.lankajobshub.repository;

import com.lankajobshub.model.Company;
import com.lankajobshub.model.Company.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    
    Optional<Company> findByName(String name);
    
    List<Company> findByStatus(CompanyStatus status);
    
    @Query("SELECT c FROM Company c WHERE c.name LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Company> searchCompanies(@Param("keyword") String keyword);
    
    boolean existsByName(String name);
}
