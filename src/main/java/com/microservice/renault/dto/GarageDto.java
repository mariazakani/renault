package com.microservice.renault.dto;

import lombok.Builder;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Builder
public record GarageDto(
        Long idGarage,
        String name,
        String address,
        String telephone,
        String email,
        Map<DayOfWeek, List<OpeningTimeDto>> horairesOuverture,
        List<VehicleDto> vehicleList
) {

}
