package org.example.flights.report;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

public record ReadinessOverviewReportRow(
        @Column("tankId")
        Integer tankId,

        @Column("factorySerialNumber")
        String factorySerialNumber,

        @Column("totalIncidents")
        Integer totalIncidents,

        @Column("lastMaintenanceDate")
        LocalDate lastMaintenanceDate,

        @Column("lastAssignmentStartDate")
        LocalDate lastAssignmentStartDate,

        @Column("latestInspectionResult")
        String latestInspectionResult
) {
}