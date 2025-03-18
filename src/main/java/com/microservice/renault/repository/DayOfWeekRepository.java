package com.microservice.renault.repository;

import com.microservice.renault.entity.DayOfWeekEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayOfWeekRepository extends JpaRepository<DayOfWeekEntity, Long> {
    DayOfWeekEntity findByDayName(String dayName);
}
