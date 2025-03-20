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
import java.util.Collections;
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

    private static final String GARAGE_NAME = "Garage A";
    private static final String GARAGE_ADDRESS = "123 Street";
    private static final String GARAGE_EMAIL = "contact@garagea.com";
    private static final String GARAGE_TEL = "1234567890";

    @BeforeEach
    public void setUp() {
        garageDto = GarageDto.builder()
                .name(GARAGE_NAME)
                .address(GARAGE_ADDRESS)
                .email(GARAGE_EMAIL)
                .telephone(GARAGE_TEL)
                .build();
    }

    @Test
    public void return_garage_information_when_id_provided() throws Exception {
        var garageId = 1L;

        when(garageService.getGarage(garageId)).thenReturn(garageDto);

        mockMvc.perform(get("/api/v1/garages/{id}", garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GARAGE_NAME))
                .andExpect(jsonPath("$.address").value(GARAGE_ADDRESS));

        verify(garageService, times(1)).getGarage(garageId);
    }

    @Test
    public void create_garage_when_body_provided() throws Exception {
        when(garageService.createGarage(any(GarageDto.class))).thenReturn(garageDto);

        mockMvc.perform(post("/api/v1/garages/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(GARAGE_NAME))
                .andExpect(jsonPath("$.address").value(GARAGE_ADDRESS));

        verify(garageService, times(1)).createGarage(any(GarageDto.class));
    }

    @Test
    public void update_garage_information_when_body_provided() throws Exception {
        var garageId = 1L;
        when(garageService.updateGarage(any(GarageDto.class), eq(garageId))).thenReturn(garageDto);

        mockMvc.perform(put("/api/v1/garages/{id}/update", garageId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(garageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(GARAGE_NAME))
                .andExpect(jsonPath("$.address").value(GARAGE_ADDRESS));

        verify(garageService, times(1)).updateGarage(any(GarageDto.class), eq(garageId));
    }

    @Test
    public void delete_garage_when_id_provided() throws Exception {
        Long garageId = 1L;
        doNothing().when(garageService).deleteGarage(garageId);
        mockMvc.perform(delete("/api/v1/garages/{id}/delete", garageId))
                .andExpect(status().isOk())
                .andExpect(content().string("Garage deleted successfully"));

        verify(garageService, times(1)).deleteGarage(garageId);
    }

    @Test
    public void return_all_list_garage() throws Exception {
        List<GarageDto> garages = Collections.singletonList(garageDto);
        when(garageService.getListGarages(anyInt(), anyInt(), anyString(), anyString())).thenReturn(garages);

        mockMvc.perform(get("/api/v1/garages/listgarage")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(GARAGE_NAME))
                .andExpect(jsonPath("$[0].address").value(GARAGE_ADDRESS));

        verify(garageService, times(1)).getListGarages(anyInt(), anyInt(), anyString(), anyString());
    }
}

