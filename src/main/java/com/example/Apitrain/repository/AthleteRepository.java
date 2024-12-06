package com.example.Apitrain.repository;

import com.example.Apitrain.domain.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {

    List<Athlete> findByNombreContainingAndCategoriaContainingAndEdadGreaterThanEqual(String nombre, String categoria, Integer edad);
}
