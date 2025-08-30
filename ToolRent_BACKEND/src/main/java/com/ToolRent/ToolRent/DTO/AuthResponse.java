package com.ToolRent.ToolRent.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String nombre;
    private String correo;
    private String role;
}
