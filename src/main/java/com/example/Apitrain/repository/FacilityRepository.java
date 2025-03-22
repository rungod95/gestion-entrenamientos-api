package com.example.Apitrain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Apitrain.domain.Facility;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    List<Facility> findByNombreContainingAndTipoContainingAndCapacidadGreaterThanEqual(String nombre, String tipo, Integer capacidad);

    @Query(value = "SELECT * FROM facilities WHERE fecha_apertura > :fecha", nativeQuery = true)
    List<Facility> findFacilitiesByFechaAperturaAfter(@Param("fecha") LocalDate fecha);
}
