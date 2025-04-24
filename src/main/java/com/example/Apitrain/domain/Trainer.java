package com.example.Apitrain.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers")

public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String nombre;
    @Column(nullable = false)
    @NotBlank
    private String especialidad;
    @Column
    private Integer experiencia;
    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;
    @Column
    private Boolean activo;
    @OneToMany(mappedBy = "trainer")
    private List<Athlete> athletes;


}
