package com.microservice.renault.service.impl;

import com.microservice.renault.dto.AccessoryDto;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.AccessoryRepository;
import com.microservice.renault.repository.VehicleRepository;
import com.microservice.renault.service.AccessoryService;
import com.microservice.renault.mapper.AccessoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccessoryServiceImpl implements AccessoryService {

    private final VehicleRepository vehicleRepository;
    private final AccessoryRepository accessoryRepository;
    private final AccessoryMapper accessoryMapper;

    @Transactional
    @Override
    public AccessoryDto createAccessoryForVehicle(AccessoryDto accessoryDto, String brand) {
        var vehicle = vehicleRepository.findByBrand(brand).orElseThrow(()->
                new ResourceNotFoundException("vehicle", "brand", brand));
        var accessoryToAdd = accessoryMapper.toEntity(accessoryDto);
        accessoryToAdd.setVehicle(vehicle);

        var accessoryEntity = accessoryRepository.save(accessoryToAdd);

        return accessoryMapper.toDto(accessoryEntity);
    }
}
