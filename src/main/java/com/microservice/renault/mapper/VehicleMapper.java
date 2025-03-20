package com.microservice.renault.mapper;

import com.microservice.renault.dto.VehicleDto;
import com.microservice.renault.entity.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AccessoryMapper.class)
public interface VehicleMapper {

    @Mapping(target = "accessoryEntities", source = "accessoryList")
    VehicleEntity vehicleDtoToEntity(VehicleDto vehicleDto);

    @Mapping(target = "accessoryList", source = "accessoryEntities")
    VehicleDto toDto(VehicleEntity vehicleEntity);

    List<VehicleDto> toListDto(List<VehicleEntity> vehicleEntities);


}
