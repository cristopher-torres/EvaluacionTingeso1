package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
    // Traer préstamos activos (no entregados) ordenados por fecha de creación descendente
    @Query("SELECT l " +
            "FROM LoanEntity l " +
            "WHERE l.delivered = false " +
            "ORDER BY l.createdLoan DESC")
    List<LoanEntity> findActiveLoansOrderedByDateDesc();

    @Query("SELECT l FROM LoanEntity l " +
            "WHERE l.delivered = false " +
            "AND l.startDate BETWEEN :startDate AND :endDate")
    List<LoanEntity> findActiveLoansByDateRange(LocalDate startDate, LocalDate endDate);


    @Query("SELECT DISTINCT l.client FROM LoanEntity l " +
            "WHERE l.loanStatus = 'ATRASADO' " +
            "AND l.scheduledReturnDate < :today")
    List<UserEntity> findClientsWithOverdueLoansAll(LocalDate today);

    @Query("SELECT DISTINCT l.client FROM LoanEntity l " +
            "WHERE l.loanStatus = 'ATRASADO' " +
            "AND l.scheduledReturnDate < :today " +
            "AND l.startDate BETWEEN :startDate AND :endDate")
    List<UserEntity> findClientsWithOverdueLoans(LocalDate today, LocalDate startDate, LocalDate endDate);


    @Query("SELECT l.tool, COUNT(l) as totalPrestamos " +
            "FROM LoanEntity l " +
            "GROUP BY l.tool " +
            "ORDER BY totalPrestamos DESC")
    List<Object[]> findMostLoanedToolsAll();

    @Query("SELECT l.tool, COUNT(l) as totalPrestamos " +
            "FROM LoanEntity l " +
            "WHERE l.startDate BETWEEN :startDate AND :endDate " +
            "GROUP BY l.tool " +
            "ORDER BY totalPrestamos DESC")
    List<Object[]> findMostLoanedTools(LocalDate startDate, LocalDate endDate);



}