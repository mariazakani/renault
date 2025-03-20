package com.microservice.renault.service.impl;

import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.entity.DayOfWeekEntity;
import com.microservice.renault.entity.GarageEntity;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.DayOfWeekRepository;
import com.microservice.renault.repository.GarageRepository;
import com.microservice.renault.service.GarageService;
import com.microservice.renault.mapper.GarageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;

    private final DayOfWeekRepository dayOfWeekRepository;

    private final GarageMapper garageMapper;


    @Transactional
    @Override
    public GarageDto getGarage(Long idGarage) {
         GarageEntity garage = garageRepository.findById(idGarage)
                .orElseThrow(() -> new ResourceNotFoundException("garage","id", String.valueOf(idGarage)));
         return garageMapper.toDto(garage);
    }

    @Transactional
    @Override
    public GarageDto createGarage(GarageDto garageDto) {
        GarageEntity garage = garageMapper.toEntity(garageDto);

        mapHoraireOuverture(garage);

        GarageEntity savedGarage = garageRepository.save(garage);
        return garageMapper.toDto(savedGarage);
    }

    private void mapHoraireOuverture(GarageEntity garage) {
        if (!CollectionUtils.isEmpty(garage.getHorairesOuverture())) {

            Map<String, DayOfWeekEntity> dayOfWeekMap = getDayOfWeekMap();
            garage.getHorairesOuverture().forEach(openingTimeEntity -> {
                DayOfWeekEntity dayOfWeek = dayOfWeekMap.get(openingTimeEntity.getDayOfWeek().getDayName());
                if (dayOfWeek != null) {
                    openingTimeEntity.setDayOfWeek(dayOfWeek);
                }
                openingTimeEntity.setGarage(garage);
            });
        }
    }

    private Map<String, DayOfWeekEntity> getDayOfWeekMap() {
        List<DayOfWeekEntity> dayOfWeekEntities = dayOfWeekRepository.findAll();
        return dayOfWeekEntities.stream()
                .collect(Collectors.toMap(DayOfWeekEntity::getDayName, day -> day));
    }


    @Transactional
    @Override
    public GarageDto updateGarage(GarageDto garageDto, Long idGarage) {
        GarageEntity beforeUpdate = garageRepository.findById(idGarage).orElseThrow(() -> new ResourceNotFoundException("garage","id", String.valueOf(idGarage)));
        GarageEntity garageToUpdate = garageMapper.toEntity(garageDto);
        mapHoraireOuverture(garageToUpdate);
        garageToUpdate.setGarageId(beforeUpdate.getGarageId());
        return garageMapper.toDto(garageRepository.save(garageToUpdate));
    }

    @Transactional
    @Override
    public void deleteGarage(Long idGarage) {
        garageRepository.deleteById(idGarage);
    }

    @Override
    public List<GarageDto> getListGarages(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Order.by(sortBy));
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<GarageEntity> garageEntities = garageRepository.findAll(pageable);
        List<GarageDto> listGarages = new ArrayList<>();
        garageEntities.getContent().forEach(garageEntity -> listGarages.add(garageMapper.toDto(garageEntity)));
        return listGarages;

    }

    @Transactional
    @Override
    public List<GarageDto> searchGaragesByVehicleBrand(String brand) {
        List<GarageEntity> listGarages = garageRepository.findGaragesByVehicleBrand(brand);
        if(listGarages.isEmpty()){
            throw new ResourceNotFoundException("vehicle", "brand", brand);
        }
        return garageMapper.toListDto(listGarages);
    }

    @Transactional
    @Override
    public List<GarageDto> searchGaragesByAccessoryName(String accessoryName) {
        List<GarageEntity> listGarages = garageRepository.findGaragesByAccessoryName(accessoryName);
        if(listGarages.isEmpty()){
            throw new ResourceNotFoundException("accessory", "name", accessoryName);
        }
        return garageMapper.toListDto(listGarages);
    }
}
