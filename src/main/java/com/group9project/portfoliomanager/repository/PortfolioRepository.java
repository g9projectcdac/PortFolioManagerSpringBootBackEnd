package com.group9project.portfoliomanager.repository;

import com.group9project.portfoliomanager.model.Portfolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {


    @Query("SELECT p FROM Portfolio p WHERE " +
            "p.name LIKE CONCAT('%', :query, '%') " +
            "OR p.location LIKE CONCAT('%', :query, '%') " +
            "ORDER BY p.name ASC")
    Page<Portfolio> searchPortfoliosAscending(String query, Pageable pageable);

    @Query("SELECT p FROM Portfolio p WHERE " +
            "p.name LIKE CONCAT('%', :query, '%') " +
            "OR p.location LIKE CONCAT('%', :query, '%') " +
            "ORDER BY p.name DESC")
    Page<Portfolio> searchPortfoliosDescending(String query, Pageable pageable);



    //    Derived Queries
    Page<Portfolio> findAllByOrderByNameAsc(Pageable pageable);

    Page<Portfolio> findAllByOrderByNameDesc(Pageable pageable);

    //    Derived Queries
    Boolean existsByEmail(String email);

    Boolean existsByPhoneno(long phoneno);

    //    Derived Queries
    Boolean existsByEmailAndIdIsNot(String email, long id);

    Boolean existsByPhonenoAndIdIsNot(long phoneno, long id);

}
