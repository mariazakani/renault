package com.microservice.renault.service;

import com.microservice.renault.dto.AccessoryDto;

public interface AccessoryService {

    AccessoryDto createAccessoryForVehicle(AccessoryDto accessoryDto, String brand);
}
