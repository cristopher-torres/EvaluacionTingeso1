package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.DTO.ToolStockDTO;
import com.ToolRent.ToolRent.Entity.ToolStatus;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Repository.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class ToolsService {
    @Autowired
    private ToolsRepository toolsRepository;

    @Autowired
    private UserService userService;

    // Registrar herramienta
    @Transactional
    public ToolsEntity registerTool(ToolsEntity tool, int quantity) {
        if (tool.getName() == null || tool.getName().isBlank()) {
            throw new IllegalArgumentException("Se debe ingresar el nombre");
        }
        if (tool.getCategory() == null || tool.getCategory().isBlank()) {
            throw new IllegalArgumentException("Se debe ingresar la categoría");
        }
        if (tool.getReplacementValue() <= 0) {
            throw new IllegalArgumentException("El valor de reposición debe ser mayor que 0");
        }

        // 2. Crear unidades automáticamente
        List<ToolsEntity> units = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            ToolsEntity unit = new ToolsEntity();
            unit.setName(tool.getName());
            unit.setCategory(tool.getCategory());
            unit.setReplacementValue(tool.getReplacementValue());
            unit.setDailyRate(tool.getDailyRate());
            unit.setDailyLateRate(tool.getDailyLateRate());
            unit.setStatus(ToolStatus.DISPONIBLE);
            units.add(unit);
        }

        List<ToolsEntity> savedUnits = toolsRepository.saveAll(units);

        // Devolver la primera unidad creada
        return savedUnits.get(0);
    }

    private void validateAdminPermission(Long userId) {
        UserEntity user = userService.findById(userId);

        String role = user.getRole();

        if (!"Administrador".equalsIgnoreCase(role)) {
            throw new RuntimeException("Solo un administrador puede realizar esta acción");
        }
    }

    @Transactional
    public ToolsEntity decommissionTool(Long toolId, Long userId) {
        validateAdminPermission(userId);

        ToolsEntity tool = toolsRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        tool.setStatus(ToolStatus.DADA_DE_BAJA);
        return toolsRepository.save(tool);
    }

    public List<ToolsEntity> findAll() {
        return toolsRepository.findAll();
    }

    public ToolsEntity findById(Long id) {
        return toolsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));
    }

    // Obtener una unidad disponible de un tipo de herramienta
    @Transactional
    public ToolsEntity getAvailableTool(long id) {
        return toolsRepository.findByIdAndStatus(id, ToolStatus.DISPONIBLE)
                .orElseThrow(() -> new RuntimeException("No hay unidades disponibles para préstamo"));
    }

    // Prestar una unidad
    @Transactional
    public void loanTool(Long toolId) {
        ToolsEntity tool = toolsRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        if (tool.getStatus() != ToolStatus.DISPONIBLE) {
            throw new RuntimeException("La herramienta no está disponible");
        }

        tool.setStatus(ToolStatus.PRESTADA);
        toolsRepository.save(tool);
    }

    // Devolver una unidad
    @Transactional
    public void returnTool(Long toolId) {
        ToolsEntity tool = toolsRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        if (tool.getStatus() != ToolStatus.PRESTADA) {
            throw new RuntimeException("La herramienta no estaba prestada");
        }

        tool.setStatus(ToolStatus.DISPONIBLE);
        toolsRepository.save(tool);
    }

    public List<ToolStockDTO> getToolsStock() {
        List<Object[]> toolNameCategory = toolsRepository.findDistinctNameAndCategory();
        List<ToolStockDTO> stockList = new ArrayList<>();

        for (Object[] pair : toolNameCategory) {
            String name = (String) pair[0];
            String category = (String) pair[1];

            ToolStockDTO dto = new ToolStockDTO();
            dto.setName(name);
            dto.setCategory(category);
            dto.setDisponible(toolsRepository.countByNameAndCategoryAndStatus(name, category, ToolStatus.DISPONIBLE));
            dto.setPrestada(toolsRepository.countByNameAndCategoryAndStatus(name, category, ToolStatus.PRESTADA));
            dto.setEnReparacion(toolsRepository.countByNameAndCategoryAndStatus(name, category, ToolStatus.EN_REPARACION));
            dto.setDadaDeBaja(toolsRepository.countByNameAndCategoryAndStatus(name, category, ToolStatus.DADA_DE_BAJA));

            stockList.add(dto);
        }

        return stockList;
    }

    public ToolsEntity updateTool(Long toolId, ToolsEntity toolDetails) {
        ToolsEntity tool = toolsRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        // Actualizar los campos editables
        tool.setName(toolDetails.getName());
        tool.setCategory(toolDetails.getCategory());
        tool.setReplacementValue(toolDetails.getReplacementValue());
        tool.setDailyRate(toolDetails.getDailyRate());
        tool.setDailyLateRate(toolDetails.getDailyLateRate());
        tool.setStatus(toolDetails.getStatus());
        // Ojo: el stock lo puedes decidir si se actualiza aquí o lo dejas fijo

        return toolsRepository.save(tool);
    }

    public List<ToolsEntity> getAvailableTools() {
        return toolsRepository.findByStatus(ToolStatus.DISPONIBLE);
    }

}
