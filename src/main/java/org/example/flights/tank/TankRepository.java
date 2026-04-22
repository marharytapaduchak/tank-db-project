package org.example.flights.tank;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TankRepository extends CrudRepository<Tank, Integer> {

    @Query("""
        SELECT TankID, FactorySerialNumber, ProductionYear, CurrentBaseLocation, Country, City, FacilityName, ModelID
        FROM Tank
        ORDER BY TankID
        """)
    List<Tank> findAllOrdered();

    @Query("""
        SELECT TankID, FactorySerialNumber, ProductionYear, CurrentBaseLocation, Country, City, FacilityName, ModelID
        FROM Tank
        WHERE TankID = :tankId
        """)
    Optional<Tank> findByTankId(Integer tankId);

    @Query("""
        SELECT COALESCE(MAX(TankID), 0) + 1
        FROM Tank
        """)
    Integer nextId();

    @Modifying
    @Query("""
        INSERT INTO Tank (TankID, FactorySerialNumber, ProductionYear, CurrentBaseLocation, Country, City, FacilityName, ModelID)
        VALUES (:tankId, :factorySerialNumber, :productionYear, :currentBaseLocation, :country, :city, :facilityName, :modelId)
        """)
    void insertTank(
            Integer tankId,
            String factorySerialNumber,
            Integer productionYear,
            String currentBaseLocation,
            String country,
            String city,
            String facilityName,
            Integer modelId
    );

    @Modifying
    @Query("""
        DELETE FROM Tank
        WHERE TankID = :tankId
        """)
    void deleteTankById(Integer tankId);
}