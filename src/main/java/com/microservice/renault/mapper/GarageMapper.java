package com.microservice.renault.mapper;

import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.dto.OpeningTimeDto;
import com.microservice.renault.entity.DayOfWeekEntity;
import com.microservice.renault.entity.GarageEntity;
import com.microservice.renault.entity.OpeningTimeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = VehicleMapper.class)
public interface GarageMapper {

    @Mapping(target = "garageId",source = "idGarage", ignore = true)
    @Mapping(target = "vehicles", source = "vehicleList")
    @Mapping(target = "horairesOuverture", qualifiedByName = "mapHorairesOuvertureToEntity")
    GarageEntity toEntity(GarageDto garageDto);

    @Named("mapHorairesOuvertureToEntity")
    default List<OpeningTimeEntity> mapHorairesOuvertureToEntity(Map<DayOfWeek, List<OpeningTimeDto>> horairesOuverture) {

        List<OpeningTimeEntity> openingTimes = new ArrayList<>();
        for (Map.Entry<DayOfWeek, List<OpeningTimeDto>> entry : horairesOuverture.entrySet()) {
            DayOfWeek dayOfWeek = entry.getKey();
            for (OpeningTimeDto openingTimeDto : entry.getValue()) {
                OpeningTimeEntity openingTime = new OpeningTimeEntity();
                openingTime.setStartDate(openingTimeDto.startDate());
                openingTime.setEndDate(openingTimeDto.startDate());
                DayOfWeekEntity dayOfWeekEntity = new DayOfWeekEntity();
                dayOfWeekEntity.setDayName(dayOfWeek.name());
                openingTime.setDayOfWeek(dayOfWeekEntity);
                openingTimes.add(openingTime);
            }
        }
        return openingTimes;
    }

    @Mapping(target = "idGarage", source = "garageId")
    @Mapping(target = "vehicleList", source = "vehicles")
    @Mapping(target = "horairesOuverture", qualifiedByName = "mapHorairesOuvertureToDto")
    GarageDto toDto(GarageEntity garageEntity);

    @Named("mapHorairesOuvertureToDto")
    default Map<DayOfWeek, List<OpeningTimeDto>> mapHorairesOuvertureToDto(List<OpeningTimeEntity> horairesOuvertureEntity) {
        Map<DayOfWeek, List<OpeningTimeDto>> result = new HashMap<>();

        if (horairesOuvertureEntity != null) {
            for (OpeningTimeEntity openingTime : horairesOuvertureEntity) {
                DayOfWeekEntity dayOfWeekEntity = openingTime.getDayOfWeek();
                OpeningTimeDto openingTimeDto = new OpeningTimeDto(openingTime.getStartDate(), openingTime.getEndDate());
                result.computeIfAbsent(DayOfWeek.valueOf(dayOfWeekEntity.getDayName()), _ -> new ArrayList<>()).add(openingTimeDto);
            }
        }

        return result;
    }

    List<GarageDto> toListDto(List<GarageEntity> garageEntities);
}
