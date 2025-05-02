package com.example.Apitrain.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String nombre;

    @Column(nullable = false)
    @NotBlank
    private String ubicacion;

    @Column
    private Integer capacidad;

    @Column(name = "fecha_evento")
    private LocalDate fechaEvento;


    @ManyToOne
    @JoinColumn(name = "facility_id")
    @JsonBackReference
    @NotNull(message = "La instalación es obligatoria")
    private Facility facility;
}