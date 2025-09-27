package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class LoanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LoanRepository loanRepository;

    @Test
    void whenFindActiveLoansOrderedByDateDesc_thenReturnOnlyUndeliveredOrdered() {
        // given
        ToolsEntity tool = new ToolsEntity();
        tool.setName("Taladro");
        tool.setDailyRate(10f);
        tool.setDailyLateRate(5f);
        tool.setRepairValue(50f);
        tool.setReplacementValue(200f);
        entityManager.persistAndFlush(tool);

        UserEntity client = new UserEntity();
        client.setUsername("usuario1");
        client.setRut("12345678-9");
        client.setName("Juan");
        client.setLastName("Perez");
        client.setStatus("ACTIVO");
        client.setEmail("juan@example.com");
        entityManager.persistAndFlush(client);

        LoanEntity deliveredLoan = new LoanEntity();
        deliveredLoan.setTool(tool);
        deliveredLoan.setClient(client);
        deliveredLoan.setDelivered(true);
        deliveredLoan.setCreatedLoan(LocalDateTime.now().minusDays(1));
        deliveredLoan.setLoanPrice(100f);
        deliveredLoan.setDamagePrice(0f);
        deliveredLoan.setFine(0f);
        deliveredLoan.setFineTotal(0f);
        deliveredLoan.setStartDate(LocalDate.now().minusDays(1));
        deliveredLoan.setScheduledReturnDate(LocalDate.now());
        deliveredLoan.setTotal(100f);
        entityManager.persist(deliveredLoan);

        LoanEntity activeLoan = new LoanEntity();
        activeLoan.setTool(tool);
        activeLoan.setClient(client);
        activeLoan.setDelivered(false);
        activeLoan.setCreatedLoan(LocalDateTime.now());
        activeLoan.setLoanPrice(120f);
        activeLoan.setDamagePrice(0f);
        activeLoan.setFine(0f);
        activeLoan.setFineTotal(0f);
        activeLoan.setStartDate(LocalDate.now());
        activeLoan.setScheduledReturnDate(LocalDate.now().plusDays(1));
        activeLoan.setTotal(120f);
        entityManager.persistAndFlush(activeLoan);

        // when
        List<LoanEntity> result = loanRepository.findActiveLoansOrderedByDateDesc();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isDelivered()).isFalse();
    }

    @Test
    void whenFindOverdueLoans_thenReturnOnlyOverdue() {
        // given
        ToolsEntity tool = new ToolsEntity();
        tool.setName("Taladro");
        tool.setDailyRate(10f);
        tool.setDailyLateRate(5f);
        tool.setRepairValue(50f);
        tool.setReplacementValue(200f);
        entityManager.persistAndFlush(tool);

        UserEntity client = new UserEntity();
        client.setUsername("usuario1");
        client.setRut("12345678-9");
        client.setName("Juan");
        client.setLastName("Perez");
        client.setStatus("ACTIVO");
        client.setEmail("juan@example.com");
        entityManager.persistAndFlush(client);

        LoanEntity overdueLoan = new LoanEntity();
        overdueLoan.setTool(tool);
        overdueLoan.setClient(client);
        overdueLoan.setDelivered(false);
        overdueLoan.setLoanStatus("ATRASADO");
        overdueLoan.setStartDate(LocalDate.now().minusDays(5));
        overdueLoan.setScheduledReturnDate(LocalDate.now().minusDays(1));
        overdueLoan.setLoanPrice(100f);
        overdueLoan.setDamagePrice(0f);
        overdueLoan.setFine(0f);
        overdueLoan.setFineTotal(0f);
        overdueLoan.setCreatedLoan(LocalDateTime.now().minusDays(5));
        overdueLoan.setTotal(100f);
        entityManager.persistAndFlush(overdueLoan);

        // when
        List<LoanEntity> result = loanRepository.findOverdueLoans(LocalDate.now());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLoanStatus()).isEqualTo("ATRASADO");
    }

    @Test
    void whenFindByFinePaidFalse_thenReturnUnpaidLoans() {
        // given
        ToolsEntity tool = new ToolsEntity();
        tool.setName("Taladro");
        tool.setDailyRate(10f);
        tool.setDailyLateRate(5f);
        tool.setRepairValue(50f);
        tool.setReplacementValue(200f);
        entityManager.persistAndFlush(tool);

        UserEntity client = new UserEntity();
        client.setUsername("usuario1");
        client.setRut("12345678-9");
        client.setName("Juan");
        client.setLastName("Perez");
        client.setStatus("ACTIVO");
        client.setEmail("juan@example.com");
        entityManager.persistAndFlush(client);

        LoanEntity unpaidLoan = new LoanEntity();
        unpaidLoan.setTool(tool);
        unpaidLoan.setClient(client);
        unpaidLoan.setDelivered(false);
        unpaidLoan.setLoanPrice(100f);
        unpaidLoan.setDamagePrice(0f);
        unpaidLoan.setFine(10f);
        unpaidLoan.setFineTotal(10f);
        unpaidLoan.setFinePaid(false);
        unpaidLoan.setStartDate(LocalDate.now());
        unpaidLoan.setScheduledReturnDate(LocalDate.now().plusDays(1));
        unpaidLoan.setCreatedLoan(LocalDateTime.now());
        unpaidLoan.setTotal(110f);
        entityManager.persistAndFlush(unpaidLoan);

        // when
        List<LoanEntity> result = loanRepository.findByFinePaidFalse();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).isFinePaid()).isFalse();
    }
}
