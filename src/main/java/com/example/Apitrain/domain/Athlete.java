package com.example.Apitrain.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Athlete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private String category;
    @ManyToMany(mappedBy = "athletes")
    private List<Train> trains;
}
