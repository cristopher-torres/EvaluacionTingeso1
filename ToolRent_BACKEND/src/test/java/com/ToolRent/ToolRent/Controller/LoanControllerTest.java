package com.ToolRent.ToolRent.Controller;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createLoan_ShouldReturnCreatedLoan() throws Exception {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);

        given(loanService.createLoan(loan)).willReturn(loan);

        mockMvc.perform(post("/api/loans/createLoan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void returnLoan_ShouldReturnLoan() throws Exception {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);

        given(loanService.returnLoan(1L, false, false)).willReturn(loan);

        mockMvc.perform(post("/api/loans/{loanId}/return", 1L)
                        .param("damaged", "false")
                        .param("irreparable", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getAllLoans_ShouldReturnLoanList() throws Exception {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        LoanEntity loan2 = new LoanEntity();
        loan2.setId(2L);

        List<LoanEntity> loans = Arrays.asList(loan1, loan2);
        given(loanService.getAllLoans()).willReturn(loans);

        mockMvc.perform(get("/api/loans/getLoans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void getActiveLoans_ShouldReturnLoanList() throws Exception {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        List<LoanEntity> loans = Arrays.asList(loan1);

        given(loanService.getActiveLoans()).willReturn(loans);

        mockMvc.perform(get("/api/loans/loansActive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void updateFinePaid_ShouldReturnUpdatedLoan() throws Exception {
        LoanEntity loan = new LoanEntity();
        loan.setId(1L);

        given(loanService.updateFinePaid(1L, true)).willReturn(loan);

        mockMvc.perform(put("/api/loans/{loanId}/finePaid", 1L)
                        .param("finePaid", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void getActiveLoansByDate_ShouldReturnLoanList() throws Exception {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        List<LoanEntity> loans = Arrays.asList(loan1);

        LocalDate start = LocalDate.of(2025, 9, 27);
        LocalDate end = LocalDate.of(2025, 9, 27);

        given(loanService.getActiveLoansByDate(start, end)).willReturn(loans);

        mockMvc.perform(get("/api/loans/loansActiveByDate")
                        .param("startDate", start.toString())
                        .param("endDate", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getOverdueClients_ShouldReturnLoanList() throws Exception {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        List<LoanEntity> loans = Arrays.asList(loan1);

        given(loanService.getOverdueLoans(LocalDate.now())).willReturn(loans);

        mockMvc.perform(get("/api/loans/overdueClients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getOverdueClientsByDate_ShouldReturnLoanList() throws Exception {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        List<LoanEntity> loans = Arrays.asList(loan1);

        LocalDate start = LocalDate.of(2025, 9, 27);
        LocalDate end = LocalDate.of(2025, 9, 27);

        given(loanService.getOverdueLoansByDate(LocalDate.now(), start, end)).willReturn(loans);

        mockMvc.perform(get("/api/loans/overdueClients/dateRange")
                        .param("startDate", start.toString())
                        .param("endDate", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getTopToolsByDate_ShouldReturnObjectArrayList() throws Exception {
        List<Object[]> list = Arrays.asList(
                new Object[]{"Hammer", 5},
                new Object[]{"Drill", 3}
        );

        LocalDate start = LocalDate.of(2025, 9, 27);
        LocalDate end = LocalDate.of(2025, 9, 27);

        given(loanService.getTopLentTools(start, end)).willReturn(list);

        mockMvc.perform(get("/api/loans/topToolsByDate")
                        .param("startDate", start.toString())
                        .param("endDate", end.toString()))
                .andExpect(status().isOk());
    }


    @Test
    void getTopTools_ShouldReturnObjectArrayList() throws Exception {
        List<Object[]> list = Arrays.asList(
                new Object[]{"Hammer", 5},
                new Object[]{"Drill", 3}
        );

        given(loanService.getTopLentToolsAllTime()).willReturn(list);

        mockMvc.perform(get("/api/loans/topTools"))
                .andExpect(status().isOk());
    }


    @Test
    void getUnpaidLoans_ShouldReturnLoanList() throws Exception {
        LoanEntity loan1 = new LoanEntity();
        loan1.setId(1L);
        List<LoanEntity> loans = Arrays.asList(loan1);

        given(loanService.getUnpaidLoans()).willReturn(loans);

        mockMvc.perform(get("/api/loans/unpaid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}


