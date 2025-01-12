package com.example.Apitrain.service;

import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.exception.TrainerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.repository.TrainingRepository;
import com.example.Apitrain.domain.Training;
import com.example.Apitrain.exception.TrainingNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class TrainingService {

        @Autowired
        private TrainingRepository trainingRepository;

        public List<Training> getAllTrainings() {
            return trainingRepository.findAll();
        }

        public Training getTrainingById(Long id) {
            return trainingRepository.findById(id)
                    .orElseThrow(() -> new TrainingNotFoundException("Training not found with id " + id));
        }

        public Training createTraining(Training training) {
            return trainingRepository.save(training);
        }

    public Training updateTraining(Long id, Training updatedTraining) {
        Training training = getTrainingById(id);
        training.setTipo(updatedTraining.getTipo());
        training.setNivel(updatedTraining.getNivel());
        training.setDuracion(updatedTraining.getDuracion());
        training.setFecha(updatedTraining.getFecha());
        training.setCompletado(updatedTraining.getCompletado());
        training.setLatitude(updatedTraining.getLatitude());
        training.setLongitude(updatedTraining.getLongitude());
        return trainingRepository.save(training);
    }

        public void deleteTraining(Long id) {
            trainingRepository.deleteById(id);
        }

    public List<Training> filterTrainings(String tipo, String nivel, Integer duracion) {
        return trainingRepository.findByTipoContainingAndNivelContainingAndDuracionGreaterThanEqual(
                tipo, nivel, duracion);
        }
    public Training updatePartial(Long id, Map<String, Object> updates) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new TrainingNotFoundException("Trainer not found with id: " + id));
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Training.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, training, value);
            }
        });

        return trainingRepository.save(training);
    }
}
