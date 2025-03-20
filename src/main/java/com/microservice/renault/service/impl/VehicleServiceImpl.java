package com.microservice.renault.service.impl;

import com.microservice.renault.dto.VehicleDto;
import com.microservice.renault.entity.GarageEntity;
import com.microservice.renault.entity.VehicleEntity;
import com.microservice.renault.exception.GarageCapacityExceededException;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.GarageRepository;
import com.microservice.renault.repository.VehicleRepository;
import com.microservice.renault.service.VehicleService;
import com.microservice.renault.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {

    private static final int MAX_VEHICLES = 50;
    private final GarageRepository garageRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    @Override
    public void createVehicle(VehicleDto vehicleDto) {
        var vehicleEntity = vehicleMapper.vehicleDtoToEntity(vehicleDto);
        if(!CollectionUtils.isEmpty(vehicleEntity.getAccessoryEntities())){
            vehicleEntity.getAccessoryEntities().forEach(accessoryEntity -> accessoryEntity.setVehicle(vehicleEntity));
        }
        vehicleRepository.save(vehicleEntity);
    }

    @Transactional
    @Override
    public VehicleDto addVehicleToGarage(String brand, String garageName) {
        GarageEntity garage = garageRepository.findByName(garageName).orElseThrow(() ->new ResourceNotFoundException("garage", "name", garageName));
        VehicleEntity vehicle = vehicleRepository.findByBrand(brand).orElseThrow(() ->new ResourceNotFoundException("vehicle", "brand", brand));
        if(!garage.getVehicles().isEmpty() && garage.getVehicles().size() >=MAX_VEHICLES ){
            throw new GarageCapacityExceededException("Cannot add more vehicles to the garage. Max limit 50");
        }
        vehicle.setGarage(garage);
        var vehicleAdded = vehicleRepository.save(vehicle);
        return vehicleMapper.toDto(vehicleAdded);
    }

    @Transactional
    @Override
    public List<VehicleDto> getVehicleForGarage(String garageName) {
        var returnedGarage = garageRepository.findByName(garageName).orElseThrow(() -> new ResourceNotFoundException("garage", "name", garageName));
        return vehicleMapper.toListDto(returnedGarage.getVehicles());

    }
}
