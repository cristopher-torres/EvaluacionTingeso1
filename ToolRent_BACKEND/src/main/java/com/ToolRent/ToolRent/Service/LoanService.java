package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.*;
import com.ToolRent.ToolRent.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ToolsService toolsService;

    @Autowired
    private KardexService kardexService;

    @Transactional
    public LoanEntity createLoan(LoanEntity loan) {

        if (loan.getClient() == null) {
            throw new IllegalArgumentException("Se debe ingresar un cliente");
        }
        if (loan.getTool() == null || loan.getTool().getId() == null) {
            throw new IllegalArgumentException("Se debe ingresar una herramienta válida");
        }

        Long userId = loan.getClient().getId();
        Long toolUnitId = loan.getTool().getId();

        // Verificar que no tenga más de 5 préstamos activos
        userService.checkActiveLoans(userId);

        // Verificar que no tenga un préstamo activo de la misma herramienta
        userService.checkDuplicateToolLoan(userId, toolUnitId);

        // Validar cliente adicional (si tienes lógica extra)
        validateClient(userId);

        // Validar fechas
        if (loan.getStartDate() == null || loan.getScheduledReturnDate() == null) {
            throw new IllegalArgumentException("Se deben ingresar fechas de préstamo y devolución");
        }
        if (loan.getScheduledReturnDate().before(loan.getStartDate())) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior a la fecha de entrega");
        }

        // Obtener unidad disponible desde ToolsService
        ToolsEntity availableUnit = toolsService.getAvailableTool(toolUnitId);

        // Marcar la unidad como prestada
        toolsService.loanTool(toolUnitId);

        // Asociar la unidad al préstamo
        loan.setTool(availableUnit);

        KardexEntity movement = new KardexEntity();
        movement.setType("PRESTAMO");
        movement.setQuantity(1);
        movement.setTool(loan.getTool());
        movement.setUser(loan.getClient());
        movement.setDateTime(LocalDateTime.now());
        movement.setLoan(loan);
        kardexService.save(movement);


        return loanRepository.save(loan);
    }


    private void validateClient(long userId){
        UserEntity user = userService.findById(userId);
        String status = user.getStatus();
        if (!"Activo".equalsIgnoreCase(status)) {
            throw new RuntimeException("El cliente no esta disponible para realizar un prestamo");
        }
    }


    public LoanEntity returnLoan(Long loanId){
        // Buscar el préstamo en la base de datos
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        // Validar que no haya sido devuelto
        if (loan.isDelivered()) {
            throw new RuntimeException("El préstamo ya fue devuelto");
        }

        // Fecha actual de devolución
        Date returnDate = new Date();
        loan.setReturnDate(returnDate);
        loan.setDelivered(true);

        long itemTool = loan.getTool().getId();

        // Marcar la unidad como disponible
        toolsService.returnTool(itemTool);

        KardexEntity movement = new KardexEntity();
        movement.setType("DEVOLUCION");
        movement.setQuantity(1);
        movement.setTool(loan.getTool());
        movement.setDateTime(LocalDateTime.now());
        kardexService.save(movement);

        return loanRepository.save(loan);

    }


    public List<LoanEntity> getAllLoans() {
        return loanRepository.findAll();
    }

    // Obtener préstamos activos ordenados
    public List<LoanEntity> getActiveLoans() {
        return loanRepository.findActiveLoansOrderedByDateDesc();
    }
}
