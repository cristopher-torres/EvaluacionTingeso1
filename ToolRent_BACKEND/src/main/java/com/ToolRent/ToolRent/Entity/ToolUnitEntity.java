package com.ToolRent.ToolRent.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tool_units")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolUnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ToolStatus status;

    @ManyToOne
    @JoinColumn(name = "tool_id", nullable = false)
    @JsonBackReference
    private ToolsEntity tool;

    @OneToMany(mappedBy = "toolUnit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<LoanEntity> loans;

}
