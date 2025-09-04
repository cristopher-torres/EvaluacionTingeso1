package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.ToolStatus;
import com.ToolRent.ToolRent.Entity.ToolUnitEntity;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Repository.ToolUnitRepository;
import com.ToolRent.ToolRent.Repository.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class ToolsService {
    @Autowired
    private ToolsRepository toolsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ToolUnitRepository toolUnitRepository;

    // Registrar herramienta
    @Transactional
    public ToolsEntity registerTool(ToolsEntity tool) {
        if (tool.getName() == null || tool.getName().isBlank()) {
            throw new IllegalArgumentException("Se debe ingresar el nombre");
        }
        if (tool.getCategory() == null || tool.getCategory().isBlank()) {
            throw new IllegalArgumentException("Se debe ingresar la categoría");
        }
        if (tool.getReplacementValue() <= 0) {
            throw new IllegalArgumentException("El valor de reposición debe ser mayor que 0");
        }
        if (tool.getStock() == null || tool.getStock() < 0) {
            throw new IllegalArgumentException("El stock debe ser mayor o igual a 0");
        }

        // 1. Guardar herramienta (sin items aún)
        tool = toolsRepository.save(tool);

        // 2. Crear unidades automáticamente
        List<ToolUnitEntity> items = new ArrayList<>();
        for (int i = 0; i < tool.getStock(); i++) {
            ToolUnitEntity item = new ToolUnitEntity();
            item.setTool(tool);
            item.setStatus(ToolStatus.DISPONIBLE);
            items.add(item);
        }

        toolUnitRepository.saveAll(items);
        tool.setUnits(items);

        return tool;
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

        // Actualizar estado de todas las unidades
        for (ToolUnitEntity item : tool.getUnits()) {
            item.setStatus(ToolStatus.DADA_DE_BAJA);
        }

        toolUnitRepository.saveAll(tool.getUnits());
        return tool;
    }

    public List<ToolsEntity> findAll() {
        return toolsRepository.findAll();
    }

    public ToolsEntity findById(Long id) {
        return toolsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrado"));
    }

    @Transactional
    public ToolUnitEntity getAvailableUnit(Long toolId) {
        List<ToolUnitEntity> disponibles =
                toolUnitRepository.findByTool_IdAndStatus(toolId, ToolStatus.DISPONIBLE);

        if (disponibles.isEmpty()) {
            // Aquí se asegura que no se pueda prestar si no hay stock
            throw new RuntimeException("No hay unidades disponibles para préstamo");
        }

        return disponibles.get(0); // tomamos la primera unidad disponible
    }

    @Transactional
    public void loanUnit(ToolUnitEntity unit) {
        // Cambiar estado de la unidad a prestada
        unit.setStatus(ToolStatus.PRESTADA);
        toolUnitRepository.save(unit);

        // Disminuir stock de la herramienta principal
        ToolsEntity tool = unit.getTool();
        if (tool.getStock() > 0) {
            tool.setStock(tool.getStock() - 1);
            toolsRepository.save(tool);
        } else {
            throw new RuntimeException("No hay stock disponible para la herramienta: ");
        }
    }

    @Transactional
    public void returnUnit(ToolUnitEntity unit) {
        unit.setStatus(ToolStatus.DISPONIBLE);
        toolUnitRepository.save(unit);
    }
}
