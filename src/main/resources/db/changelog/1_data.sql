INSERT INTO Manufacturer (ManufacturerID, ManufacturerName, CountryOfOrigin, FoundedYear)
VALUES
    (1, 'Rheinmetall', 'Germany', 1889),
    (2, 'General Dynamics', 'USA', 1952),
    (3, 'BAE Systems', 'UK', 1999),
    (4, 'Krauss-Maffei Wegmann', 'Germany', 1931),
    (5, 'Nexter Systems', 'France', 2006);

INSERT INTO TankModel (ModelID, ModelName, CrewSize, ManufacturerID)
VALUES
    (1, 'Leopard 2A6', 4, 4),
    (2, 'Leclerc XLR', 3, 5),
    (3, 'Challenger 2', 4, 3),
    (4, 'M1A2 Abrams', 4, 2),
    (5, 'KF51 Panther', 3, 1);

INSERT INTO MilitaryUnit (UnitID, UnitName, UnitType, HomeBaseLocation, Country, City, BaseName)
VALUES
    (1, '1st Armored Brigade', 'Armored', 'Lviv Region', 'Ukraine', 'Lviv', 'Yavoriv Training Center'),
    (2, '24th Mechanized Brigade', 'Mechanized', 'Lviv Region', 'Ukraine', 'Yavoriv', 'Central Base'),
    (3, '3rd Tank Battalion', 'Tank', 'Kharkiv Region', 'Ukraine', 'Kharkiv', 'Eastern Command Base'),
    (4, '92nd Mechanized Brigade', 'Mechanized', 'Kharkiv Region', 'Ukraine', 'Kharkiv', 'Operational Base');

INSERT INTO Tank (TankID, FactorySerialNumber, ProductionYear, CurrentBaseLocation, Country, City, FacilityName, ModelID)
VALUES
    (1, 'KMW-L2A6-001', 2016, 'Yavoriv Training Center', 'Ukraine', 'Lviv', 'Yavoriv Training Center', 1),
    (2, 'NEX-LXLR-014', 2019, 'Central Base', 'Ukraine', 'Yavoriv', 'Central Base', 2),
    (3, 'BAE-CH2-021', 2014, 'Eastern Command Base', 'Ukraine', 'Kharkiv', 'Eastern Command Base', 3),
    (4, 'GD-ABR-077', 2018, 'Operational Base', 'Ukraine', 'Kharkiv', 'Operational Base', 4),
    (5, 'RHM-KF51-003', 2025, 'Yavoriv Training Center', 'Ukraine', 'Lviv', 'Yavoriv Training Center', 5),
    (6, 'KMW-L2A6-032', 2017, 'Operational Base', 'Ukraine', 'Kharkiv', 'Operational Base', 1);

INSERT INTO TankRadioFrequency (TankID, Frequency)
VALUES
    (1, '30.100 MHz'),
    (1, '31.500 MHz'),
    (2, '30.100 MHz'),
    (2, '32.200 MHz'),
    (3, '40.000 MHz'),
    (4, '29.900 MHz'),
    (4, '31.500 MHz'),
    (5, '45.300 MHz'),
    (5, '47.800 MHz'),
    (6, '40.000 MHz'),
    (6, '48.500 MHz');

INSERT INTO TankAssignment (AssignmentID, TankID, StartDate, EndDate)
VALUES
    (1, 1, '2025-09-01', '2026-01-15'),
    (2, 1, '2026-01-16', NULL),
    (3, 2, '2025-10-10', NULL),
    (4, 3, '2025-11-05', '2026-02-01'),
    (5, 4, '2025-08-20', NULL),
    (6, 6, '2026-01-05', NULL);

INSERT INTO MaintenanceRecord (RecordID, TankID, UnitID, MaintenanceType, MaintenanceDate)
VALUES
    (1, 1, 1, 'Engine check', '2026-01-20'),
    (2, 1, 2, 'Track replacement', '2026-02-10'),
    (3, 2, 2, 'Radio calibration', '2026-02-05'),
    (4, 3, 3, 'Armor inspection', '2026-01-12'),
    (5, 4, 1, 'Oil change', '2026-02-18'),
    (6, 6, 4, 'Diagnostics', '2026-02-22');

INSERT INTO Inspection (InspectionID, RecordID, InspectionType, InspectionResult)
VALUES
    (2, 1, 'Functional', 'PASS'),
    (4, 2, 'Follow-up', 'PASS'),
    (5, 3, 'Functional', 'PASS'),
    (6, 4, 'Armor', 'PASS'),
    (7, 5, 'Safety', 'PASS'),
    (8, 6, 'Diagnostics', 'PASS');

INSERT INTO TankIncident (IncidentID, TankID, IncidentDate, IncidentType, Description, SeverityLevel)
VALUES
    (1, 1, '2026-03-10', 'Engine Failure', 'Engine overheating detected during training exercise', 'High'),
    (2, 2, '2026-03-12', 'Communication Issue', 'Radio signal was intermittently lost during field deployment', 'Medium'),
    (3, 1, '2026-03-15', 'Track Damage', 'Left track was damaged while moving on rough terrain', 'High'),
    (4, 3, '2026-03-18', 'Fuel Leak', 'Minor fuel leak detected near rear tank compartment', 'Low'),
    (5, 4, '2026-03-20', 'Optics Malfunction', 'Thermal sight stopped responding during inspection', 'Medium');