package org.example.flights.report;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<AssignmentHistoryReportRow> getAssignmentHistory(Integer tankId) {
        String sql = """
            SELECT
                t.TankID AS tankId,
                t.FactorySerialNumber AS factorySerialNumber,
                a.AssignmentID AS assignmentId,
                a.StartDate AS startDate,
                a.EndDate AS endDate
            FROM Tank t
            LEFT JOIN TankAssignment a ON t.TankID = a.TankID
            WHERE t.TankID = ?
            ORDER BY a.StartDate
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AssignmentHistoryReportRow(
                rs.getInt("tankId"),
                rs.getString("factorySerialNumber"),
                rs.getObject("assignmentId", Integer.class),
                rs.getObject("startDate", java.time.LocalDate.class),
                rs.getObject("endDate", java.time.LocalDate.class)
        ), tankId);
    }

    public List<MaintenanceHistoryReportRow> getMaintenanceHistory(Integer tankId) {
        String sql = """
            SELECT
                t.TankID AS tankId,
                t.FactorySerialNumber AS factorySerialNumber,
                m.RecordID AS recordId,
                m.MaintenanceType AS maintenanceType,
                m.MaintenanceDate AS maintenanceDate,
                i.InspectionID AS inspectionId,
                i.InspectionType AS inspectionType,
                i.InspectionResult AS inspectionResult
            FROM Tank t
            LEFT JOIN MaintenanceRecord m ON t.TankID = m.TankID
            LEFT JOIN Inspection i ON m.RecordID = i.RecordID
            WHERE t.TankID = ?
            ORDER BY m.MaintenanceDate, i.InspectionID
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MaintenanceHistoryReportRow(
                rs.getInt("tankId"),
                rs.getString("factorySerialNumber"),
                rs.getObject("recordId", Integer.class),
                rs.getString("maintenanceType"),
                rs.getObject("maintenanceDate", java.time.LocalDate.class),
                rs.getObject("inspectionId", Integer.class),
                rs.getString("inspectionType"),
                rs.getString("inspectionResult")
        ), tankId);
    }

    public List<ReadinessOverviewReportRow> getReadinessOverview(Integer tankId) {
        String sql = """
            SELECT
                t.TankID AS tankId,
                t.FactorySerialNumber AS factorySerialNumber,
                COUNT(DISTINCT ti.IncidentID) AS totalIncidents,
                MAX(m.MaintenanceDate) AS lastMaintenanceDate,
                MAX(a.StartDate) AS lastAssignmentStartDate,
                (
                    SELECT i2.InspectionResult
                    FROM MaintenanceRecord m2
                    LEFT JOIN Inspection i2 ON m2.RecordID = i2.RecordID
                    WHERE m2.TankID = t.TankID
                    ORDER BY m2.MaintenanceDate DESC, i2.InspectionID DESC
                    LIMIT 1
                ) AS latestInspectionResult
            FROM Tank t
            LEFT JOIN TankIncident ti ON t.TankID = ti.TankID
            LEFT JOIN MaintenanceRecord m ON t.TankID = m.TankID
            LEFT JOIN TankAssignment a ON t.TankID = a.TankID
            WHERE t.TankID = ?
            GROUP BY t.TankID, t.FactorySerialNumber
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new ReadinessOverviewReportRow(
                rs.getInt("tankId"),
                rs.getString("factorySerialNumber"),
                rs.getInt("totalIncidents"),
                rs.getObject("lastMaintenanceDate", java.time.LocalDate.class),
                rs.getObject("lastAssignmentStartDate", java.time.LocalDate.class),
                rs.getString("latestInspectionResult")
        ), tankId);
    }
}