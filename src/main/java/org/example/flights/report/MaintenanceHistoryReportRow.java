package org.example.flights.report;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

public record MaintenanceHistoryReportRow(
        @Column("tankId")
        Integer tankId,

        @Column("factorySerialNumber")
        String factorySerialNumber,

        @Column("recordId")
        Integer recordId,

        @Column("maintenanceType")
        String maintenanceType,

        @Column("maintenanceDate")
        LocalDate maintenanceDate,

        @Column("inspectionId")
        Integer inspectionId,

        @Column("inspectionType")
        String inspectionType,

        @Column("inspectionResult")
        String inspectionResult
) {
}