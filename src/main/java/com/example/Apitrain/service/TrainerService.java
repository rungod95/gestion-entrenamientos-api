package com.example.Apitrain.service;

import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.exception.TrainerNotFoundException;
import com.example.Apitrain.repository.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerRepository trainerRepository;

    public List<Trainer> getAllTrainers() {
        logger.info("Iniciando operación para obtener todos los entrenadores");
        List<Trainer> trainers = trainerRepository.findAll();
        logger.info("Operación completada: Se obtuvieron {} entrenadores", trainers.size());
        return trainers;
    }

    public Trainer getTrainerById(Long id) {
        logger.info("Buscando entrenador con ID {}", id);
        return trainerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Entrenador no encontrado con ID {}", id);
                    return new TrainerNotFoundException("Trainer not found with id " + id);
                });
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Iniciando operación para crear un entrenador");
        Trainer createdTrainer = trainerRepository.save(trainer);
        logger.info("Entrenador creado con éxito: ID {}", createdTrainer.getId());
        return createdTrainer;
    }

    public Trainer updateTrainer(Long id, Trainer updatedTrainer) {
        logger.info("Iniciando operación para actualizar el entrenador con ID {}", id);
        Trainer trainer = getTrainerById(id);
        trainer.setNombre(updatedTrainer.getNombre());
        trainer.setEspecialidad(updatedTrainer.getEspecialidad());
        trainer.setExperiencia(updatedTrainer.getExperiencia());
        trainer.setFechaIngreso(updatedTrainer.getFechaIngreso());
        trainer.setActivo(updatedTrainer.getActivo());
        Trainer savedTrainer = trainerRepository.save(trainer);
        logger.info("Entrenador actualizado con éxito: ID {}", id);
        return savedTrainer;
    }

    public void deleteTrainer(Long id) {
        logger.info("Iniciando operación para eliminar el entrenador con ID {}", id);
        Trainer trainer = getTrainerById(id);
        trainerRepository.deleteById(id);
        logger.info("Entrenador eliminado con éxito: ID {}", id);
    }

    public List<Trainer> filterTrainers(String nombre, String especialidad, Integer experiencia) {
        logger.info("Iniciando operación de filtrado con parámetros: nombre={}, especialidad={}, experiencia>={}", nombre, especialidad, experiencia);
        List<Trainer> trainers = trainerRepository.findByNombreContainingAndEspecialidadContainingAndExperienciaGreaterThanEqual(nombre, especialidad, experiencia);
        logger.info("Operación completada: Se obtuvieron {} entrenadores filtrados", trainers.size());
        return trainers;
    }

    public Trainer updatePartial(Long id, Map<String, Object> updates) {
        logger.info("Iniciando operación para actualización parcial del entrenador con ID {}", id);
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Entrenador no encontrado con ID {}", id);
                    return new TrainerNotFoundException("Trainer not found with id: " + id);
                });

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Trainer.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, trainer, value);
                logger.debug("Campo actualizado: {} con valor {}", key, value);
            }
        });

        Trainer savedTrainer = trainerRepository.save(trainer);
        logger.info("Entrenador actualizado parcialmente con éxito: ID {}", id);
        return savedTrainer;
    }
}
