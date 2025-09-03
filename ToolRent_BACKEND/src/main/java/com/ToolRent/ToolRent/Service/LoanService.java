package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.LoanEntity;
import com.ToolRent.ToolRent.Entity.ToolStatus;
import com.ToolRent.ToolRent.Entity.ToolsEntity;
import com.ToolRent.ToolRent.Entity.UserEntity;
import com.ToolRent.ToolRent.Repository.LoanRepository;
import com.ToolRent.ToolRent.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ToolsService toolsService;

    @Transactional
    public LoanEntity registerLoan(LoanEntity loan){
        if(loan.getClient() == null){
            throw new IllegalArgumentException("Se debe ingresar el cliente");
        }
        if(loan.getTool() == null){
            throw new IllegalArgumentException("Se debe ingresar una herramienta");
        }
        if(loan.getStartDate() == null){
            throw new IllegalArgumentException("Se debe ingresar la fecha de entrega de la herramienta");
        }
        if(loan.getScheduledReturnDate() == null){
            throw new IllegalArgumentException("Se debe ingresar la fecha pactada de la devolución de la herramienta");
        }
        if (loan.getScheduledReturnDate().before(loan.getStartDate())) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior a la fecha de entrega");
        }

        validateClient(loan.getClient().getId());
        validateToolStock(loan.getTool().getId());
        validateToolStatus(loan.getTool().getId());

        return loanRepository.save(loan);
    }

    private void validateClient(long userId){
        UserEntity user = userService.findById(userId);
        String status = user.getStatus();
        if (!"Activo".equalsIgnoreCase(status)) {
            throw new RuntimeException("El cliente no esta disponible para realizar un prestamo");
        }
    }

    private void validateToolStock(long toolId){
        ToolsEntity tool = toolsService.findById(toolId);
        if (tool.getStock() <= 0) {
            throw new RuntimeException("La Herramienta no tiene stock suficiente");
        }
    }

    private void validateToolStatus(long toolId) {
        ToolsEntity tool = toolsService.findById(toolId);

        if (tool.getStatus() != ToolStatus.DISPONIBLE) {
            throw new RuntimeException("La herramienta no está disponible para préstamo");
        }
    }
}
