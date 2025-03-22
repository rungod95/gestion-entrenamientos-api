package com.example.Apitrain.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AthleteInDto {

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;
    @NotNull(message = "La categoria no puede ser nula")
    private String categoria;
    @NotNull(message = "La edad no puede ser nula")
    @NotNull(message = "La edad no puede ser nula")
    private Integer edad;
    @NotNull(message = "La altura no puede ser nula")
    private Float altura;


}
