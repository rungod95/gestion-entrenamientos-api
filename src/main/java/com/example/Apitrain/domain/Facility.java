package com.example.Apitrain.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facilities")
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo;

    @Column
    private Integer capacidad;

    @Column(name = "horario")
    private String horario;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @OneToMany(mappedBy = "facility")
    private List<Event> events; // Relación con Event
}
