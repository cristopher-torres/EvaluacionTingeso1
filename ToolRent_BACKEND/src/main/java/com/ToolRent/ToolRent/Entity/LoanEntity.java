package com.ToolRent.ToolRent.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(name = "tool_unit_id", nullable = false)
    @JsonBackReference
    private ToolUnitEntity toolUnit;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference("client-loans")
    private UserEntity client;

    @Column(nullable = false)
    private Date startDate;    //La fecha en que el cliente retira la herramienta.

    @Column(nullable = false)
    private Date scheduledReturnDate; //La fecha límite para devolver la herramienta.

    private Date returnDate;   //La fecha en que el cliente devuelve la herramienta.

    private boolean delivered = false;

    @Column(nullable = false)
    private LocalDateTime createdLoan;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    @JsonBackReference("created-loans")
    private UserEntity createdBy;
}
