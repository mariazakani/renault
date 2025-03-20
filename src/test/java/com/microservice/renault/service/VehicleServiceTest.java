package com.microservice.renault.service;

import com.microservice.renault.dto.VehicleDto;
import com.microservice.renault.entity.GarageEntity;
import com.microservice.renault.entity.VehicleEntity;
import com.microservice.renault.exception.GarageCapacityExceededException;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.mapper.VehicleMapper;
import com.microservice.renault.repository.GarageRepository;
import com.microservice.renault.repository.VehicleRepository;
import com.microservice.renault.service.impl.VehicleServiceImpl;
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


    @InjectMocks
    private VehicleServiceImpl vehicleServiceImpl;

    private VehicleDto vehicleDto;
    private VehicleEntity vehicleEntity;
    private GarageEntity garageEntity;
    private VehicleDto vehicleDtoToReturn;

    private static final String BRAND_RENAULT = "Renault";
    private static final String TYPE_CARBURANT = "Diesel";
    private static final String FABRICATION_DATE = "2025-03-16";
    private static final String GARAGE_A = "GarageA";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleDto = VehicleDto.builder()
                .brand(BRAND_RENAULT)
                .fabricationDate(FABRICATION_DATE)
                .typeCarburant(TYPE_CARBURANT)
                .build();

        vehicleEntity = VehicleEntity.builder()
                .brand(BRAND_RENAULT)
                .fabricationDate(FABRICATION_DATE)
                .typeCarburant(TYPE_CARBURANT)
                .build();

        garageEntity = GarageEntity.builder()
                .name(GARAGE_A)
                .vehicles(new ArrayList<>())
                .build();

        vehicleDtoToReturn = VehicleDto.builder()
                .brand(BRAND_RENAULT)
                .fabricationDate(FABRICATION_DATE)
                .typeCarburant(TYPE_CARBURANT)
                .build();
    }

    @Test
    void create_vehicle_should_save_vehicle_when_vehicle_is_valid() {
        when(vehicleMapper.vehicleDtoToEntity(vehicleDto)).thenReturn(vehicleEntity);
        when(vehicleRepository.save(vehicleEntity)).thenReturn(vehicleEntity);
        vehicleServiceImpl.createVehicle(vehicleDto);
        verify(vehicleMapper).vehicleDtoToEntity(vehicleDto);
        verify(vehicleRepository).save(vehicleEntity);
    }

    @Test
    void add_vehicle_to_garage_should_throw_exception_when_garage_is_full() {
        var brand = BRAND_RENAULT;
        var garageName = GARAGE_A;

        for (int i = 0; i < 50; i++) {
            garageEntity.getVehicles().add(new VehicleEntity());
        }
        when(garageRepository.findByName(garageName)).thenReturn(Optional.of(garageEntity));
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.of(vehicleEntity));

        GarageCapacityExceededException exception = assertThrows(GarageCapacityExceededException.class, () -> vehicleServiceImpl.addVehicleToGarage(brand, garageName));

        assertEquals("Cannot add more vehicles to the garage. Max limit 50", exception.getMessage());
        assertTrue(exception.getMessage().contains("Max limit 50"));
    }

    @Test
    void add_vehicle_to_garage_should_throw_exception_when_garage_not_found() {
        var garageName = GARAGE_A;
        when(garageRepository.findByName(garageName)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> vehicleServiceImpl.addVehicleToGarage(BRAND_RENAULT, garageName));

        assertEquals("garage", exception.getResourceName());
        assertEquals("name", exception.getFieldName());
        assertEquals(garageName, exception.getFieldValue());
    }

    @Test
    void add_vehicle_to_garage_should_throw_exception_when_vehicle_not_found() {
        var brand = BRAND_RENAULT;
        var garageName = "Garage 1";
        when(garageRepository.findByName(garageName)).thenReturn(Optional.of(garageEntity));
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> vehicleServiceImpl.addVehicleToGarage(brand, garageName));

        assertEquals("vehicle", exception.getResourceName());
        assertEquals("brand", exception.getFieldName());
        assertEquals(brand, exception.getFieldValue());
    }

    @Test
    void should_return_vehicle_when_garage_exists() {
        var garageName = GARAGE_A;
        List<VehicleEntity> vehicles = new ArrayList<>();
        vehicles.add(vehicleEntity);
        garageEntity.setVehicles(vehicles);
        when(garageRepository.findByName(garageName)).thenReturn(Optional.of(garageEntity));
        when(vehicleMapper.toListDto(vehicles)).thenReturn(List.of(vehicleDtoToReturn));

        List<VehicleDto> result = vehicleServiceImpl.getVehicleForGarage(garageName);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicleDtoToReturn.brand(), result.getFirst().brand());
    }

    @Test
    void get_vehicle_for_garage_should_throw_exception_when_garage_not_found() {
        var garageName = GARAGE_A;
        when(garageRepository.findByName(garageName)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> vehicleServiceImpl.getVehicleForGarage(garageName));

        assertEquals("garage", exception.getResourceName());
        assertEquals("name", exception.getFieldName());
        assertEquals(garageName, exception.getFieldValue());
    }
}
