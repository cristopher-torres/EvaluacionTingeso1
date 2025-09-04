package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Guardar un usuario
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    // Obtener todos los usuarios
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }


    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void checkActiveLoans(Long userId) {
        long activeLoans = userRepository.countActiveLoans(userId);
        if (activeLoans >= 5) {
            throw new RuntimeException("El usuario ya tiene 5 prestamos activos no puede tomar otro prestamo.");
        }
    }

    public void checkDuplicateToolLoan(Long userId, Long toolUnitId) {
        if (userRepository.existsActiveLoanForTool(userId, toolUnitId)) {
            throw new RuntimeException("El usuario ya tiene un prestamos activo de esta herramienta");
        }
    }


}
