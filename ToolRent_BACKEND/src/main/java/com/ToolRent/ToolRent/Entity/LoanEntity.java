package com.ToolRent.ToolRent.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "loans")
@Data
@NoArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tool_id")
    private ToolsEntity tool;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private UserEntity client;

    @Column(nullable = false)
    private Date startDate;    //La fecha en que el cliente retira la herramienta.

    @Column(nullable = false)
    private Date deliveryDate; //La fecha límite para devolver la herramienta.

    private Date returnDate;   //La fecha en que el cliente devuelve la herramienta.

    private Double delayFine;
    private Double damageCharge;
    private String damageLevel;

    private Boolean paid;

    @Column(nullable = false)
    private LocalDateTime createdLoan;

    @Column(nullable = false)
    private String createdBy;
}
