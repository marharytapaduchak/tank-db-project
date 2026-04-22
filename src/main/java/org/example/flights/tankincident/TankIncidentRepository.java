package org.example.flights.tankincident;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TankIncidentRepository extends CrudRepository<TankIncident, Integer> {

    @Query("""
        SELECT IncidentID, TankID, IncidentDate, IncidentType, Description, SeverityLevel
        FROM TankIncident
        ORDER BY IncidentID
        """)
    List<TankIncident> findAllOrdered();

    @Query("""
        SELECT IncidentID, TankID, IncidentDate, IncidentType, Description, SeverityLevel
        FROM TankIncident
        WHERE IncidentID = :incidentId
        """)
    Optional<TankIncident> findByIncidentId(Integer incidentId);

    @Modifying
    @Query("""
        DELETE FROM TankIncident
        WHERE IncidentID = :incidentId
        """)
    void deleteIncidentById(Integer incidentId);

    @Query("""
        SELECT COALESCE(MAX(IncidentID), 0) + 1
        FROM TankIncident
        """)
    Integer nextId();

    @Modifying
    @Query("""
        INSERT INTO TankIncident (IncidentID, TankID, IncidentDate, IncidentType, Description, SeverityLevel)
        VALUES (:incidentId, :tankId, :incidentDate, :incidentType, :description, :severityLevel)
        """)
    void insertIncident(
            Integer incidentId,
            Integer tankId,
            LocalDate incidentDate,
            String incidentType,
            String description,
            String severityLevel
    );
}