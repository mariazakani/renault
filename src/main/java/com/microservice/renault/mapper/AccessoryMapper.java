package com.microservice.renault.mapper;

import com.microservice.renault.dto.AccessoryDto;
import com.microservice.renault.entity.AccessoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    AccessoryEntity toEntity(AccessoryDto accessoryDto);

    AccessoryDto toDto(AccessoryEntity accessoryEntity);

    List<AccessoryDto> toListDto(List<AccessoryEntity> accessoryEntities);
}
