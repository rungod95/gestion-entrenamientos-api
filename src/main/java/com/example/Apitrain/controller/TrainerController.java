package com.example.Apitrain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Apitrain.service.TrainerService;
import com.example.Apitrain.domain.Trainer;

import java.util.List;

@RestController
@RequestMapping("/trainers")

public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @GetMapping
    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }

    @GetMapping("/{id}")
    public Trainer getTrainerById(@PathVariable Long id) {
        return trainerService.getTrainerById(id);
    }

    @PostMapping
    public Trainer createTrainer(@RequestBody Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    @PutMapping("/{id}")
    public Trainer updateTrainer(@PathVariable Long id, @RequestBody Trainer updatedTrainer) {
        return trainerService.updateTrainer(id, updatedTrainer);
    }

    @DeleteMapping("/{id}")
    public void deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
    }
}
