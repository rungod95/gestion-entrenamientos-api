package com.example.Apitrain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Apitrain.domain.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    }
