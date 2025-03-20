package com.microservice.renault.dto;

import lombok.Builder;

@Builder
public record AccessoryDto(String name, String description, String price, String type) {

}
