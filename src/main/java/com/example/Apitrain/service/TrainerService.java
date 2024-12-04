package com.example.Apitrain.service;

import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.exception.TrainerNotFoundException;
import com.example.Apitrain.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id " + id));
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public Trainer updateTrainer(Long id, Trainer updatedTrainer) {
        Trainer trainer = getTrainerById(id);
        trainer.setNombre(updatedTrainer.getNombre());
        trainer.setEspecialidad(updatedTrainer.getEspecialidad());
        trainer.setExperiencia(updatedTrainer.getExperiencia());
        trainer.setFechaIngreso(updatedTrainer.getFechaIngreso());
        trainer.setActivo(updatedTrainer.getActivo());
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Long id) {
        Trainer trainer = getTrainerById(id);
        trainerRepository.deleteById(id);
    }
}