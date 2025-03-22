package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.domain.dto.AthleteInDto;
import com.example.Apitrain.service.AthleteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/athletes")
public class AthleteController {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(AthleteController.class);

    @Autowired
    private AthleteService athleteService;

    @GetMapping
    public List<Athlete> getAllAthletes() {
        logger.info("Iniciando operación para obtener todos los atletas");
        List<Athlete> athletes = athleteService.getAllAthletes();
        logger.info("Operación completada: Se obtuvieron {} atletas");
        return athletes;
    }

    @GetMapping("/{id}")
    public Athlete getAthleteById(@PathVariable Long id) {
        logger.info("Iniciando operación para obtener el atleta con ID {}");
        Athlete athlete = athleteService.getAthleteById(id);
        logger.info("Operación completada: Atleta obtenido con ID {}");
        return athlete;
    }

    @PostMapping
    public Athlete createAthlete(@RequestBody Athlete athlete) {
        logger.info("Iniciando operación para crear un atleta");
        Athlete createdAthlete = athleteService.createAthlete(athlete);
        logger.info("Atleta creado con éxito: ID {}");
        return createdAthlete;
    }

    @PutMapping("/{id}")
    public Athlete updateAthlete(@PathVariable Long id, @RequestBody Athlete athlete) {
        logger.info("Iniciando operación para actualizar el atleta con ID {}");
        Athlete updatedAthlete = athleteService.updateAthlete(id, athlete);
        logger.info("Operación completada: Atleta actualizado con ID {}");
        return updatedAthlete;
    }

    @DeleteMapping("/{id}")
    public void deleteAthlete(@PathVariable Long id) {
        logger.info("Iniciando operación para eliminar el atleta con ID {}");
        athleteService.deleteAthlete(id);
        logger.info("Operación completada: Atleta eliminado con ID {}");
    }

    @GetMapping("/filter")
    public List<Athlete> filterAthletes(
            @RequestParam(required = false, defaultValue = "") String nombre,
            @RequestParam(required = false, defaultValue = "") String categoria,
            @RequestParam(required = false, defaultValue = "0") Integer edad) {
        logger.info("Iniciando operación de filtrado con parámetros: nombre={}, categoría={}, edad>={}");
        List<Athlete> filteredAthletes = athleteService.filterAthletes(nombre, categoria, edad);
        logger.info("Operación completada: Se obtuvieron {} atletas filtrados");
        return filteredAthletes;
    }

    @PostMapping("/dto")
    public Athlete createAthlete(@RequestBody @Valid AthleteInDto athleteDto) {
        logger.info("Iniciando operación para crear un atleta a partir de DTO");
        Athlete athlete = new Athlete();
        athlete.setNombre(athleteDto.getNombre());
        athlete.setCategoria(athleteDto.getCategoria());
        athlete.setEdad(athleteDto.getEdad());
        athlete.setAltura(athleteDto.getAltura());

        Athlete createdAthlete = athleteService.createAthlete(athlete);
        logger.info("Atleta creado con éxito a partir de DTO: ID {}");
        return createdAthlete;
    }



    @PatchMapping("/{id}")
    public ResponseEntity<Athlete> updatePartialAthlete(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        logger.info("Iniciando operación de actualización parcial para el atleta con ID {}");
        Athlete updatedAthlete = athleteService.updatePartial(id, updates);
        logger.info("Operación completada: Atleta actualizado parcialmente con ID {}");
        return ResponseEntity.ok(updatedAthlete);
    }

    @GetMapping("/filterByEdad")
    public List<Athlete> getByEdadGreaterThan(
            @RequestParam Integer edadMin) {
        logger.info("Iniciando operación para filtrar atletas con edad>={}");
        List<Athlete> filteredAthletes = athleteService.getByEdadGreaterThan(edadMin);
        logger.info("Operación completada: Se obtuvieron {} atletas filtrados");
        return athleteService.getByEdadGreaterThan(edadMin);
    }

}


