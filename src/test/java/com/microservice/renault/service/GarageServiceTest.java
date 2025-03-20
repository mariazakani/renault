package com.microservice.renault.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.entity.GarageEntity;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.DayOfWeekRepository;
import com.microservice.renault.repository.GarageRepository;
import com.microservice.renault.service.impl.GarageServiceImpl;
import com.microservice.renault.mapper.GarageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class GarageServiceTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private DayOfWeekRepository dayOfWeekRepository;

    @Mock
    private GarageMapper garageMapper;

    private GarageServiceImpl garageService;

    private static final String GARAGE_NAME = "Garage A";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        garageService = new GarageServiceImpl(garageRepository, dayOfWeekRepository, garageMapper);
    }

    @Test
    void should_return_garage_when_id_provided() {
        var garageId = 1L;
        var garageEntity = GarageEntity.builder()
                .garageId(garageId)
                .build();

        var garageDto = GarageDto.builder()
                .idGarage(garageId).build();

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garageEntity));
        when(garageMapper.toDto(garageEntity)).thenReturn(garageDto);

        var result = garageService.getGarage(garageId);

        assertNotNull(result);
        assertEquals(garageId, result.idGarage());

        verify(garageRepository, times(1)).findById(garageId);
        verify(garageMapper, times(1)).toDto(garageEntity);
    }

    @Test
    void should_throw_exception_when_garage_not_found() {
        var garageId = 1L;
        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> garageService.getGarage(garageId));
        verify(garageRepository, times(1)).findById(garageId);
    }

    @Test
    void should_create_garage_when_request_body_provided() {
        var garageDto = GarageDto.builder().name(GARAGE_NAME).build();

        var garageEntity = GarageEntity.builder().name(GARAGE_NAME).build();

        var savedGarageEntity = GarageEntity.builder().name(GARAGE_NAME).build();

        when(garageMapper.toEntity(garageDto)).thenReturn(garageEntity);
        when(garageMapper.toDto(savedGarageEntity)).thenReturn(garageDto);
        when(garageRepository.save(garageEntity)).thenReturn(savedGarageEntity);

        var result = garageService.createGarage(garageDto);

        assertNotNull(result);
        assertEquals(GARAGE_NAME, result.name());

        verify(garageRepository, times(1)).save(garageEntity);
        verify(garageMapper, times(1)).toDto(savedGarageEntity);
    }

    @Test
    void should_update_garage_when_request_body_provided() {
        var garageId = 1L;
        var garageDto = GarageDto.builder().name("Updated Garage").build();

        var existingGarageEntity = GarageEntity.builder()
                .garageId(garageId)
                .name("Old Garage").build();

        var updatedGarageEntity = GarageEntity.builder()
                .garageId(garageId)
                .name("Updated Garage").build();

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(existingGarageEntity));
        when(garageMapper.toEntity(garageDto)).thenReturn(updatedGarageEntity);
        when(garageMapper.toDto(updatedGarageEntity)).thenReturn(garageDto);
        when(garageRepository.save(updatedGarageEntity)).thenReturn(updatedGarageEntity);

        GarageDto result = garageService.updateGarage(garageDto, garageId);

        assertNotNull(result);
        assertEquals("Updated Garage", result.name());

        verify(garageRepository, times(1)).findById(garageId);
        verify(garageRepository, times(1)).save(updatedGarageEntity);
    }

    @Test
    void should_delete_garage_when_id_provided() {
        var garageId = 1L;

        doNothing().when(garageRepository).deleteById(garageId);

        garageService.deleteGarage(garageId);

        verify(garageRepository, times(1)).deleteById(garageId);
    }

    @Test
    void should_return_sorted_list_garages_when_sort_informations_provided() {
        var page = 0;
        var size = 10;
        var sortBy = "name";
        var sortDirection = "asc";

        var garageDto = GarageDto.builder().name(GARAGE_NAME).build();

        List<GarageEntity> garageEntities = new ArrayList<>();
        GarageEntity garageEntity = GarageEntity.builder().name(GARAGE_NAME).build();
        garageEntities.add(garageEntity);

        Page<GarageEntity> garageEntityPage = new PageImpl<>(garageEntities);

        when(garageRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.by(sortBy))))).thenReturn(garageEntityPage);
        when(garageMapper.toDto(garageEntity)).thenReturn(garageDto);

        var result = garageService.getListGarages(page, size, sortBy, sortDirection);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(GARAGE_NAME, result.getFirst().name());

        verify(garageRepository, times(1)).findAll(PageRequest.of(page, size, Sort.by(Sort.Order.by(sortBy))));
        verify(garageMapper, times(1)).toDto(garageEntity);
    }
}
