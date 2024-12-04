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
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String Tipo;
    @Column(nullable = false)
    private String Nivel;
    @Column
    private Integer Duracion;// duration en minutos
    @Column
    private Boolean Completado;
    @Column
    private LocalDate Fecha;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToMany
    @JoinTable(
        name = "training_athletes",
        joinColumns = @JoinColumn(name = "training_id"),
        inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    private List<Athlete> athletes;
    // getters y setters
}
