
DROP TABLE IF EXISTS accessory CASCADE;
DROP TABLE IF EXISTS day_of_week CASCADE;
DROP TABLE IF EXISTS garage CASCADE;
DROP TABLE IF EXISTS opening_time CASCADE;
DROP TABLE IF EXISTS vehicle CASCADE;

CREATE TABLE day_of_week (
    day_id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    day_name VARCHAR(255) NOT NULL
);

CREATE TABLE garage (
    garage_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    address VARCHAR(255),
    email VARCHAR(255),
    name VARCHAR(255),
    telephone VARCHAR(255)
);

CREATE TABLE opening_time (
    opening_time_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    day_id INTEGER NOT NULL,
    end_date TIME(6) NOT NULL,
    start_date TIME(6) NOT NULL,
    garage_id BIGINT NOT NULL,
    CONSTRAINT FK_day FOREIGN KEY (day_id) REFERENCES day_of_week(day_id),
    CONSTRAINT FK_garage FOREIGN KEY (garage_id) REFERENCES garage(garage_id)
);

CREATE TABLE vehicle (
    vehicle_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    brand VARCHAR(255),
    fabrication_date VARCHAR(255),
    type_carburant VARCHAR(255),
    garage_id BIGINT,
    CONSTRAINT FK_garage_vehicle FOREIGN KEY (garage_id) REFERENCES garage(garage_id)
);

CREATE TABLE accessory (
    accessory_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(255),
    name VARCHAR(255),
    price VARCHAR(255),
    type VARCHAR(255),
    vehicle_id BIGINT,
    CONSTRAINT FK_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

INSERT INTO day_of_week (day_name)
VALUES
    ('MONDAY'),
    ('TUESDAY'),
    ('WEDNESDAY'),
    ('THURSDAY'),
    ('FRIDAY'),
    ('SATURDAY'),
    ('SUNDAY');