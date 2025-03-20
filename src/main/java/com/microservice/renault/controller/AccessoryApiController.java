package com.microservice.renault.controller;

import com.microservice.renault.dto.AccessoryDto;
import com.microservice.renault.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accessories")
public class AccessoryApiController {

    private final AccessoryService accessoryService;

    @PostMapping("/create")
    public ResponseEntity<AccessoryDto> createAccessory(@RequestBody AccessoryDto accessory, @RequestParam String brand){
        return new ResponseEntity<>(accessoryService.createAccessoryForVehicle(accessory, brand), HttpStatus.CREATED);
    }


}
