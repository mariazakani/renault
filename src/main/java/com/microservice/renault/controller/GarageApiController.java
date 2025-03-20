package com.microservice.renault.controller;

import com.microservice.renault.dto.GarageDto;
import com.microservice.renault.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/garages")
public class GarageApiController {

    private final GarageService garageService;

    @GetMapping("/{id}")
    public ResponseEntity<GarageDto> getGarageInformation(@PathVariable("id") Long garageId){

        return new ResponseEntity<>(garageService.getGarage(garageId), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<GarageDto> createGarage(@RequestBody GarageDto garage){
        return new ResponseEntity<>(garageService.createGarage(garage), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<GarageDto> updateGarage(@RequestBody GarageDto garage, @PathVariable("id") Long idGarage){
        return new ResponseEntity<>(garageService.updateGarage(garage, idGarage), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteGarage(@PathVariable("id") Long idGarage){
        garageService.deleteGarage(idGarage);
        return ResponseEntity.ok("Garage deleted successfully");
    }

    @GetMapping("/listgarage")
    public ResponseEntity<List<GarageDto>> getGarages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String sortBy,
            @RequestParam String sortDirection) {
        return new ResponseEntity<>(garageService.getListGarages(page, size, sortBy, sortDirection), HttpStatus.OK);
    }

    @GetMapping("/searchbyvehicle/{brand}")
    public ResponseEntity<List<GarageDto>> searchGaragesByVehicleBrand(@PathVariable String brand){
        return new ResponseEntity<>(garageService.searchGaragesByVehicleBrand(brand), HttpStatus.OK);
    }
    @GetMapping("/searchbyaccessory/{accessory}")
    public ResponseEntity<List<GarageDto>> searchGaragesByAccessoryName(@PathVariable("accessory") String accessoryName){
        return new ResponseEntity<>(garageService.searchGaragesByAccessoryName(accessoryName), HttpStatus.OK);
    }
}
