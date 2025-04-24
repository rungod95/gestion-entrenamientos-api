package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Training;
import com.example.Apitrain.domain.dto.Trainingindto;
import com.example.Apitrain.exception.TrainingNotFoundException;
import com.example.Apitrain.service.TrainingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainings")
public class TrainingController {
    private static final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    @Autowired
    private TrainingService trainingService;

    @GetMapping
    public List<Training> getAllTrainings() {
        logger.info("Iniciando operación para obtener todos los entrenamientos");
        List<Training> trainings = trainingService.getAllTrainings();
        logger.info("Operación completada: Se obtuvieron {} entrenamientos", trainings.size());
        return trainings;
    }

    @GetMapping("/{id}")
    public Training getTrainingById(@PathVariable Long id) {
        logger.info("Iniciando operación para obtener el entrenamiento con ID {}", id);
        try {
            Training training = trainingService.getTrainingById(id);
            logger.info("Operación completada: Entrenamiento obtenido con ID {}", id);
            return training;
        } catch (RuntimeException e) {
            logger.error("Error al obtener el entrenamiento con ID {}: {}", id, e.getMessage());
            throw new TrainingNotFoundException("No se encontró el entrenamiento con ID " + id);
        }
    }

    @PostMapping
    public ResponseEntity<?> createTraining(@RequestBody Training training) {
        logger.info("Iniciando operación para crear un entrenamiento");
        
        // Validar datos requeridos
        if (training == null) {
            return ResponseEntity.badRequest().body("El entrenamiento no puede ser nulo");
        }
        
        if (training.getTipo() == null || training.getTipo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El tipo de entrenamiento es requerido");
        }
        
        if (training.getNivel() == null || training.getNivel().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nivel de entrenamiento es requerido");
        }
        
        if (training.getDuracion() == null || training.getDuracion() <= 0) {
            return ResponseEntity.badRequest().body("La duración debe ser mayor que 0");
        }

        try {
            Training createdTraining = trainingService.createTraining(training);
            logger.info("Entrenamiento creado con éxito: ID {}", createdTraining.getId());
            return ResponseEntity.ok(createdTraining);
        } catch (Exception e) {
            logger.error("Error al crear el entrenamiento: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error al crear el entrenamiento: " + e.getMessage());
        }
    }

    @PostMapping("/dto")
    public Training createTraining(@RequestBody @Valid Trainingindto trainingDto) {
        logger.info("Iniciando operación para crear un entrenamiento desde un DTO");
        // Convertir el DTO a la entidad
        Training training = new Training();
        training.setTipo(trainingDto.getTipo());
        training.setNivel(trainingDto.getNivel());
        training.setDuracion(trainingDto.getDuracion());
        training.setCompletado(trainingDto.getCompletado());
        training.setFecha(trainingDto.getFecha());
        training.setLatitude(trainingDto.getLatitude());
        training.setLongitude(trainingDto.getLongitude());

        Training createdTraining = trainingService.createTraining(training);
        logger.info("Entrenamiento creado desde DTO con éxito: ID {}", createdTraining.getId());
        return createdTraining;
    }

    @PutMapping("/{id}")
    public Training updateTraining(@PathVariable Long id, @RequestBody Training updatedTraining) {
        logger.info("Iniciando operación para actualizar el entrenamiento con ID {}", id);
        try {
            Training training = trainingService.updateTraining(id, updatedTraining);
            logger.info("Operación completada: Entrenamiento actualizado con ID {}", id);
            return training;
        } catch (RuntimeException e) {
            logger.error("Error al actualizar el entrenamiento con ID {}: {}", id, e.getMessage());
            throw new TrainingNotFoundException("No se encontró el entrenamiento con ID " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        logger.info("Iniciando operación para eliminar el entrenamiento con ID {}", id);
        try {
            trainingService.deleteTraining(id);
            logger.info("Operación completada: Entrenamiento eliminado con ID {}", id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Error al eliminar el entrenamiento con ID {}: {}", id, e.getMessage());
            throw new TrainingNotFoundException("No se encontró el entrenamiento con ID " + id);
        }
    }

    @GetMapping("/filter")
    public List<Training> filterTrainings(
            @RequestParam(required = false, defaultValue = "") String tipo,
            @RequestParam(required = false, defaultValue = "") String nivel,
            @RequestParam(required = false, defaultValue = "0") Integer duracion) {
        logger.info("Iniciando operación para filtrar entrenamientos con parámetros: tipo={}, nivel={}, duración>={}", tipo, nivel, duracion);
        List<Training> trainings = trainingService.filterTrainings(tipo, nivel, duracion);
        logger.info("Operación completada: Se obtuvieron {} entrenamientos filtrados", trainings.size());
        return trainings;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Training> updatePartialTraining(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        logger.info("Iniciando operación para actualización parcial del entrenamiento con ID {}", id);
        try {
            Training updatedTraining = trainingService.updatePartial(id, updates);
            logger.info("Operación completada: Entrenamiento actualizado parcialmente con ID {}", id);
            return ResponseEntity.ok(updatedTraining);
        } catch (RuntimeException e) {
            logger.error("Error en la actualización parcial del entrenamiento con ID {}: {}", id, e.getMessage());
            throw new TrainingNotFoundException("No se encontró el entrenamiento con ID " + id);
        }
    }

    @GetMapping("/filterByNivelAndDuracion")
    public List<Training> getByNivelAndDuracion(
            @RequestParam String nivel,
            @RequestParam Integer duracion) {
        logger.info("Iniciando operación para filtrar entrenamientos por nivel y duración");
        return trainingService.getByNivelAndDuracion(nivel, duracion);
    }
}
