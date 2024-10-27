package com.example.Apitrain.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String type;
    @Column
    private LocalDate date;
    @Column
    private int duration;// duration en minutos
    @Column
    private String level;
    @ManyToOne
    private Trainer trainer;
    @ManyToMany
    private List<Athlete> athletes;
    // getters y setters
}
