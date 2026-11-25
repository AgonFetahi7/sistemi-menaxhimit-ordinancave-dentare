-- Te dhena testuese FIKTIVE (pa te dhena reale shendetesore, kerkesa J06)
USE dental_clinic;

INSERT INTO patient (first_name, last_name, phone, email) VALUES
 ('Arta',  'Krasniqi', '+383 44 111 222', 'arta.test@example.com'),
 ('Blerim','Hoxha',    '+383 44 333 444', 'blerim.test@example.com'),
 ('Drita', 'Berisha',  '+383 49 555 666', NULL);

INSERT INTO dentist (first_name, last_name, phone, email) VALUES
 ('Liridon','Gashi', '+383 44 777 888', 'liridon.test@example.com'),
 ('Vjosa',  'Morina','+383 49 999 000', 'vjosa.test@example.com');

INSERT INTO service (name, description, duration_minutes) VALUES
 ('Kontroll', 'Kontroll rutinor',   30),
 ('Pastrim',  'Pastrim i dhëmbëve', 45),
 ('Mbushje',  'Mbushje e dhëmbit',  60);

INSERT INTO appointment (patient_id, dentist_id, service_id, start_at, end_at, status) VALUES
 (1, 1, 1, '2026-11-20 09:00:00', '2026-11-20 09:30:00', 'SCHEDULED'),
 (2, 1, 3, '2026-11-20 10:00:00', '2026-11-20 11:00:00', 'SCHEDULED'),
 (3, 2, 2, '2026-11-20 09:00:00', '2026-11-20 09:45:00', 'SCHEDULED');
