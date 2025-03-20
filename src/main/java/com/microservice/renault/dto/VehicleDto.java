package com.microservice.renault.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record VehicleDto(String brand, String fabricationDate, String typeCarburant, List<AccessoryDto> accessoryList) {

}
