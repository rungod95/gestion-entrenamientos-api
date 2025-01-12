package com.example.Apitrain.service;

import com.example.Apitrain.domain.Facility;
import com.example.Apitrain.exception.FacilityNotFoundException;
import com.example.Apitrain.repository.FacilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class FacilityService {
    private static final Logger logger = LoggerFactory.getLogger(FacilityService.class);

    @Autowired
    private FacilityRepository facilityRepository;

    public List<Facility> getAllFacilities() {
        logger.info("Obteniendo todas las instalaciones de la base de datos");
        List<Facility> facilities = facilityRepository.findAll();
        logger.info("Se obtuvieron {} instalaciones", facilities.size());
        return facilities;
    }

    public Facility getFacilityById(Long id) {
        logger.info("Buscando instalación con ID {}", id);
        return facilityRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Instalación no encontrada con ID {}", id);
                    return new FacilityNotFoundException("Facility not found with id " + id);
                });
    }

    public Facility createFacility(Facility facility) {
        logger.info("Guardando una nueva instalación en la base de datos");
        Facility createdFacility = facilityRepository.save(facility);
        logger.info("Instalación creada con éxito: ID {}", createdFacility.getId());
        return createdFacility;
    }

    public Facility updateFacility(Long id, Facility updatedFacility) {
        logger.info("Actualizando la instalación con ID {}", id);
        Facility facility = getFacilityById(id);
        facility.setNombre(updatedFacility.getNombre());
        facility.setTipo(updatedFacility.getTipo());
        facility.setCapacidad(updatedFacility.getCapacidad());
        facility.setHorario(updatedFacility.getHorario());
        facility.setFechaApertura(updatedFacility.getFechaApertura());
        Facility savedFacility = facilityRepository.save(facility);
        logger.info("Instalación actualizada con éxito: ID {}", id);
        return savedFacility;
    }

    public void deleteFacility(Long id) {
        logger.info("Eliminando la instalación con ID {}", id);
        getFacilityById(id);
        facilityRepository.deleteById(id);
        logger.info("Instalación eliminada con éxito: ID {}", id);
    }

    public List<Facility> filterFacilities(String nombre, String tipo, Integer capacidad) {
        logger.info("Filtrando instalaciones con parámetros: nombre={}, tipo={}, capacidad>={}", nombre, tipo, capacidad);
        List<Facility> facilities = facilityRepository.findByNombreContainingAndTipoContainingAndCapacidadGreaterThanEqual(nombre, tipo, capacidad);
        logger.info("Operación completada: Se obtuvieron {} instalaciones filtradas", facilities.size());
        return facilities;
    }

    public Facility updatePartial(Long id, Map<String, Object> updates) {
        logger.info("Actualizando parcialmente la instalación con ID {}", id);
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Instalación no encontrada con ID {}", id);
                    return new FacilityNotFoundException("Facility not found with id: " + id);
                });

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Facility.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, facility, value);
                logger.debug("Campo actualizado: {} con valor {}", key, value);
            }
        });

        Facility savedFacility = facilityRepository.save(facility);
        logger.info("Instalación actualizada parcialmente con éxito: ID {}", id);
        return savedFacility;
    }
}
