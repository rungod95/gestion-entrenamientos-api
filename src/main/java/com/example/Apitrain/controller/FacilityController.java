package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Apitrain.service.FacilityService;
import com.example.Apitrain.domain.Facility;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/facilities")
public class FacilityController{

        @Autowired
        private FacilityService facilityService;

        @GetMapping
        public List<Facility> getAllFacilities() {
            return facilityService.getAllFacilities();
        }

        @GetMapping("/{id}")
        public Facility getFacilityById(@PathVariable Long id) {
            return facilityService.getFacilityById(id);
        }

        @PostMapping
        public Facility createFacility(@RequestBody Facility facility) {
            return facilityService.createFacility(facility);
        }

        @PutMapping("/{id}")
        public Facility updateFacility(@PathVariable Long id, @RequestBody Facility facility) {
            return facilityService.updateFacility(id, facility);
        }

        @DeleteMapping("/{id}")
        public void deleteFacility(@PathVariable Long id) {
            facilityService.deleteFacility(id);
        }

        @GetMapping("/filter")
        public List<Facility> filterFacilities(
                @RequestParam(required = false, defaultValue = "") String nombre,
                @RequestParam(required = false, defaultValue = "") String tipo,
                @RequestParam(required = false, defaultValue = "0") Integer capacidad) {
            return facilityService.filterFacilities(nombre, tipo, capacidad);
        }
    @PatchMapping("/{id}")
    public ResponseEntity<Facility> updatePartialFacility(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Facility updatedFacility = facilityService.updatePartial(id, updates);
        return ResponseEntity.ok(updatedFacility);
    }
}
