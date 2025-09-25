package com.ToolRent.ToolRent.Controller;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin("*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/createLoan")
    public ResponseEntity<LoanEntity> createLoan(@RequestBody LoanEntity loan) {
        LoanEntity createdLoan = loanService.createLoan(loan);
        return ResponseEntity.ok(createdLoan);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/{loanId}/return")
    public LoanEntity returnLoan(
            @PathVariable Long loanId,
            @RequestParam(required = false, defaultValue = "false") boolean damaged,
            @RequestParam(required = false, defaultValue = "false") boolean irreparable) {
        return loanService.returnLoan(loanId, damaged, irreparable);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/getLoans")
    public ResponseEntity<List<LoanEntity>> getAllLoans() {
        List<LoanEntity> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // Traer solo los préstamos activos (no devueltos)
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/loansActive")
    public ResponseEntity<List<LoanEntity>> getActiveLoans() {
        List<LoanEntity> loans = loanService.getActiveLoans();
        return ResponseEntity.ok(loans);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PutMapping("/{loanId}/finePaid")
    public LoanEntity updateFinePaid(@PathVariable Long loanId, @RequestParam boolean finePaid) {
        return loanService.updateFinePaid(loanId, finePaid);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/loansActiveByDate")
    public ResponseEntity<List<LoanEntity>> getActiveLoansByDate(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        List<LoanEntity> loans = loanService.getActiveLoansByDate(startDate, endDate);
        return ResponseEntity.ok(loans);
    }

    // Obtener todos los clientes con préstamos atrasados
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/overdueClients")
    public List<LoanEntity> getOverdueClients() {
        LocalDate today = LocalDate.now();
        return loanService.getOverdueLoans(today);
    }

    // Obtener clientes con préstamos atrasados filtrados por rango de fechas
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/overdueClients/dateRange")
    public List<LoanEntity> getOverdueClientsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        LocalDate today = LocalDate.now();
        return loanService.getOverdueLoansByDate(today, startDate, endDate);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/topToolsByDate")
    public List<Object[]> getTopToolsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return loanService.getTopLentTools(startDate, endDate);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/topTools")
    public List<Object[]> getTopTools() {
        return loanService.getTopLentToolsAllTime();
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<LoanEntity>> getUnpaidLoans() {
        List<LoanEntity> unpaidLoans = loanService.getUnpaidLoans();
        return ResponseEntity.ok(unpaidLoans);
    }

}
