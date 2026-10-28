-- drop user and database
drop user if exists 'rcis'@'localhost';
drop database if exists rcis;

-- create database
create database rcis;

-- create user
create user 'rcis'@'localhost' identified by 'Changeme0';

-- grant basic permissions
grant all privileges on rcis.* to 'rcis'@'localhost';
flush privileges;

use rcis;

CREATE TABLE IF NOT EXISTS person (
    person_id INT AUTO_INCREMENT PRIMARY KEY
  , last_name VARCHAR(50) NOT NULL
  , first_name VARCHAR(50) NOT NULL
  , middle_name VARCHAR(50)
  , email VARCHAR(100) UNIQUE
  , date_of_birth DATE
  , gender VARCHAR(20)
  , address VARCHAR(255)
  , contact_number VARCHAR(20)
);


CREATE TABLE IF NOT EXISTS staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY
  , person_id INT NOT NULL UNIQUE
  , staff_number VARCHAR(20) NOT NULL UNIQUE
  , position VARCHAR(50) NOT NULL
  , department VARCHAR(100)
  , FOREIGN KEY (person_id) REFERENCES person(person_id)
);


CREATE TABLE IF NOT EXISTS student (
    student_id INT AUTO_INCREMENT PRIMARY KEY
  , person_id INT NOT NULL UNIQUE
  , student_number VARCHAR(20) NOT NULL UNIQUE
  , academic_level VARCHAR(30) NOT NULL
  , year_level VARCHAR(20) NOT NULL
  , section_id INT
  , FOREIGN KEY (person_id) REFERENCES person(person_id)
);


CREATE TABLE IF NOT EXISTS medical_record (
    medical_record_id INT AUTO_INCREMENT PRIMARY KEY
  , student_id INT NOT NULL
  , medical_conditions TEXT
  , FOREIGN KEY (student_id) REFERENCES student(student_id)
);


CREATE TABLE IF NOT EXISTS gatepass (
    gatepass_id INT AUTO_INCREMENT PRIMARY KEY
  , student_id INT NOT NULL
  , staff_id INT NOT NULL
  , medical_record_id INT NOT NULL
  , medical_reason TEXT NOT NULL
  , issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  , FOREIGN KEY (student_id) REFERENCES student(student_id)
  , FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
  , FOREIGN KEY (medical_record_id) REFERENCES medical_record(medical_record_id)
);

-- test data
-- staff person
INSERT INTO person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number) VALUES
    ('Ambunan', 'Zywynne', 'Rubiano', 'zywynneambunan@gmail.com', DATE '2005-09-20', 'Male', 'Lumil-Silang-Cavite', '09707311233');

-- student person
INSERT INTO person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number) VALUES
    ('Santos', 'Maria', 'L', 'maria.santos@rcis.edu', DATE '2008-07-21', 'Female', '45 Rizal Ave, San Pedro, Laguna', '09181234567');

INSERT INTO staff (person_id, staff_number, position, department) VALUES
    (1, 'N-01', 'Nurse', 'Clinic');

INSERT INTO student (person_id, student_number, academic_level, year_level, section_id) VALUES
    (2, 'STU-0001', 'Senior High School', 'Grade 11', 1);

INSERT INTO medical_record (student_id, medical_conditions) VALUES
    (1, 'Asthma');

INSERT INTO gatepass (student_id, staff_id, medical_record_id, medical_reason) VALUES
    (1, 1, 1, 'Asthma attack, needs to go home for rest');