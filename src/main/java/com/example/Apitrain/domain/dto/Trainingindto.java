package com.example.Apitrain.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class Trainingindto {

    @NotNull(message = "El campo tipo es obligatorio")
    private String tipo;

    @NotNull(message = "El nivel es obligatorio")
    private String nivel;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private Integer duracion;

    private Boolean completado;

    @NotNull(message = "La fecha es obligatoria")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    private Double latitude;

    private Double longitude;

    // Getters y setters
}
