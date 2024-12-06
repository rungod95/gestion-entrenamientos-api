package com.example.Apitrain.controller;

import org.springframework.web.bind.annotation.*;
import com.example.Apitrain.domain.Training;
import com.example.Apitrain.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    @Autowired
        private TrainingService trainingService;

    @GetMapping
        public List<Training> getAllTrainings() {
            return trainingService.getAllTrainings();
        }

        @GetMapping("/{id}")
        public Training getTrainingById(@PathVariable Long id) {
            return trainingService.getTrainingById(id);
        }

        @PostMapping
        public Training createTraining(@RequestBody Training training) {
            return trainingService.createTraining(training);
        }

    @PutMapping("/{id}")
    public Training updateTraining(@PathVariable Long id, @RequestBody Training updatedTraining) {
        return trainingService.updateTraining(id, updatedTraining);
    }

    @DeleteMapping("/{id}")
        public void deleteTraining(@PathVariable Long id) {
            trainingService.deleteTraining(id);
        }

    @GetMapping("/filter")
    public List<Training> filterTrainings(
            @RequestParam(required = false, defaultValue = "") String tipo,
            @RequestParam(required = false, defaultValue = "") String nivel,
            @RequestParam(required = false, defaultValue = "0") Integer duracion) {
        return trainingService.filterTrainings(tipo, nivel, duracion);
    }
}
