package com.example.Apitrain.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.repository.FacilityRepository;
import com.example.Apitrain.domain.Facility;
import com.example.Apitrain.exception.FacilityNotFoundException;

import java.util.List;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Facility not found with id " + id));
    }

    public Facility createFacility(Facility facility) {
        return facilityRepository.save(facility);
    }

    public Facility updateFacility(Long id, Facility updatedFacility) {
        Facility facility = getFacilityById(id);
        facility.setNombre(updatedFacility.getNombre());
        facility.setTipo(updatedFacility.getTipo());
        facility.setCapacidad(updatedFacility.getCapacidad());
        facility.setHorario(updatedFacility.getHorario());
        facility.setFechaApertura(updatedFacility.getFechaApertura());
        return facilityRepository.save(facility);
    }

    public void deleteFacility(Long id) {
        Facility facility = getFacilityById(id);
        facilityRepository.deleteById(id);
    }

    public List<Facility> filterFacilities(String nombre, String tipo, Integer capacidad) {
        return facilityRepository.findByNombreContainingAndTipoContainingAndCapacidadGreaterThanEqual(nombre, tipo, capacidad);
    }
}
