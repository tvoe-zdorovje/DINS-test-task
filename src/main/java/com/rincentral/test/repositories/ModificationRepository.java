package com.rincentral.test.repositories;

import com.rincentral.test.models.Modification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface ModificationRepository extends JpaRepository<Modification, Integer> {

    @Query("SELECT m FROM Modification m JOIN FETCH m.generation g JOIN FETCH g.model WHERE m.title=?1 AND g.title=?2 AND g.model.title=?3")
    Optional<Modification> find(String modTitle, String genTitle, String modelTitle);

}
