package com.example.Apitrain.repository;

import com.example.Apitrain.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findByTipoContainingAndNivelContainingAndDuracionGreaterThanEqual(
            String tipo, String nivel, Integer duracion);
}
