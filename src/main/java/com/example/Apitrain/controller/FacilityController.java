package com.example.Apitrain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Apitrain.service.FacilityService;
import com.example.Apitrain.domain.Facility;

import java.util.List;

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
}
