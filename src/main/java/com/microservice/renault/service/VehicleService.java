package com.microservice.renault.service;

import com.microservice.renault.dto.VehicleDto;

import java.util.List;

public interface VehicleService {

    void createVehicle(VehicleDto vehicleDto);

    VehicleDto addVehicleToGarage(String brand, String garageName) throws IllegalAccessException;

    List<VehicleDto> getVehicleForGarage(String garageName);

}
