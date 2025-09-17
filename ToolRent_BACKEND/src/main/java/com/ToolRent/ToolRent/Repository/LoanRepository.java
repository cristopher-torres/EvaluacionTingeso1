package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    // Traer préstamos activos (no entregados) ordenados por fecha de creación descendente
    @Query("SELECT l " +
            "FROM LoanEntity l " +
            "WHERE l.delivered = false " +
            "ORDER BY l.createdLoan DESC")
    List<LoanEntity> findActiveLoansOrderedByDateDesc();
}