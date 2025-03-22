package com.example.Apitrain.repository;

import com.example.Apitrain.domain.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {

    @Query("SELECT a FROM Athlete a WHERE a.edad > :edadMin")
    List<Athlete> findByEdadGreaterThan(@Param("edadMin") Integer edadMin);


    List<Athlete> findByNombreContainingAndCategoriaContainingAndEdadGreaterThanEqual(String nombre, String categoria, Integer edad);

    }
