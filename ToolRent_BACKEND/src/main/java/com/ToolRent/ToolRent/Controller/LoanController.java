package com.ToolRent.ToolRent.Controller;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/createLoan")
    public ResponseEntity<LoanEntity> createLoan(@RequestBody LoanEntity loan) {
        LoanEntity createdLoan = loanService.createLoan(loan);
        return ResponseEntity.ok(createdLoan);
    }

}
