package com.microservice.renault.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.entity.GarageEntity;
import com.microservice.renault.exception.ResourceNotFoundException;
import com.microservice.renault.repository.DayOfWeekRepository;
import com.microservice.renault.repository.GarageRepository;
import com.microservice.renault.service.impl.GarageServiceImpl;
import com.microservice.renault.utils.mapper.GarageMapper;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        garageService = new GarageServiceImpl(garageRepository, dayOfWeekRepository, garageMapper);
    }

    @Test
    void testGetGarage() {
        Long garageId = 1L;
        GarageEntity garageEntity = new GarageEntity();
        garageEntity.setGarageId(garageId);

        GarageDto garageDto = new GarageDto();
        garageDto.setIdGarage(garageId);

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garageEntity));
        when(garageMapper.toDto(garageEntity)).thenReturn(garageDto);

        GarageDto result = garageService.getGarage(garageId);

        assertNotNull(result);
        assertEquals(garageId, result.getIdGarage());

        verify(garageRepository, times(1)).findById(garageId);
        verify(garageMapper, times(1)).toDto(garageEntity);
    }

    @Test
    void testGetGarage_NotFound() {
        Long garageId = 1L;
        when(garageRepository.findById(garageId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> garageService.getGarage(garageId));
        verify(garageRepository, times(1)).findById(garageId);
    }

    @Test
    void testCreateGarage() {
        GarageDto garageDto = new GarageDto();
        garageDto.setName("Garage A");

        GarageEntity garageEntity = new GarageEntity();
        garageEntity.setName("Garage A");

        GarageEntity savedGarageEntity = new GarageEntity();
        savedGarageEntity.setName("Garage A");

        when(garageMapper.toEntity(garageDto)).thenReturn(garageEntity);
        when(garageMapper.toDto(savedGarageEntity)).thenReturn(garageDto);
        when(garageRepository.save(garageEntity)).thenReturn(savedGarageEntity);

        GarageDto result = garageService.createGarage(garageDto);

        assertNotNull(result);
        assertEquals("Garage A", result.getName());

        verify(garageRepository, times(1)).save(garageEntity);
        verify(garageMapper, times(1)).toDto(savedGarageEntity);
    }

    @Test
    void testUpdateGarage() {
        Long garageId = 1L;
        GarageDto garageDto = new GarageDto();
        garageDto.setName("Updated Garage");

        GarageEntity existingGarageEntity = new GarageEntity();
        existingGarageEntity.setGarageId(garageId);
        existingGarageEntity.setName("Old Garage");

        GarageEntity updatedGarageEntity = new GarageEntity();
        updatedGarageEntity.setGarageId(garageId);
        updatedGarageEntity.setName("Updated Garage");

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(existingGarageEntity));
        when(garageMapper.toEntity(garageDto)).thenReturn(updatedGarageEntity);
        when(garageMapper.toDto(updatedGarageEntity)).thenReturn(garageDto);
        when(garageRepository.save(updatedGarageEntity)).thenReturn(updatedGarageEntity);

        GarageDto result = garageService.updateGarage(garageDto, garageId);

        assertNotNull(result);
        assertEquals("Updated Garage", result.getName());

        verify(garageRepository, times(1)).findById(garageId);
        verify(garageRepository, times(1)).save(updatedGarageEntity);
    }

    @Test
    void testDeleteGarage() {
        Long garageId = 1L;

        doNothing().when(garageRepository).deleteById(garageId);

        garageService.deleteGarage(garageId);

        verify(garageRepository, times(1)).deleteById(garageId);
    }

    @Test
    void testGetListGarages() {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortDirection = "asc";

        GarageDto garageDto = new GarageDto();
        garageDto.setName("Garage A");

        List<GarageEntity> garageEntities = new ArrayList<>();
        GarageEntity garageEntity = new GarageEntity();
        garageEntity.setName("Garage A");
        garageEntities.add(garageEntity);

        Page<GarageEntity> garageEntityPage = new PageImpl<>(garageEntities);

        when(garageRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.by(sortBy))))).thenReturn(garageEntityPage);
        when(garageMapper.toDto(garageEntity)).thenReturn(garageDto);

        List<GarageDto> result = garageService.getListGarages(page, size, sortBy, sortDirection);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Garage A", result.getFirst().getName());

        verify(garageRepository, times(1)).findAll(PageRequest.of(page, size, Sort.by(Sort.Order.by(sortBy))));
        verify(garageMapper, times(1)).toDto(garageEntity);
    }
}
