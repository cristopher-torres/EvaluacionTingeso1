package com.ToolRent.ToolRent.Service;

import com.ToolRent.ToolRent.Entity.*;
import com.ToolRent.ToolRent.Repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

        // Validar cliente adicional (si tienes lógica extra)
        validateClient(userId);

        // Validar fechas
        if (loan.getStartDate() == null || loan.getScheduledReturnDate() == null) {
            throw new IllegalArgumentException("Se deben ingresar fechas de préstamo y devolución");
        }
        if (loan.getScheduledReturnDate().isBefore(loan.getStartDate())) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser anterior a la fecha de entrega");
        }

        // Obtener unidad disponible desde ToolsService
        ToolsEntity availableUnit = toolsService.getAvailableTool(toolUnitId);

        // Marcar la unidad como prestada
        toolsService.loanTool(toolUnitId);

        // Asociar la unidad al préstamo
        loan.setTool(availableUnit);

        // Verificar que no tenga un préstamo activo de la misma herramienta
        userService.checkDuplicateToolLoan(userId, loan.getTool().getName());

        // Calcular el precio del pretamo
        long days = java.time.temporal.ChronoUnit.DAYS.between(
                loan.getStartDate(),
                loan.getScheduledReturnDate()
        );
        if (days <= 0) {
            days = 1; // mínimo 1 día de cobro
        }
        double price = days * availableUnit.getDailyRate();
        loan.setLoanPrice(price);

        String emailUsuario = userService.getEmailFromToken();

        KardexEntity movement = new KardexEntity();
        movement.setType("PRESTAMO");
        movement.setQuantity(1);
        movement.setTool(loan.getTool());
        movement.setUserEmail(emailUsuario);
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


    public LoanEntity returnLoan(Long loanId,  boolean damaged, boolean irreparable) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (loan.isDelivered()) {
            throw new RuntimeException("El préstamo ya fue devuelto");
        }

        LocalDate today = LocalDate.now();
        loan.setReturnDate(today);
        loan.setDelivered(true);

        // Calcular precio real del préstamo
        long daysUsed = java.time.temporal.ChronoUnit.DAYS.between(
                loan.getStartDate(),
                today
        );
        if (daysUsed <= 0) daysUsed = 1; // mínimo 1 día
        double realPrice = daysUsed * loan.getTool().getDailyRate();
        loan.setLoanPrice(realPrice);

        // Manejar daños
        double damagePrice = 0.0;
        ToolsEntity tool = loan.getTool();
        String emailUsuario = userService.getEmailFromToken();
        if (damaged) {
            if (irreparable) {
                toolsService.decommissionTool(tool.getId());
                damagePrice = tool.getReplacementValue();
            } else {
                tool.setStatus(ToolStatus.EN_REPARACION);

                KardexEntity reparacion = new KardexEntity();
                reparacion.setType("REPARACION");
                reparacion.setTool(loan.getTool());
                reparacion.setUserEmail(emailUsuario);
                reparacion.setDateTime(LocalDateTime.now());
                reparacion.setLoan(loan);
                kardexService.save(reparacion);

                damagePrice = tool.getRepairValue();
            }
        } else {
            // Solo liberar si no hay daño
            toolsService.returnTool(tool.getId());
        }

        loan.setDamagePrice(damagePrice);
        loan.setTotal(loan.getLoanPrice() + damagePrice + loan.getFine());
        loan.setFineTotal(loan.getFine() + damagePrice);
        loan.setLoanStatus("DEVUELTO");


        // Registrar la devolución en Kardex
        KardexEntity devolucion = new KardexEntity();
        devolucion.setType("DEVOLUCION");
        devolucion.setTool(loan.getTool());
        devolucion.setQuantity(1);
        devolucion.setUserEmail(emailUsuario);
        devolucion.setDateTime(LocalDateTime.now());
        devolucion.setLoan(loan);
        kardexService.save(devolucion);

        return loanRepository.save(loan);
    }


    public List<LoanEntity> getAllLoans() {
        LocalDate now = LocalDate.now();
        List<LoanEntity> loans = loanRepository.findAll();

        return loans;
    }


    // Obtener préstamos activos ordenados

    public List<LoanEntity> getActiveLoans() {
        LocalDate now = LocalDate.now();

        // Traer los préstamos vigentes
        List<LoanEntity> loans = loanRepository.findActiveLoansOrderedByDateDesc();

        // Retornar la lista ya actualizada
        return loans;
    }

    public List<LoanEntity> getActiveLoansByDate(LocalDate startDate, LocalDate endDate) {
        // Esta función **solo se usa si se pasan fechas válidas**
        return loanRepository.findActiveLoansByDateRange(startDate, endDate);
    }

    @Scheduled(cron = "0 0 0 * * ?") // todos los días a medianoche
    @Transactional
    public void updateOverdueLoans() {
        LocalDate today = LocalDate.now();
        List<LoanEntity> activeLoans = loanRepository.findActiveLoansOrderedByDateDesc();

        for (LoanEntity loan : activeLoans) {
            if (!loan.isDelivered() && loan.getScheduledReturnDate().isBefore(today)) {
                loan.setLoanStatus("ATRASADO");

                //Restringir al cliente por futuros prestamos
                long client = loan.getClient().getId();
                userService.restrictUserById(client);

                // Calcular días de atraso
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                        loan.getScheduledReturnDate(),
                        today
                );
                if (daysLate < 0) {
                    daysLate = 0;
                }

                // Calcular multa acumulada
                double fine = daysLate * loan.getTool().getDailyLateRate();
                loan.setFine(fine);

                loanRepository.save(loan);
            }
        }
    }

    public LoanEntity updateFinePaid(Long loanId, boolean finePaid) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
        loan.setFinePaid(finePaid);
        userService.updateUserStatus(loan.getClient().getId(), finePaid);
        return loanRepository.save(loan);
    }

    // Clientes con préstamos atrasados - todos
    public List<UserEntity> getClientsWithOverdueLoans(LocalDate today) {
        return loanRepository.findClientsWithOverdueLoansAll(today);
    }

    // Clientes con préstamos atrasados - filtrados por rango de fechas
    public List<UserEntity> getClientsWithOverdueLoans(LocalDate today, LocalDate startDate, LocalDate endDate) {
        return loanRepository.findClientsWithOverdueLoans(today, startDate, endDate);
    }

}
