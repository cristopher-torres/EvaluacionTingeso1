package com.ToolRent.ToolRent.Repository;

import com.ToolRent.ToolRent.Entity.KardexEntity;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class KardexRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private KardexRepository kardexRepository;

    @Test
    void whenFindByTool_thenReturnKardexEntries() {
        // given
        ToolsEntity tool = new ToolsEntity();
        tool.setName("Taladro");
        entityManager.persistAndFlush(tool);

        KardexEntity entry1 = new KardexEntity();
        entry1.setTool(tool);
        entry1.setType("PRESTAMO");
        entry1.setDateTime(LocalDateTime.now());
        entry1.setUserEmail("usuario1@example.com"); // obligatorio
        entry1.setQuantity(1); // si quantity es not-null

        KardexEntity entry2 = new KardexEntity();
        entry2.setTool(tool);
        entry2.setType("DEVOLUCION");
        entry2.setDateTime(LocalDateTime.now());
        entry2.setUserEmail("usuario2@example.com"); // obligatorio
        entry2.setQuantity(1);

        entityManager.persist(entry1);
        entityManager.persist(entry2);
        entityManager.flush();

        // when
        List<KardexEntity> foundEntries = kardexRepository.findByTool(tool);

        // then
        assertThat(foundEntries).hasSize(2)
                .extracting(KardexEntity::getType)
                .containsExactlyInAnyOrder("PRESTAMO", "DEVOLUCION");
    }

    @Test
    void whenFindByDateTimeBetween_thenReturnKardexEntries() {
        // given
        ToolsEntity tool = new ToolsEntity();
        tool.setName("Taladro");
        entityManager.persistAndFlush(tool);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = now.minusDays(1);
        LocalDateTime future = now.plusDays(1);

        KardexEntity entry = new KardexEntity();
        entry.setTool(tool);
        entry.setType("PRESTAMO");
        entry.setDateTime(now);
        entry.setUserEmail("usuario@example.com"); // obligatorio
        entry.setQuantity(1);

        entityManager.persistAndFlush(entry);

        // when
        List<KardexEntity> foundEntries = kardexRepository.findByDateTimeBetween(past, future);

        // then
        assertThat(foundEntries).hasSize(1)
                .extracting(KardexEntity::getType)
                .containsExactly("PRESTAMO");
    }
}


