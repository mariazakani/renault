package com.microservice.renault.exception;

import com.microservice.renault.dto.ErrorDetailsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handleRessourceNotFoundException(ResourceNotFoundException exception){

        ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), exception.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDto> handleGlobalException(Exception exception){

        ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), exception.getMessage());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GarageCapacityExceededException.class)
    public ResponseEntity<ErrorDetailsDto> handleGarageCapacityExceededException(GarageCapacityExceededException exception){

        ErrorDetailsDto errorDetails = new ErrorDetailsDto(new Date(), exception.getMessage());

        return  new ResponseEntity<>(errorDetails, HttpStatus.INSUFFICIENT_STORAGE);
    }
}
