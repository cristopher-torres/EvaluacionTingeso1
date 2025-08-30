package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.DTO.AuthResponse;
import com.ToolRent.ToolRent.DTO.LoginRequest;
import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {
        UserEntity usuario = userRepository.findByEmail(request.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("Correo incorrecto"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        return new AuthResponse(
                usuario.getId(),
                usuario.getName(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }

}
