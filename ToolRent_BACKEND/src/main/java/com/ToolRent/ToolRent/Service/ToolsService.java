package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.ToolStatus;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Repository.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ToolsService {
    @Autowired
    private ToolsRepository toolsRepository;

    @Autowired
    private UserService userService;

    // Registrar herramienta
    @Transactional
    public ToolsEntity registerTool(ToolsEntity tool) {
        if (tool.getName() == null) {
            throw new IllegalArgumentException("Se debe ingresar el nombre");
        }
        if (tool.getCategory() == null) {
            throw new IllegalArgumentException("Se debe ingresar la categoria");
        }
        if (tool.getReplacementValue() <= 0) {
            throw new IllegalArgumentException("El valor de la reposición debe ser mayor que 0");
        }

        // Estado por defecto si no viene
        if (tool.getStatus() == null) {
            tool.setStatus(ToolStatus.DISPONIBLE);
        }

        // Si no hay stock, inicializar en 0
        if (tool.getStock() == null || tool.getStock() < 0) {
            tool.setStock(0);
        }

        return toolsRepository.save(tool);
    }

    private void validateAdminPermission(Long userId) {
        UserEntity user = userService.findById(userId);

        String role = user.getRole();

        if (!"Administrador".equalsIgnoreCase(role)) {
            throw new RuntimeException("Solo un administrador puede realizar esta acción");
        }
    }

    public ToolsEntity decommissionTool(Long toolId, Long userId) {
        // Validar permisos de administrador
        validateAdminPermission(userId);

        ToolsEntity tool = toolsRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        tool.setStatus(ToolStatus.DADA_DE_BAJA);
        tool.setStock(0);

        return toolsRepository.save(tool);
    }

    public List<ToolsEntity> findAll() {
        return toolsRepository.findAll();
    }

    public ToolsEntity findById(Long id) {
        return toolsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrado"));
    }
}
