package com.microservice.renault.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.service.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class GarageApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GarageService garageService;

    @Autowired
    private ObjectMapper objectMapper;

    private GarageDto garageDto;

    @BeforeEach
    public void setUp() {
        garageDto = new GarageDto();
        garageDto.setName("Garage A");
        garageDto.setAddress("123 Street");
        garageDto.setEmail("contact@garagea.com");
        garageDto.setTelephone("1234567890");
    }

    @Test
    public void testGetGarageInformation() throws Exception {
        Long garageId = 1L;

        when(garageService.getGarage(garageId)).thenReturn(garageDto);

        mockMvc.perform(get("/garages/{id}", garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage A"))
                .andExpect(jsonPath("$.address").value("123 Street"));

        verify(garageService, times(1)).getGarage(garageId);
    }

    @Test
    public void testCreateGarage() throws Exception {
        when(garageService.createGarage(any(GarageDto.class))).thenReturn(garageDto);

        mockMvc.perform(post("/garages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Garage A"))
                .andExpect(jsonPath("$.address").value("123 Street"));

        verify(garageService, times(1)).createGarage(any(GarageDto.class));
    }

    @Test
    public void testUpdateGarage() throws Exception {
        Long garageId = 1L;
        when(garageService.updateGarage(any(GarageDto.class), eq(garageId))).thenReturn(garageDto);

        mockMvc.perform(put("/garage/{id}/update", garageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage A"))
                .andExpect(jsonPath("$.address").value("123 Street"));

        verify(garageService, times(1)).updateGarage(any(GarageDto.class), eq(garageId));
    }

    @Test
    public void testDeleteGarage() throws Exception {
        Long garageId = 1L;
        doNothing().when(garageService).deleteGarage(garageId);
        mockMvc.perform(delete("/garage/{id}/delete", garageId))
                .andExpect(status().isOk())
                .andExpect(content().string("Garage deleted successfully"));

        verify(garageService, times(1)).deleteGarage(garageId);
    }

    @Test
    public void testGetGarages() throws Exception {
        List<GarageDto> garages = Arrays.asList(garageDto);
        when(garageService.getListGarages(anyInt(), anyInt(), anyString(), anyString())).thenReturn(garages);

        mockMvc.perform(get("/garages")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Garage A"))
                .andExpect(jsonPath("$[0].address").value("123 Street"));

        verify(garageService, times(1)).getListGarages(anyInt(), anyInt(), anyString(), anyString());
    }
}

