package com.example.Apitrain.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @Column(name = "tipo")
    private String tipo;
    @NotBlank
    @Column(name = "nivel", nullable = false)
    private String nivel;

    @Column(name = "duracion")
    private Integer duracion;

    @Column(name = "completado")
    private Boolean completado;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @ManyToMany
    @JoinTable(
            name = "training_athletes",
            joinColumns = @JoinColumn(name = "training_id"),
            inverseJoinColumns = @JoinColumn(name = "athlete_id")
    )
    @JsonIgnore
    private List<Athlete> athletes;
}
