package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Repository.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
public class ToolsService {
    @Autowired
    ToolsRepository toolsRepository;

    // Registrar herramienta
    @Transactional
    public ToolsEntity registerTool(ToolsEntity tool) {
        if (tool.getName() == null) {
            throw new IllegalArgumentException("Se debe ingresar el nombre");
        }
        if (tool.getCategory() == null) {
            throw new IllegalArgumentException("Se debe ingresar la categoria");
        }
        if (tool.getReplacementValue() == null || tool.getReplacementValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El valor de la reposición debe ser mayor que 0");
        }

        // Estado por defecto si no viene
        if (tool.getStatus() == null) {
            tool.setStatus("Disponible");
        }

        // Si no hay stock, inicializar en 0
        if (tool.getStock() == null || tool.getStock() < 0) {
            tool.setStock(0);
        }

        return toolsRepository.save(tool);
    }
}
