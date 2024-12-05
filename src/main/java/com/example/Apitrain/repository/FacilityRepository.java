package com.example.Apitrain.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Apitrain.domain.Facility;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
