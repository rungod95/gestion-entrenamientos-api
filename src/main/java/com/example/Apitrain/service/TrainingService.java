package com.example.Apitrain.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.repository.TrainingRepository;
import com.example.Apitrain.domain.Training;
import com.example.Apitrain.exception.TrainingNotFoundException;

import java.util.List;

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
            return trainingRepository.save(training);
        }

        public void deleteTraining(Long id) {
            trainingRepository.deleteById(id);
        }

    public List<Training> filterTrainings(String tipo, String nivel, Integer duracion) {
        return trainingRepository.findByTipoContainingAndNivelContainingAndDuracionGreaterThanEqual(
                tipo, nivel, duracion);
        }
}
