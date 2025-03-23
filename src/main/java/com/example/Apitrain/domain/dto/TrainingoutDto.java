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
    private String tipo;
    private String nivel;
    private Integer duracion;
    private LocalDate fecha;
}