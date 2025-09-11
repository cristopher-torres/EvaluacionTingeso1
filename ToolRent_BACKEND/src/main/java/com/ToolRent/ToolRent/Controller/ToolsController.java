package com.ToolRent.ToolRent.Controller;


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
    @PostMapping("/createTool")
    public ResponseEntity<ToolsEntity> createTool(@RequestBody ToolsEntity tool) {
        ToolsEntity savedTool = toolsService.registerTool(tool);
        return ResponseEntity.ok(savedTool);
    }


    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    @GetMapping("/getTools")
    public ResponseEntity<List<ToolsEntity>> getAllTools() {
        return ResponseEntity.ok(toolsService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{toolId}/decommission")
    public ResponseEntity<?> decommissionTool(@PathVariable Long toolId, @RequestParam Long userId) {
        try {
            ToolsEntity updatedTool = toolsService.decommissionTool(toolId, userId);
            return ResponseEntity.ok(updatedTool);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}

