package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.KardexEntity;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Repository.KardexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KardexServiceTest {

    @Mock
    private KardexRepository kardexRepository;

    @InjectMocks
    private KardexService kardexService;

    @BeforeEach
    void setUp() {
        // Inicializa los @Mock y @InjectMocks
        MockitoAnnotations.openMocks(this);
    }

    // --- save() ---
    @Test
    void testSaveKardex() {
        ToolsEntity tool = new ToolsEntity();
        tool.setId(1L);
        tool.setName("Martillo");

        KardexEntity movement = new KardexEntity();
        movement.setId(10L);
        movement.setType("Ingreso");
        movement.setQuantity(3);
        movement.setUserEmail("user@test.com");
        movement.setDateTime(LocalDateTime.now());
        movement.setTool(tool);

        when(kardexRepository.save(movement)).thenReturn(movement);

        KardexEntity result = kardexService.save(movement);

        assertNotNull(result);
        assertEquals("Ingreso", result.getType());
        assertEquals(tool, result.getTool());
        verify(kardexRepository, times(1)).save(movement);
    }

    @Test
    void testSaveMultipleKardex() {
        // Crear varias herramientas y movimientos
        ToolsEntity tool1 = new ToolsEntity();
        tool1.setId(1L);
        tool1.setName("Taladro");

        ToolsEntity tool2 = new ToolsEntity();
        tool2.setId(2L);
        tool2.setName("Martillo");

        KardexEntity m1 = new KardexEntity();
        m1.setId(10L); m1.setTool(tool1); m1.setType("Ingreso"); m1.setQuantity(5); m1.setUserEmail("user1@test.com"); m1.setDateTime(LocalDateTime.now());

        KardexEntity m2 = new KardexEntity();
        m2.setId(11L); m2.setTool(tool1); m2.setType("Egreso"); m2.setQuantity(2); m2.setUserEmail("user2@test.com"); m2.setDateTime(LocalDateTime.now());

        KardexEntity m3 = new KardexEntity();
        m3.setId(12L); m3.setTool(tool2); m3.setType("Ingreso"); m3.setQuantity(7); m3.setUserEmail("user3@test.com"); m3.setDateTime(LocalDateTime.now());

        // Mock del repository
        when(kardexRepository.save(m1)).thenReturn(m1);
        when(kardexRepository.save(m2)).thenReturn(m2);
        when(kardexRepository.save(m3)).thenReturn(m3);

        // Guardar movimientos
        KardexEntity r1 = kardexService.save(m1);
        KardexEntity r2 = kardexService.save(m2);
        KardexEntity r3 = kardexService.save(m3);

        // Asserts
        assertEquals(5, r1.getQuantity());
        assertEquals("Egreso", r2.getType());
        assertEquals(tool2, r3.getTool());

        verify(kardexRepository, times(1)).save(m1);
        verify(kardexRepository, times(1)).save(m2);
        verify(kardexRepository, times(1)).save(m3);
    }
    // --- getMovementsByTool() ---
    @Test
    void testGetMovementsByTool() {
        ToolsEntity tool = new ToolsEntity();
        tool.setId(2L);
        tool.setName("Taladro");

        KardexEntity movement = new KardexEntity();
        movement.setId(20L);
        movement.setType("Egreso");
        movement.setQuantity(2);
        movement.setDateTime(LocalDateTime.now());
        movement.setTool(tool);

        List<KardexEntity> list = new ArrayList<>();
        list.add(movement);

        when(kardexRepository.findByTool(tool)).thenReturn(list);

        List<KardexEntity> result = kardexService.getMovementsByTool(tool);

        assertEquals(1, result.size());
        assertEquals("Egreso", result.get(0).getType());
        verify(kardexRepository, times(1)).findByTool(tool);
    }

    @Test
    void testGetMovementsByToolMultiple() {
        ToolsEntity tool1 = new ToolsEntity();
        tool1.setId(1L);
        tool1.setName("Taladro");

        ToolsEntity tool2 = new ToolsEntity();
        tool2.setId(2L);
        tool2.setName("Martillo");

        KardexEntity m1 = new KardexEntity();
        m1.setId(10L); m1.setTool(tool1); m1.setType("Ingreso"); m1.setQuantity(5); m1.setDateTime(LocalDateTime.now());

        KardexEntity m2 = new KardexEntity();
        m2.setId(11L); m2.setTool(tool1); m2.setType("Egreso"); m2.setQuantity(2); m2.setDateTime(LocalDateTime.now());

        KardexEntity m3 = new KardexEntity();
        m3.setId(12L); m3.setTool(tool2); m3.setType("Ingreso"); m3.setQuantity(7); m3.setDateTime(LocalDateTime.now());

        List<KardexEntity> tool1Movements = new ArrayList<>();
        tool1Movements.add(m1);
        tool1Movements.add(m2);

        List<KardexEntity> tool2Movements = new ArrayList<>();
        tool2Movements.add(m3);

        when(kardexRepository.findByTool(tool1)).thenReturn(tool1Movements);
        when(kardexRepository.findByTool(tool2)).thenReturn(tool2Movements);

        List<KardexEntity> result1 = kardexService.getMovementsByTool(tool1);
        List<KardexEntity> result2 = kardexService.getMovementsByTool(tool2);

        assertEquals(2, result1.size());
        assertEquals("Ingreso", result1.get(0).getType());
        assertEquals("Egreso", result1.get(1).getType());

        assertEquals(1, result2.size());
        assertEquals("Ingreso", result2.get(0).getType());

        verify(kardexRepository, times(1)).findByTool(tool1);
        verify(kardexRepository, times(1)).findByTool(tool2);
    }

    // --- getMovementsByDateRange() ---
    @Test
    void testGetMovementsByDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        ToolsEntity tool = new ToolsEntity();
        tool.setId(4L);
        tool.setName("Compresor");

        KardexEntity movement = new KardexEntity();
        movement.setId(30L);
        movement.setType("Ingreso");
        movement.setQuantity(7);
        movement.setDateTime(LocalDateTime.now());
        movement.setTool(tool);

        List<KardexEntity> list = new ArrayList<>();
        list.add(movement);

        when(kardexRepository.findByDateTimeBetween(start, end)).thenReturn(list);

        List<KardexEntity> result = kardexService.getMovementsByDateRange(start, end);

        assertEquals(1, result.size());
        assertEquals("Ingreso", result.get(0).getType());
        verify(kardexRepository, times(1)).findByDateTimeBetween(start, end);
    }

    @Test
    void testGetMovementsByDateRangeMultiple() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(2);
        LocalDateTime end = now.plusDays(2);

        ToolsEntity tool = new ToolsEntity();
        tool.setId(1L);
        tool.setName("Taladro");

        KardexEntity m1 = new KardexEntity();
        m1.setId(10L); m1.setTool(tool); m1.setType("Ingreso"); m1.setQuantity(5); m1.setDateTime(now.minusDays(1));

        KardexEntity m2 = new KardexEntity();
        m2.setId(11L); m2.setTool(tool); m2.setType("Egreso"); m2.setQuantity(2); m2.setDateTime(now);

        KardexEntity m3 = new KardexEntity();
        m3.setId(12L); m3.setTool(tool); m3.setType("Ingreso"); m3.setQuantity(7); m3.setDateTime(now.plusDays(1));

        List<KardexEntity> list = new ArrayList<>();
        list.add(m1); list.add(m2); list.add(m3);

        when(kardexRepository.findByDateTimeBetween(start, end)).thenReturn(list);

        List<KardexEntity> result = kardexService.getMovementsByDateRange(start, end);

        assertEquals(3, result.size());
        assertEquals("Ingreso", result.get(0).getType());
        assertEquals("Egreso", result.get(1).getType());
        assertEquals("Ingreso", result.get(2).getType());

        verify(kardexRepository, times(1)).findByDateTimeBetween(start, end);
    }
}

