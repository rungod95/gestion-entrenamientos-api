package com.example.Apitrain.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String speciality;
    @Column
    private int Experience;
    @OneToMany
    private List<Train> trains;

}
