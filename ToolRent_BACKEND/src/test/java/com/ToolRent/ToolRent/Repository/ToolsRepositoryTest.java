package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.ToolStatus;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ToolsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToolsRepository toolsRepository;

    @Test
    void whenFindByIdAndStatus_thenReturnToolIfExists() {
        // given
        ToolsEntity tool = new ToolsEntity();
        tool.setName("Taladro");
        tool.setCategory("Electricas");
        tool.setDailyRate(10000.0);
        tool.setDailyLateRate(5000.0);
        tool.setRepairValue(50000.0);
        tool.setReplacementValue(200000.0);
        tool.setStatus(ToolStatus.DISPONIBLE);
        entityManager.persistAndFlush(tool);

        // when
        Optional<ToolsEntity> found = toolsRepository.findByIdAndStatus(tool.getId(), ToolStatus.DISPONIBLE);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Taladro");
    }

    @Test
    void whenFindDistinctNameAndCategory_thenReturnList() {
        // given
        ToolsEntity tool1 = new ToolsEntity();
        tool1.setName("Taladro");
        tool1.setCategory("Electricas");
        tool1.setDailyRate(10000.0);
        tool1.setDailyLateRate(5000.0);
        tool1.setRepairValue(50000.0);
        tool1.setReplacementValue(200000.0);
        tool1.setStatus(ToolStatus.DISPONIBLE);

        ToolsEntity tool2 = new ToolsEntity();
        tool2.setName("Martillo");
        tool2.setCategory("Manuales");
        tool2.setDailyRate(5000.0);
        tool2.setDailyLateRate(2000.0);
        tool2.setRepairValue(20000.0);
        tool2.setReplacementValue(100000.0);
        tool2.setStatus(ToolStatus.DISPONIBLE);

        entityManager.persistAndFlush(tool1);
        entityManager.persistAndFlush(tool2);

        // when
        List<Object[]> distinctTools = toolsRepository.findDistinctNameAndCategory();

        // then
        assertThat(distinctTools).hasSize(2);
        assertThat(distinctTools).extracting(row -> row[0]).containsExactlyInAnyOrder("Taladro", "Martillo");
        assertThat(distinctTools).extracting(row -> row[1]).containsExactlyInAnyOrder("Electricas", "Manuales");
    }

    @Test
    void whenCountByNameCategoryAndStatus_thenReturnCorrectCount() {
        // given
        ToolsEntity tool1 = new ToolsEntity();
        tool1.setName("Taladro");
        tool1.setCategory("Electricas");
        tool1.setStatus(ToolStatus.DISPONIBLE);
        tool1.setDailyRate(10000.0);
        tool1.setDailyLateRate(5000.0);
        tool1.setRepairValue(50000.0);
        tool1.setReplacementValue(200000.0);
        entityManager.persistAndFlush(tool1);

        ToolsEntity tool2 = new ToolsEntity();
        tool2.setName("Taladro");
        tool2.setCategory("Electricas");
        tool2.setStatus(ToolStatus.PRESTADA);
        tool2.setDailyRate(12000.0);
        tool2.setDailyLateRate(6000.0);
        tool2.setRepairValue(60000.0);
        tool2.setReplacementValue(210000.0);
        entityManager.persistAndFlush(tool2);

        // when
        int countDisponible = toolsRepository.countByNameAndCategoryAndStatus("Taladro", "Electricas", ToolStatus.DISPONIBLE);
        int countPrestada = toolsRepository.countByNameAndCategoryAndStatus("Taladro", "Electricas", ToolStatus.PRESTADA);

        // then
        assertThat(countDisponible).isEqualTo(1);
        assertThat(countPrestada).isEqualTo(1);
    }

    @Test
    void whenFindByStatus_thenReturnOnlyMatchingTools() {
        // given
        ToolsEntity tool1 = new ToolsEntity();
        tool1.setName("Taladro");
        tool1.setCategory("Electricas");
        tool1.setStatus(ToolStatus.DISPONIBLE);
        tool1.setDailyRate(10000.0);
        tool1.setDailyLateRate(5000.0);
        tool1.setRepairValue(50000.0);
        tool1.setReplacementValue(200000.0);

        ToolsEntity tool2 = new ToolsEntity();
        tool2.setName("Martillo");
        tool2.setCategory("Manuales");
        tool2.setStatus(ToolStatus.PRESTADA);
        tool2.setDailyRate(5000.0);
        tool2.setDailyLateRate(2000.0);
        tool2.setRepairValue(20000.0);
        tool2.setReplacementValue(100000.0);

        entityManager.persistAndFlush(tool1);
        entityManager.persistAndFlush(tool2);

        // when
        List<ToolsEntity> disponibles = toolsRepository.findByStatus(ToolStatus.DISPONIBLE);

        // then
        assertThat(disponibles).hasSize(1);
        assertThat(disponibles.get(0).getName()).isEqualTo("Taladro");
    }
}

