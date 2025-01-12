package com.example.Apitrain.service;

import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.exception.AthleteNotFoundException;
import com.example.Apitrain.exception.TrainerNotFoundException;
import com.example.Apitrain.repository.AthleteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


@Service
public class AthleteService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AthleteService.class);

    @Autowired
    private AthleteRepository athleteRepository;

    public List<Athlete> getAllAthletes() {
        logger.info("Obteniendo todos los atletas de la base de datos");
        List<Athlete> athletes = athleteRepository.findAll();
        logger.info("Se obtuvieron {} atletas");
        return athletes;
    }

    public Athlete getAthleteById(Long id) {
        logger.info("Buscando atleta con ID {}");
        return athleteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Atleta no encontrado con ID {}", id);
                    return new AthleteNotFoundException("Athlete not found with id " + id);
                });
    }

    public Athlete createAthlete(Athlete athlete) {
        logger.info("Guardando un nuevo atleta en la base de datos");
        return athleteRepository.save(athlete);
    }

    public Athlete updateAthlete(Long id, Athlete updatedAthlete) {
        logger.info("Actualizando el atleta con ID {}");
        Athlete athlete = getAthleteById(id);
        athlete.setNombre(updatedAthlete.getNombre());
        athlete.setCategoria(updatedAthlete.getCategoria());
        athlete.setEdad(updatedAthlete.getEdad());
        athlete.setAltura(updatedAthlete.getAltura());
        athlete.setFechaRegistro(updatedAthlete.getFechaRegistro());
        return athleteRepository.save(athlete);
    }

    public void deleteAthlete(Long id) {
        logger.info("Eliminando el atleta con ID {}");
        athleteRepository.deleteById(id);
    }

    public List<Athlete> filterAthletes(String nombre, String categoria, Integer edad) {
        logger.info("Filtrando atletas con parámetros: nombre={}, categoría={}, edad>={}");
        return athleteRepository.findByNombreContainingAndCategoriaContainingAndEdadGreaterThanEqual(nombre, categoria, edad);
    }

    public Athlete updatePartial(Long id, Map<String, Object> updates) {
        logger.info("Actualizando parcialmente el atleta con ID {}");
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Atleta no encontrado con ID {}", id);
                    return new AthleteNotFoundException("Athlete not found with id: " + id);
                });

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Athlete.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, athlete, value);
                logger.debug("Campo actualizado: {} con valor {}", key, value);
            }
        });

        return athleteRepository.save(athlete);
    }
}
