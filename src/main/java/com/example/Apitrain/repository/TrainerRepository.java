package com.example.Apitrain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Apitrain.domain.Trainer;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    List<Trainer> findByNombreContainingAndEspecialidadContainingAndExperienciaGreaterThanEqual(String nombre, String especialidad, Integer experiencia);

    @Query(value = "SELECT * FROM trainers WHERE experiencia > :experiencia", nativeQuery = true)
    List<Trainer> findTrainersByExperienciaGreaterThan(@Param("experiencia") Integer experiencia);

    }
