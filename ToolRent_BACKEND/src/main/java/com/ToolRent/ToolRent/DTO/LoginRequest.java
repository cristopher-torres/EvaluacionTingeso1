package com.ToolRent.ToolRent.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class LoginRequest {
    private String correo;
    private String password;
}
