package com.example.Apitrain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Apitrain.domain.Event;

import java.util.List;

@Repository

public interface EventRepository extends JpaRepository<Event, Long> {


    List<Event> findByNombreContainingAndUbicacionContainingAndCapacidadGreaterThanEqual(String nombre, String ubicación, Integer capacidad);


@Query("SELECT e FROM Event e WHERE e.ubicacion = :ubicacion AND e.capacidad > :capacidad")
List<Event> findEventsByUbicacionAndCapacidad(@Param("ubicacion") String ubicacion, @Param("capacidad") Integer capacidad);

@Query(value = "SELECT * FROM events WHERE capacidad > :capacidad AND ubicacion = :ubicacion", nativeQuery = true)
List<Event> findEventsByCapacidadAndUbicacion(@Param("capacidad") Integer capacidad, @Param("ubicacion") String ubicacion);

    }

