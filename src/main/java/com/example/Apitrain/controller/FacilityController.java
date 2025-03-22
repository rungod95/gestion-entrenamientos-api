package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Facility;
import com.example.Apitrain.service.FacilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/facilities")
public class FacilityController {
    private static final Logger logger = LoggerFactory.getLogger(FacilityController.class);

    @Autowired
    private FacilityService facilityService;

    @GetMapping
    public List<Facility> getAllFacilities() {
        logger.info("Iniciando operación para obtener todas las instalaciones");
        List<Facility> facilities = facilityService.getAllFacilities();
        logger.info("Operación completada: Se obtuvieron {} instalaciones", facilities.size());
        return facilities;
    }

    @GetMapping("/{id}")
    public Facility getFacilityById(@PathVariable Long id) {
        logger.info("Iniciando operación para obtener la instalación con ID {}", id);
        Facility facility = facilityService.getFacilityById(id);
        logger.info("Operación completada: Instalación obtenida con ID {}", id);
        return facility;
    }

    @PostMapping
    public Facility createFacility(@RequestBody Facility facility) {
        logger.info("Iniciando operación para crear una instalación");
        Facility createdFacility = facilityService.createFacility(facility);
        logger.info("Instalación creada con éxito: ID {}", createdFacility.getId());
        return createdFacility;
    }

    @PutMapping("/{id}")
    public Facility updateFacility(@PathVariable Long id, @RequestBody Facility facility) {
        logger.info("Iniciando operación para actualizar la instalación con ID {}", id);
        Facility updatedFacility = facilityService.updateFacility(id, facility);
        logger.info("Operación completada: Instalación actualizada con ID {}", id);
        return updatedFacility;
    }

    @DeleteMapping("/{id}")
    public void deleteFacility(@PathVariable Long id) {
        logger.info("Iniciando operación para eliminar la instalación con ID {}", id);
        facilityService.deleteFacility(id);
        logger.info("Operación completada: Instalación eliminada con ID {}", id);
    }

    @GetMapping("/filter")
    public List<Facility> filterFacilities(
            @RequestParam(required = false, defaultValue = "") String nombre,
            @RequestParam(required = false, defaultValue = "") String tipo,
            @RequestParam(required = false, defaultValue = "0") Integer capacidad) {
        logger.info("Iniciando operación para filtrar instalaciones con parámetros: nombre={}, tipo={}, capacidad>={}", nombre, tipo, capacidad);
        List<Facility> filteredFacilities = facilityService.filterFacilities(nombre, tipo, capacidad);
        logger.info("Operación completada: Se obtuvieron {} instalaciones filtradas", filteredFacilities.size());
        return filteredFacilities;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Facility> updatePartialFacility(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        logger.info("Iniciando operación para actualización parcial de la instalación con ID {}", id);
        Facility updatedFacility = facilityService.updatePartial(id, updates);
        logger.info("Operación completada: Instalación actualizada parcialmente con ID {}", id);
        return ResponseEntity.ok(updatedFacility);
    }

    @GetMapping("/filterByFechaApertura")
    public List<Facility> getFacilitiesByFechaApertura(
           @RequestParam("fecha") LocalDate fecha) {
        logger.info("Iniciando operación para filtrar instalaciones por fecha de apertura: fechaApertura={}", fecha);
        List<Facility> filteredFacilities = facilityService.getFacilitiesByFechaApertura(fecha);
        logger.info("Operación completada: Se obtuvieron {} instalaciones filtradas", filteredFacilities.size());
        return facilityService.getFacilitiesByFechaApertura(fecha);
    }
}
