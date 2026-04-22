package org.example.flights.tankincident;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("TankIncident")
public record TankIncident(
        @Id
        @Column("IncidentID")
        Integer incidentId,

        @Column("TankID")
        Integer tankId,

        @Column("IncidentDate")
        LocalDate incidentDate,

        @Column("IncidentType")
        String incidentType,

        @Column("Description")
        String description,

        @Column("SeverityLevel")
        String severityLevel
) {
}