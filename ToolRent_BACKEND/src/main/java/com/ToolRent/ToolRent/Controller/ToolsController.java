package com.ToolRent.ToolRent.Controller;


import com.ToolRent.ToolRent.DTO.ToolStockDTO;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Service.ToolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/tools")
public class ToolsController {

    @Autowired
    private ToolsService toolsService;

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @PostMapping("/createTool/{quantity}")
    public ResponseEntity<ToolsEntity> createTool(@RequestBody ToolsEntity tool, @PathVariable("quantity") int quantity) {
        ToolsEntity savedTool = toolsService.registerTool(tool, quantity);
        return ResponseEntity.ok(savedTool);
    }


    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/getTools")
    public ResponseEntity<List<ToolsEntity>> getAllTools() {
        return ResponseEntity.ok(toolsService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{toolId}/decommission")
    public ResponseEntity<?> decommissionTool(@PathVariable Long toolId) {
        try {
            ToolsEntity updatedTool = toolsService.decommissionTool(toolId);
            return ResponseEntity.ok(updatedTool);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/stock")
    public ResponseEntity<List<ToolStockDTO>> getToolsStock() {
        List<ToolStockDTO> stock = toolsService.getToolsStock();
        return ResponseEntity.ok(stock);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateTool/{toolId}")
    public ResponseEntity<ToolsEntity> updateTool(
            @PathVariable Long toolId,
            @RequestBody ToolsEntity toolDetails) {

        ToolsEntity updatedTool = toolsService.updateTool(toolId, toolDetails);
        return ResponseEntity.ok(updatedTool);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/getTool/{toolId}")
    public ResponseEntity<ToolsEntity> getToolById(@PathVariable Long toolId) {
        ToolsEntity tool = toolsService.findById(toolId);
        return ResponseEntity.ok(tool);
    }

    @GetMapping("/available")
    public List<ToolsEntity> getAvailableTools() {
        return toolsService.getAvailableTools();
    }

}

