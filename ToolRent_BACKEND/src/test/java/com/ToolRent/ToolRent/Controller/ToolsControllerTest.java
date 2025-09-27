package com.ToolRent.ToolRent.Controller;

import com.ToolRent.ToolRent.DTO.ToolStockDTO;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ToolsControllerTest {

    @Mock
    private ToolsService toolsService;

    @InjectMocks
    private ToolsController toolsController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(toolsController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createTool_ShouldReturnSavedTool() throws Exception {
        ToolsEntity tool = new ToolsEntity();
        tool.setId(1L);
        tool.setName("Taladro");

        given(toolsService.registerTool(tool, 5)).willReturn(tool);

        mockMvc.perform(post("/api/tools/createTool/{quantity}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tool)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Taladro")));
    }

    @Test
    void getAllTools_ShouldReturnListOfTools() throws Exception {
        ToolsEntity tool1 = new ToolsEntity();
        tool1.setId(1L);
        tool1.setName("Taladro");

        ToolsEntity tool2 = new ToolsEntity();
        tool2.setId(2L);
        tool2.setName("Martillo");

        List<ToolsEntity> tools = Arrays.asList(tool1, tool2);
        given(toolsService.findAll()).willReturn(tools);

        mockMvc.perform(get("/api/tools/getTools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void decommissionTool_ShouldReturnUpdatedTool_WhenAdmin() throws Exception {
        ToolsEntity tool = new ToolsEntity();
        tool.setId(1L);
        tool.setName("Taladro");
        given(toolsService.decommissionTool(1L)).willReturn(tool);

        mockMvc.perform(put("/api/tools/{toolId}/decommission", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Taladro")));
    }

    @Test
    void decommissionTool_ShouldReturn403_WhenExceptionThrown() throws Exception {
        given(toolsService.decommissionTool(1L)).willThrow(new RuntimeException("No permission"));

        mockMvc.perform(put("/api/tools/{toolId}/decommission", 1L))
                .andExpect(status().isForbidden())
                .andExpect(content().string("No permission"));
    }

    @Test
    void getToolsStock_ShouldReturnListOfToolStockDTO() throws Exception {
        ToolStockDTO stockDTO = new ToolStockDTO();
        stockDTO.setName("Taladro");
        stockDTO.setCategory("Eléctrica");
        stockDTO.setDisponible(5);
        stockDTO.setPrestada(2);
        stockDTO.setEnReparacion(1);
        stockDTO.setDadaDeBaja(0);

        List<ToolStockDTO> stockList = Arrays.asList(stockDTO);
        given(toolsService.getToolsStock()).willReturn(stockList);

        mockMvc.perform(get("/api/tools/stock"))
                .andExpect(jsonPath("$[0].name", is("Taladro")))
                .andExpect(jsonPath("$[0].category", is("Eléctrica")))
                .andExpect(jsonPath("$[0].disponible", is(5)))
                .andExpect(jsonPath("$[0].prestada", is(2)))
                .andExpect(jsonPath("$[0].enReparacion", is(1)))
                .andExpect(jsonPath("$[0].dadaDeBaja", is(0)));

    }

    @Test
    void updateTool_ShouldReturnUpdatedTool() throws Exception {
        ToolsEntity toolDetails = new ToolsEntity();
        toolDetails.setName("Taladro Modificado");

        ToolsEntity updatedTool = new ToolsEntity();
        updatedTool.setId(1L);
        updatedTool.setName("Taladro Modificado");

        given(toolsService.updateTool(1L, toolDetails)).willReturn(updatedTool);

        mockMvc.perform(put("/api/tools/updateTool/{toolId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toolDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Taladro Modificado")));
    }

    @Test
    void getToolById_ShouldReturnTool() throws Exception {
        ToolsEntity tool = new ToolsEntity();
        tool.setId(1L);
        tool.setName("Taladro");

        given(toolsService.findById(1L)).willReturn(tool);

        mockMvc.perform(get("/api/tools/getTool/{toolId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Taladro")));
    }

    @Test
    void getAvailableTools_ShouldReturnListOfTools() throws Exception {
        ToolsEntity tool1 = new ToolsEntity();
        tool1.setId(1L);
        tool1.setName("Taladro");

        List<ToolsEntity> tools = Arrays.asList(tool1);
        given(toolsService.getAvailableTools()).willReturn(tools);

        mockMvc.perform(get("/api/tools/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
