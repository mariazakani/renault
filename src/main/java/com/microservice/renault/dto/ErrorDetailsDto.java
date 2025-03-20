package com.microservice.renault.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorDetailsDto(Date timestamp, String message) {

}
