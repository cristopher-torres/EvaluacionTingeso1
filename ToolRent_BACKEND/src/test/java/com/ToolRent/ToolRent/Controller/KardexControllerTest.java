package com.ToolRent.ToolRent.Controller;

import com.ToolRent.ToolRent.Entity.KardexEntity;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Service.KardexService;
import com.ToolRent.ToolRent.Service.ToolsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class KardexControllerTest {

    @Mock
    private KardexService kardexService;

    @Mock
    private ToolsService toolsService;

    @InjectMocks
    private KardexController kardexController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(kardexController).build();
    }

    @Test
    void getMovementsByTool_ShouldReturnKardexList() throws Exception {
        ToolsEntity tool = new ToolsEntity();
        tool.setId(1L);
        tool.setName("Taladro");

        KardexEntity movement1 = new KardexEntity();
        movement1.setId(1L);
        movement1.setTool(tool);

        KardexEntity movement2 = new KardexEntity();
        movement2.setId(2L);
        movement2.setTool(tool);

        List<KardexEntity> movements = Arrays.asList(movement1, movement2);

        given(toolsService.findById(1L)).willReturn(tool);
        given(kardexService.getMovementsByTool(tool)).willReturn(movements);

        mockMvc.perform(get("/api/kardex/tool/{toolId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void getMovementsByDateRange_ShouldReturnKardexList() throws Exception {
        KardexEntity movement1 = new KardexEntity();
        movement1.setId(1L);

        KardexEntity movement2 = new KardexEntity();
        movement2.setId(2L);

        List<KardexEntity> movements = Arrays.asList(movement1, movement2);

        LocalDateTime start = LocalDateTime.of(2025, 9, 27, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 9, 27, 23, 59);

        given(kardexService.getMovementsByDateRange(start, end)).willReturn(movements);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        mockMvc.perform(get("/api/kardex/dates")
                        .param("start", start.format(formatter))
                        .param("end", end.format(formatter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
}


