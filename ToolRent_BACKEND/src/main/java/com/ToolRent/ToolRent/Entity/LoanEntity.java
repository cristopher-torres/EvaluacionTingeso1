package com.ToolRent.ToolRent.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "loans")
@Data
@Getter
@NoArgsConstructor
public class LoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tool_id", nullable = false)
    @JsonBackReference
    private ToolsEntity tool;

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

    private double fine;
    private double loanPrice;

    @Column(nullable = false)
    private LocalDateTime createdLoan;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "loan-kardex")
    private List<KardexEntity> kardexMovements;
}
