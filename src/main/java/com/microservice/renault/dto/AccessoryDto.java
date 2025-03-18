package com.microservice.renault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AccessoryDto {
    private String name;
    private String description;
    private String price;
    private String type;
}
