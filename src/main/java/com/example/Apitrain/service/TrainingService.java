package com.example.Apitrain.service;

import com.example.Apitrain.domain.Training;
import com.example.Apitrain.exception.TrainingNotFoundException;
import com.example.Apitrain.repository.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    private TrainingRepository trainingRepository;

    public List<Training> getAllTrainings() {
        logger.info("Obteniendo todos los entrenamientos de la base de datos");
        List<Training> trainings = trainingRepository.findAll();
        logger.info("Se obtuvieron {} entrenamientos", trainings.size());
        return trainings;
    }

    public Training getTrainingById(Long id) {
        logger.info("Buscando entrenamiento con ID {}", id);
        return trainingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Entrenamiento no encontrado con ID {}", id);
                    return new TrainingNotFoundException("Training not found with id " + id);
                });
    }

    public Training createTraining(Training training) {
        logger.info("Guardando un nuevo entrenamiento en la base de datos");
        Training createdTraining = trainingRepository.save(training);
        logger.info("Entrenamiento creado con éxito: ID {}", createdTraining.getId());
        return createdTraining;
    }

    public Training updateTraining(Long id, Training updatedTraining) {
        logger.info("Actualizando el entrenamiento con ID {}", id);
        Training training = getTrainingById(id);
        training.setTipo(updatedTraining.getTipo());
        training.setNivel(updatedTraining.getNivel());
        training.setDuracion(updatedTraining.getDuracion());
        training.setFecha(updatedTraining.getFecha());
        training.setCompletado(updatedTraining.getCompletado());
        training.setLatitude(updatedTraining.getLatitude());
        training.setLongitude(updatedTraining.getLongitude());
        Training savedTraining = trainingRepository.save(training);
        logger.info("Entrenamiento actualizado con éxito: ID {}", id);
        return savedTraining;
    }

    public void deleteTraining(Long id) {
        logger.info("Eliminando el entrenamiento con ID {}", id);
        getTrainingById(id);
        trainingRepository.deleteById(id);
        logger.info("Entrenamiento eliminado con éxito: ID {}", id);
    }

    public List<Training> filterTrainings(String tipo, String nivel, Integer duracion) {
        logger.info("Filtrando entrenamientos con parámetros: tipo={}, nivel={}, duración>={}", tipo, nivel, duracion);
        List<Training> trainings = trainingRepository.findByTipoContainingAndNivelContainingAndDuracionGreaterThanEqual(
                tipo, nivel, duracion);
        logger.info("Operación completada: Se obtuvieron {} entrenamientos filtrados", trainings.size());
        return trainings;
    }

    public Training updatePartial(Long id, Map<String, Object> updates) {
        logger.info("Actualizando parcialmente el entrenamiento con ID {}", id);
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Entrenamiento no encontrado con ID {}", id);
                    return new TrainingNotFoundException("Training not found with id: " + id);
                });

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Training.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, training, value);
                logger.debug("Campo actualizado: {} con valor {}", key, value);
            }
        });

        Training savedTraining = trainingRepository.save(training);
        logger.info("Entrenamiento actualizado parcialmente con éxito: ID {}", id);
        return savedTraining;
    }
    public List<Training> getByNivelAndDuracion(String nivel, Integer duracionMin) {
        logger.info("Buscando entrenamientos con nivel {} y duración mayor o igual a {}", nivel, duracionMin);
        return trainingRepository.findByNivelAndDuracion(nivel, duracionMin);
    }

}
