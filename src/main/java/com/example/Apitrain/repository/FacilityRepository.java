package com.example.Apitrain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Apitrain.domain.Facility;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findByNombreContainingAndTipoContainingAndCapacidadGreaterThanEqual(String nombre, String tipo, Integer capacidad);
}
