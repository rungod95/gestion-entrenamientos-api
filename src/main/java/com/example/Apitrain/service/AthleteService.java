package com.example.Apitrain.service;

import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.exception.AthleteNotFoundException;
import com.example.Apitrain.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AthleteService {
    @Autowired
    private AthleteRepository athleteRepository;

    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    public Athlete getAthleteById(Long id) {
        return athleteRepository.findById(id)
                .orElseThrow(() -> new AthleteNotFoundException("Athlete not found with id " + id));
    }

    public Athlete createAthlete(Athlete athlete) {
        return athleteRepository.save(athlete);
    }

    public Athlete updateAthlete(Long id, Athlete updatedAthlete) {
        Athlete athlete = getAthleteById(id);
        athlete.setNombre(updatedAthlete.getNombre());
        athlete.setCategoria(updatedAthlete.getCategoria());
        athlete.setEdad(updatedAthlete.getEdad());
        athlete.setAltura(updatedAthlete.getAltura());
        athlete.setFechaRegistro(updatedAthlete.getFechaRegistro());
        return athleteRepository.save(athlete);
    }

    public void deleteAthlete(Long id) {
        athleteRepository.deleteById(id);
    }
}
