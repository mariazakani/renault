package com.microservice.renault.repository;

import com.microservice.renault.entity.GarageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GarageRepository extends JpaRepository<GarageEntity, Long> {

    Optional<GarageEntity> findByName(String name);

    @Query("SELECT g FROM GarageEntity g JOIN g.vehicles v WHERE v.brand = :brand")
    List<GarageEntity> findGaragesByVehicleBrand(@Param("brand") String brand);

    @Query("SELECT g FROM GarageEntity g JOIN g.vehicles v JOIN v.accessoryEntities a WHERE a.name = :name")
    List<GarageEntity> findGaragesByAccessoryName(@Param("name") String accessoryName);
}
