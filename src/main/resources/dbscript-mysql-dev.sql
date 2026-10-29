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

-- create tables

CREATE TABLE student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT NOT NULL UNIQUE,
    student_number VARCHAR(30) NOT NULL UNIQUE,
    academic_level VARCHAR(50),
    year_level VARCHAR(30),
    section_id INT,
    CONSTRAINT fk_student_person
        FOREIGN KEY (person_id)
        REFERENCES person(person_id)
);

CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT NOT NULL UNIQUE,
    staff_number VARCHAR(30) NOT NULL UNIQUE,
    position VARCHAR(50) NOT NULL,
    department VARCHAR(100),
    CONSTRAINT fk_staff_person
        FOREIGN KEY (person_id)
        REFERENCES person(person_id)
);

CREATE TABLE medical_record (
    medical_record_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL UNIQUE,
    medical_conditions TEXT,
    CONSTRAINT fk_medical_record_student
        FOREIGN KEY (student_id)
        REFERENCES student(student_id)
);



create table role (
    id            int primary key auto_increment,
    role          varchar(128),
    description   varchar(64)
);

create table login (
    id               int primary key auto_increment,
    username         varchar(16),
    password         varchar(64),
    role_id          int,
    last_login_date  timestamp null,
    foreign key (role_id) references role (id)
);

create table person (
    id          int primary key auto_increment,
    first_name  varchar(16),
    last_name   varchar(64),
    login_id    int,
    foreign key (login_id) references login (id)
);

-- create test data

INSERT INTO student
    (person_id, student_number, academic_level,
     year_level, section_id)
VALUES
    (1, 'CT22-0199', 'College', '3rd Year', 1),
    (2, 'CT33-0777', 'College', '3rd Year', 1),
    (3, 'CT27-0987', 'College', '2nd Year', 2);

INSERT INTO staff
    (person_id, staff_number, position, department)
VALUES
    (4, 'STF-001', 'Nurse', 'Clinic'),
    (5, 'STF-002', 'Doctor', 'Clinic'),
    (6, 'STF-003', 'Nurse', 'Clinic');

INSERT INTO medical_record
    (student_id, medical_conditions)
VALUES
    (1, 'Asthma'),
    (2, 'None'),
    (3, 'Peanut allergy');


insert into role (id, role, description)
values (1, 'Clinic Staff', 'The account for a clinic staff');

insert into login (id, username, password, role_id, last_login_date)
values (1, 'staff', '1234', 1, null);

insert into person (id, first_name, last_name, login_id)
values (3, 'John', 'Doe', 1);