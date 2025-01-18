package com.example.Apitrain.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Apitrain.service.TrainerService;
import com.example.Apitrain.domain.Trainer;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainers")
public class TrainerController {
    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);

    @Autowired
    private TrainerService trainerService;

    @GetMapping
    public List<Trainer> getAllTrainers() {
        logger.info("Iniciando operación para obtener todos los entrenadores");
        List<Trainer> trainers = trainerService.getAllTrainers();
        logger.info("Operación completada: Se obtuvieron {} entrenadores", trainers.size());
        return trainers;
    }

    @GetMapping("/{id}")
    public Trainer getTrainerById(@PathVariable Long id) {
        logger.info("Iniciando operación para obtener el entrenador con ID {}", id);
        Trainer trainer = trainerService.getTrainerById(id);
        logger.info("Operación completada: Entrenador obtenido con ID {}", id);
        return trainer;
    }

    @PostMapping
    public Trainer createTrainer(@RequestBody Trainer trainer) {
        logger.info("Iniciando operación para crear un entrenador");
        try {
            Trainer createdTrainer = trainerService.createTrainer(trainer);
            logger.info("Entrenador creado con éxito: ID {}", createdTrainer.getId());
            return createdTrainer;
        } catch (Exception e) {
            logger.error("Error al crear el entrenador", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public Trainer updateTrainer(@PathVariable Long id, @RequestBody Trainer updatedTrainer) {
        logger.info("Iniciando operación para actualizar el entrenador con ID {}", id);
        Trainer trainer = trainerService.updateTrainer(id, updatedTrainer);
        logger.info("Operación completada: Entrenador actualizado con ID {}", id);
        return trainer;
    }

    @DeleteMapping("/{id}")
    public void deleteTrainer(@PathVariable Long id) {
        logger.info("Iniciando operación para eliminar el entrenador con ID {}", id);
        trainerService.deleteTrainer(id);
        logger.info("Operación completada: Entrenador eliminado con ID {}", id);
    }

    @GetMapping("/filter")
    public List<Trainer> filterTrainers(
            @RequestParam(required = false, defaultValue = "") String nombre,
            @RequestParam(required = false, defaultValue = "") String especialidad,
            @RequestParam(required = false, defaultValue = "0") Integer experiencia) {
        logger.info("Iniciando operación para filtrar entrenadores con parámetros: nombre={}, especialidad={}, experiencia>={}", nombre, especialidad, experiencia);
        List<Trainer> filteredTrainers = trainerService.filterTrainers(nombre, especialidad, experiencia);
        logger.info("Operación completada: Se obtuvieron {} entrenadores filtrados", filteredTrainers.size());
        return filteredTrainers;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Trainer> updatePartialTrainer(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        logger.info("Iniciando operación para actualización parcial del entrenador con ID {}", id);
        Trainer updatedTrainer = trainerService.updatePartial(id, updates);
        logger.info("Operación completada: Entrenador actualizado parcialmente con ID {}", id);
        return ResponseEntity.ok(updatedTrainer);
    }
    @GetMapping("/filterByExperiencia")
    public List<Trainer> getTrainersByExperienciaGreaterThan(@RequestParam Integer experiencia) {
        logger.info("Iniciando operación para obtener entrenadores con experiencia mayor a {}", experiencia);
        List<Trainer> trainers = trainerService.getTrainersByExperienciaGreaterThan(experiencia);
        logger.info("Operación completada: Se obtuvieron {} entrenadores con experiencia mayor a {}", trainers.size(), experiencia);
        return trainerService.getTrainersByExperienciaGreaterThan(experiencia);
    }


}
