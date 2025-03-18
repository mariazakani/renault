package com.microservice.renault.service;

import com.microservice.renault.dto.GarageDto;

import java.util.List;

public interface GarageService {

    GarageDto getGarage(Long idGarage);

    GarageDto createGarage(GarageDto garageDto);

    GarageDto updateGarage(GarageDto garageDto, Long idGarage);

    void deleteGarage(Long idGarage);

    List<GarageDto> getListGarages(int page, int size, String sortBy, String sortDirection);

    List<GarageDto> searchGaragesByVehicleBrand(String brand);

    List<GarageDto> searchGaragesByAccessoryName(String accessoryName);
}
