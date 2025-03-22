package com.example.Apitrain.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "athletes")
public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Marca como requerido
    private String nombre;

    @Column(nullable = false) // Marca como requerido
    private String categoria;

    @Column
    private Integer edad;

    @Column
    private Float altura;

    @Column(name = "fecha_registro") // Mapea "fechaRegistro" a "fecha_registro" en la tabla
    private LocalDate fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "trainer_id") // Configura la relación con la tabla "Trainer"
    private Trainer trainer;
}