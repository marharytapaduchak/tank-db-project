CREATE TABLE Manufacturer
(
    ManufacturerID   INT NOT NULL PRIMARY KEY,
    ManufacturerName VARCHAR(255) NOT NULL,
    CountryOfOrigin  VARCHAR(100) NOT NULL,
    FoundedYear      INT NOT NULL
);

CREATE TABLE TankModel
(
    ModelID         INT NOT NULL PRIMARY KEY,
    ModelName       VARCHAR(255) NOT NULL,
    CrewSize        INT NOT NULL,
    ManufacturerID  INT NOT NULL,

    CONSTRAINT fk_tankmodel_manufacturer
        FOREIGN KEY (ManufacturerID)
            REFERENCES Manufacturer (ManufacturerID)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);

CREATE TABLE MilitaryUnit
(
    UnitID             INT NOT NULL PRIMARY KEY,
    UnitName           VARCHAR(255) NOT NULL,
    UnitType           VARCHAR(100) NOT NULL,
    HomeBaseLocation   VARCHAR(255) NOT NULL,
    Country            VARCHAR(100) NOT NULL,
    City               VARCHAR(100) NOT NULL,
    BaseName           VARCHAR(255) NOT NULL
);

CREATE TABLE Tank
(
    TankID               INT NOT NULL PRIMARY KEY,
    FactorySerialNumber  VARCHAR(100) NOT NULL,
    ProductionYear       INT NOT NULL,
    CurrentBaseLocation  VARCHAR(255) NOT NULL,
    Country              VARCHAR(100) NOT NULL,
    City                 VARCHAR(100) NOT NULL,
    FacilityName         VARCHAR(255) NOT NULL,
    ModelID              INT NOT NULL,

    CONSTRAINT uq_tank_serial UNIQUE (FactorySerialNumber),

    CONSTRAINT fk_tank_model
        FOREIGN KEY (ModelID)
            REFERENCES TankModel (ModelID)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);

CREATE TABLE TankRadioFrequency
(
    TankID      INT NOT NULL,
    Frequency   VARCHAR(50) NOT NULL,

    CONSTRAINT pk_tankradiofrequency
        PRIMARY KEY (TankID, Frequency),

    CONSTRAINT fk_tankradiofrequency_tank
        FOREIGN KEY (TankID)
            REFERENCES Tank (TankID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE TankAssignment
(
    AssignmentID INT NOT NULL PRIMARY KEY,
    TankID       INT NOT NULL,
    StartDate    DATE NOT NULL,
    EndDate      DATE NULL,

    CONSTRAINT fk_tankassignment_tank
        FOREIGN KEY (TankID)
            REFERENCES Tank (TankID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE MaintenanceRecord
(
    RecordID          INT NOT NULL PRIMARY KEY,
    TankID            INT NOT NULL,
    UnitID            INT NOT NULL,
    MaintenanceType   VARCHAR(255) NOT NULL,
    MaintenanceDate   DATE NOT NULL,

    CONSTRAINT fk_maintenancerecord_tank
        FOREIGN KEY (TankID)
            REFERENCES Tank (TankID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,

    CONSTRAINT fk_maintenancerecord_unit
        FOREIGN KEY (UnitID)
            REFERENCES MilitaryUnit (UnitID)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);

CREATE TABLE Inspection
(
    InspectionID      INT NOT NULL PRIMARY KEY,
    RecordID          INT NOT NULL,
    InspectionType    VARCHAR(255) NOT NULL,
    InspectionResult  VARCHAR(255) NOT NULL,

    CONSTRAINT fk_inspection_record
        FOREIGN KEY (RecordID)
            REFERENCES MaintenanceRecord (RecordID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

CREATE TABLE TankIncident
(
    IncidentID      INT NOT NULL PRIMARY KEY,
    TankID          INT NOT NULL,
    IncidentDate    DATE NOT NULL,
    IncidentType    VARCHAR(255) NOT NULL,
    Description     TEXT NULL,
    SeverityLevel   VARCHAR(50) NOT NULL,

    CONSTRAINT fk_tankincident_tank
        FOREIGN KEY (TankID)
            REFERENCES Tank (TankID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);