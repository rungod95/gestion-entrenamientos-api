package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/athletes")
public class AthleteController {
    @Autowired
    private AthleteService athleteService;

    @GetMapping
    public List<Athlete> getAllAthletes() {
        return athleteService.getAllAthletes();
    }

    @GetMapping("/{id}")
    public Athlete getAthleteById(@PathVariable Long id) {
        return athleteService.getAthleteById(id);
    }

    @PostMapping
    public Athlete createAthlete(@RequestBody Athlete athlete) {
        return athleteService.createAthlete(athlete);
    }

    @PutMapping("/{id}")
    public Athlete updateAthlete(@PathVariable Long id, @RequestBody Athlete athlete) {
        return athleteService.updateAthlete(id, athlete);
    }

    @DeleteMapping("/{id}")
    public void deleteAthlete(@PathVariable Long id) {
        athleteService.deleteAthlete(id);
    }

    @GetMapping("/filter")
    public List<Athlete> filterAthletes(
            @RequestParam(required = false, defaultValue = "") String nombre,
            @RequestParam(required = false, defaultValue = "") String categoria,
            @RequestParam(required = false, defaultValue = "0") Integer edad) {
        return athleteService.filterAthletes(nombre, categoria, edad);
    }
}
