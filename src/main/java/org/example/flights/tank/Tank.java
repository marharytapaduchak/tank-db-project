package org.example.flights.tank;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("Tank")
public record Tank(
        @Id
        @Column("TankID")
        Integer tankId,

        @Column("FactorySerialNumber")
        String factorySerialNumber,

        @Column("ProductionYear")
        Integer productionYear,

        @Column("CurrentBaseLocation")
        String currentBaseLocation,

        @Column("Country")
        String country,

        @Column("City")
        String city,

        @Column("FacilityName")
        String facilityName,

        @Column("ModelID")
        Integer modelId
) {
}