package com.ToolRent.ToolRent.Controller;

import com.ToolRent.ToolRent.Entity.KardexEntity;
import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Service.KardexService;
import com.ToolRent.ToolRent.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/kardex")
@CrossOrigin("*")
public class KardexController {

    private KardexService kardexService;


    @GetMapping("/tool/{toolId}")
    public List<KardexEntity> getMovementsByTool(@PathVariable ToolsEntity tool) {
        return kardexService.getMovementsByTool(tool);
    }

    @GetMapping("/dates")
    public List<KardexEntity> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return kardexService.getMovementsByDateRange(start, end);
    }
}
