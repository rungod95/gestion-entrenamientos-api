package com.example.Apitrain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Apitrain.domain.Event;

import java.util.List;

@Repository

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByNombreContainingAndUbicacionContainingAndCapacidadGreaterThanEqual(String nombre, String ubicación, Integer capacidad);
}
