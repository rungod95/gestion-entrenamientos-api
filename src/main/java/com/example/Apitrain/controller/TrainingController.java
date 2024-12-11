package com.example.Apitrain.controller;

import com.example.Apitrain.domain.dto.Trainingindto;
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
    @PostMapping("/dto")
    public Training createTraining(@RequestBody Trainingindto trainingDto) {
        // Convertir el DTO a la entidad
        Training training = new Training();
        training.setTipo(trainingDto.getTipo());
        training.setNivel(trainingDto.getNivel());
        training.setDuracion(trainingDto.getDuracion());
        training.setCompletado(trainingDto.getCompletado());
        training.setFecha(trainingDto.getFecha());
        training.setLatitude(trainingDto.getLatitude());
        training.setLongitude(trainingDto.getLongitude());

        // Delegar la creación al servicio
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
