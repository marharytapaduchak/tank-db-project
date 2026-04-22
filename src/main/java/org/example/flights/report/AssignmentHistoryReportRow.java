package org.example.flights.report;

import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

public record AssignmentHistoryReportRow(
        @Column("tankId")
        Integer tankId,

        @Column("factorySerialNumber")
        String factorySerialNumber,

        @Column("assignmentId")
        Integer assignmentId,

        @Column("startDate")
        LocalDate startDate,

        @Column("endDate")
        LocalDate endDate
) {
}