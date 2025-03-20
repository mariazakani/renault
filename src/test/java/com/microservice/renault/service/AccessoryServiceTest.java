package com.microservice.renault.service;

import com.microservice.renault.dto.AccessoryDto;
import com.microservice.renault.entity.AccessoryEntity;
import com.microservice.renault.entity.VehicleEntity;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.AccessoryRepository;
import com.microservice.renault.repository.VehicleRepository;
import com.microservice.renault.service.impl.AccessoryServiceImpl;
import com.microservice.renault.mapper.AccessoryMapper;
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

    private static final String BRAND_RENAULT = "Renault";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vehicle = VehicleEntity.builder().brand("BRAND").build();

        accessoryEntity = AccessoryEntity.builder()
                .name("accessory1")
                .description("descrption accessory")
                .price("100")
                .type("type1").build();


        accessoryDto = AccessoryDto.builder()
                .name("accessory1")
                .description("descrption accessory")
                .price("100")
                .type("type1").build();
    }

    @Test
    void create_accessory_for_vehicle_should_return_accessory_when_vehicle_exists() {

        String brand = BRAND_RENAULT;
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(accessoryDto)).thenReturn(accessoryEntity);
        when(accessoryRepository.save(accessoryEntity)).thenReturn(accessoryEntity);
        when(accessoryMapper.toDto(accessoryEntity)).thenReturn(accessoryDto);

        AccessoryDto result = accessoryServiceImpl.createAccessoryForVehicle(accessoryDto, brand);

        assertNotNull(result);
        assertEquals(accessoryDto.name(), result.name());
        assertEquals(accessoryDto.description(), result.description());
        assertEquals(accessoryDto.price(), result.price());
        assertEquals(accessoryDto.type(), result.type());

        verify(vehicleRepository).findByBrand(brand);
        verify(accessoryRepository).save(accessoryEntity);
        verify(accessoryMapper).toDto(accessoryEntity);
    }

    @Test
    void create_accessory_for_vehicle_should_throw_exception_when_vehicle_not_found() {
        String brand = BRAND_RENAULT;
        when(vehicleRepository.findByBrand(brand)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> accessoryServiceImpl.createAccessoryForVehicle(accessoryDto, brand));

        assertEquals("vehicle", exception.getResourceName());
        assertEquals("brand", exception.getFieldName());
        assertEquals(brand, exception.getFieldValue());
    }
}
