package com.microservice.renault.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DayOfWeek")
public class DayOfWeekEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id")
    private Integer dayId;

    @Column(name = "day_name", nullable = false)
    private String dayName;

}
