package com.microservice.renault.service.impl;

import com.microservice.renault.dto.AccessoryDto;
import com.microservice.renault.entity.AccessoryEntity;
import com.microservice.renault.entity.VehicleEntity;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.AccessoryRepository;
import com.microservice.renault.repository.VehicleRepository;
import com.microservice.renault.service.AccessoryService;
import com.microservice.renault.utils.mapper.AccessoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccessoryServiceImpl implements AccessoryService {

    private final VehicleRepository vehicleRepository;
    private final AccessoryRepository accessoryRepository;
    private final AccessoryMapper accessoryMapper;

    @Override
    public AccessoryDto createAccessoryForVehicle(AccessoryDto accessoryDto, String brand) {
        VehicleEntity vehicle = vehicleRepository.findByBrand(brand).orElseThrow(()->
                new ResourceNotFoundException("vehicle", "brand", brand));
        AccessoryEntity accessoryToAdd = accessoryMapper.toEntity(accessoryDto);
        accessoryToAdd.setVehicle(vehicle);

        AccessoryEntity accessoryEntity = accessoryRepository.save(accessoryToAdd);

        return accessoryMapper.toDto(accessoryEntity);
    }
}
