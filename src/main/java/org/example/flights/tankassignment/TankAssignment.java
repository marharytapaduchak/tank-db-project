package org.example.flights.tankassignment;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("TankAssignment")
public record TankAssignment(
        @Id
        @Column("AssignmentID")
        Integer assignmentId,

        @Column("TankID")
        Integer tankId,

        @Column("StartDate")
        LocalDate startDate,

        @Column("EndDate")
        LocalDate endDate
) {
}