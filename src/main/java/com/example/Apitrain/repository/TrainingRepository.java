package com.example.Apitrain.repository;

import com.example.Apitrain.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTipoContainingAndNivelContainingAndDuracionGreaterThanEqual(
            String tipo, String nivel, Integer duracion);

    @Query("SELECT t FROM Training t WHERE t.nivel = :nivel AND t.duracion > :duracionMin")
    List<Training> findByNivelAndDuracion(@Param("nivel") String nivel, @Param("duracionMin") Integer duracionMin);
}
