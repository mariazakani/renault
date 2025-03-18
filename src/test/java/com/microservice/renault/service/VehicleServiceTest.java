package com.microservice.renault.service;

import com.microservice.renault.dto.VehicleDto;
import com.microservice.renault.entity.GarageEntity;
import com.microservice.renault.entity.VehicleEntity;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.GarageRepository;
import com.microservice.renault.repository.VehicleRepository;
import com.microservice.renault.service.impl.VehicleServiceImpl;
import com.microservice.renault.utils.mapper.GarageMapper;
import com.microservice.renault.utils.mapper.VehicleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VehicleServiceTest {


    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleServiceImpl;

    private VehicleDto vehicleDto;
    private VehicleEntity vehicleEntity;
    private GarageEntity garageEntity;
    private VehicleDto vehicleDtoToReturn;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleDto = new VehicleDto();
        vehicleDto.setBrand("Renault");
        vehicleDto.setFabricationDate("2025-03-16");
        vehicleDto.setTypeCarburant("Diesel");

        vehicleEntity = new VehicleEntity();
        vehicleEntity.setBrand("Renault");
        vehicleEntity.setFabricationDate("2025-03-16");
        vehicleEntity.setTypeCarburant("Diesel");

        garageEntity = new GarageEntity();
        garageEntity.setName("GarageA");
        garageEntity.setVehicles(new ArrayList<>());

        vehicleDtoToReturn = new VehicleDto();
        vehicleDtoToReturn.setBrand("Renault");
        vehicleDtoToReturn.setFabricationDate("2025-03-16");
        vehicleDtoToReturn.setTypeCarburant("Diesel");
    }

    @Test
    void createVehicle_ShouldSaveVehicle_WhenVehicleIsValid() {
        when(vehicleMapper.vehicleDtoToEntity(vehicleDto)).thenReturn(vehicleEntity);
        when(vehicleRepository.save(vehicleEntity)).thenReturn(vehicleEntity);
        vehicleServiceImpl.createVehicle(vehicleDto);
        verify(vehicleMapper).vehicleDtoToEntity(vehicleDto);
        verify(vehicleRepository).save(vehicleEntity);
    }

    @Test
    void addVehicleToGarage_ShouldThrowException_WhenGarageIsFull() {
        String brand = "Renault";
        String garageName = "GarageA";

        for (int i = 0; i < 50; i++) {
            garageEntity.getVehicles().add(new VehicleEntity());
        }
        when(garageRepository.findByName(garageName)).thenReturn(Optional.of(garageEntity));
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.of(vehicleEntity));

        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            vehicleServiceImpl.addVehicleToGarage(brand, garageName);
        });

        assertEquals("Cannot add more vehicles to the garage. Max limit 50", exception.getMessage());
    }

    @Test
    void addVehicleToGarage_ShouldThrowException_WhenGarageNotFound() {
        String brand = "Renault";
        String garageName = "GarageA";
        when(garageRepository.findByName(garageName)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            vehicleServiceImpl.addVehicleToGarage(brand, garageName);
        });

        assertEquals("garage", exception.getResourceName());
        assertEquals("name", exception.getFieldName());
        assertEquals(garageName, exception.getFieldValue());
    }

    @Test
    void addVehicleToGarage_ShouldThrowException_WhenVehicleNotFound() {
        String brand = "Renault";
        String garageName = "Garage 1";
        when(garageRepository.findByName(garageName)).thenReturn(Optional.of(garageEntity));
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            vehicleServiceImpl.addVehicleToGarage(brand, garageName);
        });

        assertEquals("vehicle", exception.getResourceName());
        assertEquals("brand", exception.getFieldName());
        assertEquals(brand, exception.getFieldValue());
    }

    @Test
    void getVehicleForGarage_ShouldReturnVehicleDtos_WhenGarageExists() {
        String garageName = "GarageA";
        List<VehicleEntity> vehicles = new ArrayList<>();
        vehicles.add(vehicleEntity);
        garageEntity.setVehicles(vehicles);
        when(garageRepository.findByName(garageName)).thenReturn(Optional.of(garageEntity));
        when(vehicleMapper.toListDto(vehicles)).thenReturn(List.of(vehicleDtoToReturn));

        List<VehicleDto> result = vehicleServiceImpl.getVehicleForGarage(garageName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleDtoToReturn.getBrand(), result.get(0).getBrand());
    }

    @Test
    void getVehicleForGarage_ShouldThrowException_WhenGarageNotFound() {
        String garageName = "GarageA";
        when(garageRepository.findByName(garageName)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            vehicleServiceImpl.getVehicleForGarage(garageName);
        });

        assertEquals("garage", exception.getResourceName());
        assertEquals("name", exception.getFieldName());
        assertEquals(garageName, exception.getFieldValue());
    }
}
