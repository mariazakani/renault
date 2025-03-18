package com.microservice.renault.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VehicleDto {
    private String brand;
    private String fabricationDate;
    private String typeCarburant;
    private List<AccessoryDto> accessoryList = new ArrayList<>();


}
