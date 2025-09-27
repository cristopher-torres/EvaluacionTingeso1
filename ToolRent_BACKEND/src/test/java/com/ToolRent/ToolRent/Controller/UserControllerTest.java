package com.ToolRent.ToolRent.Controller;

import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Service.UserService;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createUser_ShouldReturnSavedUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Cristopher");

        given(userService.save(user)).willReturn(user);

        mockMvc.perform(post("/api/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Cristopher")));
    }

    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("Cristopher");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("Ana");

        List<UserEntity> users = Arrays.asList(user1, user2);
        given(userService.findAll()).willReturn(users);

        mockMvc.perform(get("/api/users/getUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Cristopher")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Ana")));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Cristopher");

        given(userService.findById(1L)).willReturn(user);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Cristopher")));
    }

    @Test
    void updateUserStatus_ShouldReturnSuccessMessage() throws Exception {
        mockMvc.perform(put("/api/users/{userId}/status", 1L)
                        .param("finePaid", "true"))
                .andExpect(status().isOk())
                .andExpect(content().string("Estado del usuario actualizado correctamente"));
    }
}
