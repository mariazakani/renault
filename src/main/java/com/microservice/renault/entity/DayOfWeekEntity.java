package com.microservice.renault.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@Table(name = "DayOfWeek")
@NoArgsConstructor
@AllArgsConstructor
public class DayOfWeekEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id")
    private Integer dayId;

    @Column(name = "day_name", nullable = false)
    private String dayName;

}
