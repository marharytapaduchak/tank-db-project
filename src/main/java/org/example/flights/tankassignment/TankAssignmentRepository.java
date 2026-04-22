package org.example.flights.tankassignment;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TankAssignmentRepository extends CrudRepository<TankAssignment, Integer> {

    @Query("""
        SELECT AssignmentID, TankID, StartDate, EndDate
        FROM TankAssignment
        ORDER BY AssignmentID
        """)
    List<TankAssignment> findAllOrdered();

    @Query("""
        SELECT AssignmentID, TankID, StartDate, EndDate
        FROM TankAssignment
        WHERE AssignmentID = :assignmentId
        """)
    Optional<TankAssignment> findByAssignmentId(Integer assignmentId);

    @Query("""
        SELECT COALESCE(MAX(AssignmentID), 0) + 1
        FROM TankAssignment
        """)
    Integer nextId();

    @Modifying
    @Query("""
        INSERT INTO TankAssignment (AssignmentID, TankID, StartDate, EndDate)
        VALUES (:assignmentId, :tankId, :startDate, :endDate)
        """)
    void insertAssignment(
            Integer assignmentId,
            Integer tankId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Modifying
    @Query("""
        DELETE FROM TankAssignment
        WHERE AssignmentID = :assignmentId
        """)
    void deleteAssignmentById(Integer assignmentId);
}