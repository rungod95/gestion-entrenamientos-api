package com.example.Apitrain.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor



public class TrainingoutDto {
    private Long id;
    private String nombre;
    private String ubicacion;
    private Integer capacidad;
    private LocalDate fechaEvento;
    private boolean completado;

}
