package com.microservice.renault.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "OpeningTime")
public class OpeningTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opening_time_id")
    private Long openingTimeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    private GarageEntity garage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id", nullable = false)
    private DayOfWeekEntity dayOfWeek;

    @Column(name = "start_date", nullable = false)
    private LocalTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalTime endDate;
}
