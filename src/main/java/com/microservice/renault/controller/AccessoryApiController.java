package com.microservice.renault.controller;

import com.microservice.renault.dto.AccessoryDto;
import com.microservice.renault.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AccessoryApiController {

    private final AccessoryService accessoryService;

    @PostMapping("accessory/create")
    public ResponseEntity<AccessoryDto> createAccessory(@RequestBody AccessoryDto accessory, @RequestParam String brand){
        return new ResponseEntity<>(accessoryService.createAccessoryForVehicle(accessory, brand), HttpStatus.CREATED);
    }


}
