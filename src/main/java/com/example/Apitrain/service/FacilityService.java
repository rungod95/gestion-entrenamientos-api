package com.example.Apitrain.service;

import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.exception.TrainerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.repository.FacilityRepository;
import com.example.Apitrain.domain.Facility;
import com.example.Apitrain.exception.FacilityNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    public Facility updatePartial(Long id, Map<String, Object> updates) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new FacilityNotFoundException("Trainer not found with id: " + id));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Facility.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, facility, value);
            }
        });

        return facilityRepository.save(facility);
    }
}
