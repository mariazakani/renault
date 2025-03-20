package com.microservice.renault.dto;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record OpeningTimeDto(LocalTime startDate, LocalTime endDate) {

}
