package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Entity.ToolStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void whenCountActiveLoans_thenReturnCorrectNumber() {
        // given
        UserEntity user = new UserEntity();
        user.setRut("12345678-9");
        user.setUsername("user1");
        entityManager.persistAndFlush(user);

        ToolsEntity tool = new ToolsEntity();
        tool.setName("Taladro");
        tool.setStatus(ToolStatus.DISPONIBLE);
        entityManager.persistAndFlush(tool);

        LoanEntity loan1 = new LoanEntity();
        loan1.setClient(user);
        loan1.setTool(tool);
        loan1.setDelivered(false);
        loan1.setCreatedLoan(LocalDateTime.now());
        loan1.setStartDate(LocalDate.now());
        loan1.setScheduledReturnDate(LocalDate.now().plusDays(1));
        entityManager.persist(loan1);

        LoanEntity loan2 = new LoanEntity();
        loan2.setClient(user);
        loan2.setTool(tool);
        loan2.setDelivered(true); // entregado, no cuenta
        loan2.setCreatedLoan(LocalDateTime.now());
        loan2.setStartDate(LocalDate.now());
        loan2.setScheduledReturnDate(LocalDate.now().plusDays(1));
        entityManager.persist(loan2);

        entityManager.flush();

        // when
        long activeLoans = userRepository.countActiveLoans(user.getId());

        // then
        assertThat(activeLoans).isEqualTo(1);
    }

    @Test
    void whenCountActiveLoansByToolName_thenReturnCorrectNumber() {
        // given
        UserEntity user = new UserEntity();
        user.setRut("98765432-1");
        user.setUsername("user2");
        entityManager.persistAndFlush(user);

        ToolsEntity tool1 = new ToolsEntity();
        tool1.setName("Taladro");
        tool1.setStatus(ToolStatus.DISPONIBLE);
        entityManager.persistAndFlush(tool1);

        ToolsEntity tool2 = new ToolsEntity();
        tool2.setName("Martillo");
        tool2.setStatus(ToolStatus.DISPONIBLE);
        entityManager.persistAndFlush(tool2);

        LoanEntity loan1 = new LoanEntity();
        loan1.setClient(user);
        loan1.setTool(tool1);
        loan1.setDelivered(false);
        loan1.setCreatedLoan(LocalDateTime.now());
        loan1.setStartDate(LocalDate.now());
        loan1.setScheduledReturnDate(LocalDate.now().plusDays(1));
        entityManager.persist(loan1);

        LoanEntity loan2 = new LoanEntity();
        loan2.setClient(user);
        loan2.setTool(tool1);
        loan2.setDelivered(true); // no cuenta
        loan2.setCreatedLoan(LocalDateTime.now());
        loan2.setStartDate(LocalDate.now());
        loan2.setScheduledReturnDate(LocalDate.now().plusDays(1));
        entityManager.persist(loan2);

        LoanEntity loan3 = new LoanEntity();
        loan3.setClient(user);
        loan3.setTool(tool2);
        loan3.setDelivered(false); // otra herramienta, no cuenta
        loan3.setCreatedLoan(LocalDateTime.now());
        loan3.setStartDate(LocalDate.now());
        loan3.setScheduledReturnDate(LocalDate.now().plusDays(1));
        entityManager.persist(loan3);

        entityManager.flush();

        // when
        int activeToolLoans = userRepository.countActiveLoansByToolName(user.getId(), "Taladro");

        // then
        assertThat(activeToolLoans).isEqualTo(1);
    }
}


