package com.microservice.renault.service;

import com.microservice.renault.dto.AccessoryDto;
import com.microservice.renault.entity.AccessoryEntity;
import com.microservice.renault.entity.VehicleEntity;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.AccessoryRepository;
import com.microservice.renault.repository.VehicleRepository;
import com.microservice.renault.service.impl.AccessoryServiceImpl;
import com.microservice.renault.utils.mapper.AccessoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccessoryServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    @InjectMocks
    private AccessoryServiceImpl accessoryServiceImpl;

    private VehicleEntity vehicle;
    private AccessoryEntity accessoryEntity;
    private AccessoryDto accessoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = new VehicleEntity();
        vehicle.setBrand("Renault");

        accessoryEntity = new AccessoryEntity();
        accessoryEntity.setName("accessory1");
        accessoryEntity.setDescription("descrption accessory");
        accessoryEntity.setPrice("100");
        accessoryEntity.setType("type1");

        accessoryDto = new AccessoryDto();
        accessoryDto.setName("accessory1");
        accessoryDto.setDescription("descrption accessory");
        accessoryDto.setPrice("100");
        accessoryDto.setType("type1");
    }

    @Test
    void createAccessoryForVehicle_returnAccessoryDto_WhenVehicleExists() {

        String brand = "Renault";
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(accessoryDto)).thenReturn(accessoryEntity);
        when(accessoryRepository.save(accessoryEntity)).thenReturn(accessoryEntity);
        when(accessoryMapper.toDto(accessoryEntity)).thenReturn(accessoryDto);

        AccessoryDto result = accessoryServiceImpl.createAccessoryForVehicle(accessoryDto, brand);

        assertNotNull(result);
        assertEquals(accessoryDto.getName(), result.getName());
        assertEquals(accessoryDto.getDescription(), result.getDescription());
        assertEquals(accessoryDto.getPrice(), result.getPrice());
        assertEquals(accessoryDto.getType(), result.getType());

        verify(vehicleRepository).findByBrand(brand);
        verify(accessoryRepository).save(accessoryEntity);
        verify(accessoryMapper).toDto(accessoryEntity);
    }

    @Test
    void createAccessoryForVehicle_throwException_WhenVehicleNotFound() {
        String brand = "Renault";
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            accessoryServiceImpl.createAccessoryForVehicle(accessoryDto, brand);
        });

        assertEquals("vehicle", exception.getResourceName());
        assertEquals("brand", exception.getFieldName());
        assertEquals(brand, exception.getFieldValue());
    }
}
