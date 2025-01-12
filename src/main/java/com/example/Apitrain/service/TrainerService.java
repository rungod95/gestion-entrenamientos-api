package com.example.Apitrain.service;

import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.exception.TrainerNotFoundException;
import com.example.Apitrain.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    public List<Trainer> filterTrainers(String nombre, String especialidad, Integer experiencia) {
        return trainerRepository.findByNombreContainingAndEspecialidadContainingAndExperienciaGreaterThanEqual(nombre, especialidad, experiencia);
    }
    public Trainer updatePartial(Long id, Map<String, Object> updates) {
        Trainer trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not found with id: " + id));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Trainer.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, trainer, value);
            }
        });

        return trainerRepository.save(trainer);
    }
}