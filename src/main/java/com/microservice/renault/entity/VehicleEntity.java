package com.microservice.renault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Vehicle")
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId;
    private String brand;
    @Column(name = "fabrication_date")
    private String fabricationDate;
    @Column(name = "type_carburant")
    private String typeCarburant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    private GarageEntity garage;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AccessoryEntity> accessoryEntities;

    public List<AccessoryEntity> getAccessoryEntities() {
        if(this.accessoryEntities==null){
            return new ArrayList<>();
        }
        return accessoryEntities;
    }
}
