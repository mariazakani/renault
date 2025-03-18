package com.microservice.renault.controller;

import com.microservice.renault.dto.VehicleDto;
import com.microservice.renault.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VehicleApiController {

    private final VehicleService vehicleService;

    @PostMapping("vehicle/create")
    public void createVehicle(@RequestBody VehicleDto vehicle){
        vehicleService.createVehicle(vehicle);
    }

    @PostMapping("vehicle/addToGarage")
    public ResponseEntity<VehicleDto> addVehicleToGarage(@RequestParam String brand, @RequestParam String garageName) throws IllegalAccessException {
        return new ResponseEntity<>(vehicleService.addVehicleToGarage(brand, garageName), HttpStatus.OK);
    }

    @GetMapping("vehicles/{garageName}")
    public ResponseEntity<List<VehicleDto>> getVehicles(@PathVariable String garageName){
        return new ResponseEntity<>(vehicleService.getVehicleForGarage(garageName), HttpStatus.OK);
    }
}
