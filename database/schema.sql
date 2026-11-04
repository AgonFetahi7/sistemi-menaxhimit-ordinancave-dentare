-- Sistemi per Menaxhimin e Ordinancave Dentare - skema (Raporti Teknik 2)
-- Kerkon MySQL 8.0.16+ (qe CHECK constraints te zbatohen). Nuk fshin asgje:
-- nese databaza 'dental_clinic' ekziston, skripti ndalet me gabim.
CREATE DATABASE dental_clinic CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE dental_clinic;

CREATE TABLE patient (
    patient_id  BIGINT       NOT NULL AUTO_INCREMENT,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    phone       VARCHAR(30)  NOT NULL,
    email       VARCHAR(100) NULL,
    PRIMARY KEY (patient_id),
    CONSTRAINT uq_patient_email UNIQUE (email),
    INDEX idx_patient_last_name (last_name)
) ENGINE=InnoDB;

CREATE TABLE dentist (
    dentist_id  BIGINT       NOT NULL AUTO_INCREMENT,
    first_name  VARCHAR(50)  NOT NULL,
    last_name   VARCHAR(50)  NOT NULL,
    phone       VARCHAR(30)  NOT NULL,
    email       VARCHAR(100) NULL,
    PRIMARY KEY (dentist_id),
    CONSTRAINT uq_dentist_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE service (
    service_id       BIGINT       NOT NULL AUTO_INCREMENT,
    name             VARCHAR(100) NOT NULL,
    description      VARCHAR(255) NULL,
    duration_minutes INT          NOT NULL,
    PRIMARY KEY (service_id),
    CONSTRAINT uq_service_name UNIQUE (name),
    CONSTRAINT chk_service_duration CHECK (duration_minutes > 0)
) ENGINE=InnoDB;

CREATE TABLE appointment (
    appointment_id BIGINT      NOT NULL AUTO_INCREMENT,
    patient_id     BIGINT      NOT NULL,
    dentist_id     BIGINT      NOT NULL,
    service_id     BIGINT      NOT NULL,
    start_at       DATETIME    NOT NULL,
    end_at         DATETIME    NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    PRIMARY KEY (appointment_id),
    CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patient (patient_id),
    CONSTRAINT fk_appt_dentist FOREIGN KEY (dentist_id) REFERENCES dentist (dentist_id),
    CONSTRAINT fk_appt_service FOREIGN KEY (service_id) REFERENCES service (service_id),
    CONSTRAINT chk_appt_status CHECK (status IN ('SCHEDULED','COMPLETED','CANCELLED')),
    CONSTRAINT chk_appt_time   CHECK (end_at > start_at),
    -- perdoret nga kontrolli i perplasjes se orarit (dentisti + koha)
    INDEX idx_appt_dentist_time (dentist_id, start_at, end_at)
) ENGINE=InnoDB;

CREATE TABLE visit (
    visit_id       BIGINT NOT NULL AUTO_INCREMENT,
    appointment_id BIGINT NOT NULL,
    visit_date     DATE   NOT NULL,
    notes          TEXT   NULL,
    PRIMARY KEY (visit_id),
    -- 1 termin = max 1 vizite (relacion 1:1)
    CONSTRAINT uq_visit_appointment UNIQUE (appointment_id),
    CONSTRAINT fk_visit_appointment FOREIGN KEY (appointment_id) REFERENCES appointment (appointment_id)
) ENGINE=InnoDB;
