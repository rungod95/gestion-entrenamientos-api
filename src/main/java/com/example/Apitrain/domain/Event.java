package com.example.Apitrain.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ubicacion;

    @Column
    private Integer capacidad;
    @Column(name = "fecha_evento")
    private LocalDate fechaEvento;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility; // Relación con Facility
}