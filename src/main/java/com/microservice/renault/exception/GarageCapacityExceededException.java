package com.microservice.renault.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@ResponseStatus(HttpStatus.INSUFFICIENT_STORAGE)
public class GarageCapacityExceededException extends RuntimeException{

    public GarageCapacityExceededException(String message) {
        super(message);
    }
}
