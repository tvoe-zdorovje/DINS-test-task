package com.rincentral.test.repositories;

import com.rincentral.test.models.Modification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface ModificationRepository extends JpaRepository<Modification, Integer> {

    @Query("SELECT m FROM Modification m WHERE m.title=?1 AND m.generation.title=?2 AND m.generation.model.title=?3")
    Optional<Modification> find(String modTitle, String genTitle, String modelTitle);

    @Query("SELECT AVG(m.maxSpeed) FROM Modification m WHERE m.generation.model.title=?1")
    Double getAvgMaxSpeedByModel(String model);

    @Query("SELECT AVG(m.maxSpeed) FROM Modification m WHERE m.generation.model.brand.title=?1")
    Double getAvgMaxSpeedByBrand(String brand);
}
