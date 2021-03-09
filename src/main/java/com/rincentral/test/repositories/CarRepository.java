package com.rincentral.test.repositories;

import com.rincentral.test.models.Car;
import com.rincentral.test.models.enums.EngineType;
import com.rincentral.test.models.enums.FuelType;
import com.rincentral.test.models.enums.GearboxType;
import com.rincentral.test.models.enums.WheelDriveType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface CarRepository extends JpaRepository<Car, Integer>, JpaSpecificationExecutor<Car> {

    @EntityGraph(
            attributePaths = {"modification.generation.model.segment", "modification.generation.model.brand.country"},
            type = EntityGraph.EntityGraphType.LOAD)
    @Override
    List<Car> findAll(Specification<Car> spec);

    @Query("SELECT DISTINCT c.modification.fuelType FROM Car c")
    Set<FuelType> findFuelTypes();

    @Query("SELECT DISTINCT c.modification.engineType FROM Car c")
    Set<EngineType> findEngineTypes();

    @Query("SELECT DISTINCT c.modification.bodyStyle FROM Car c")
    List<String> findBodyStyles();

    @Query("SELECT DISTINCT c.modification.wheelDriveType FROM Car c")
    Set<WheelDriveType> findWheelDriverTypes();

    @Query("SELECT DISTINCT c.modification.gearboxType FROM Car c")
    Set<GearboxType> findGearboxTypes();
}
