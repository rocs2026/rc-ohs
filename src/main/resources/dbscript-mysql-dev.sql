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
    id              int primary key auto_increment,
    first_name      varchar(16),
    middle_name     varchar(16),
    last_name       varchar(64),
    email           varchar(128),
    date_of_birth   date,
    gender          varchar(16),
    address         varchar(255),
    contact_number  varchar(16),
    login_id        int,
    foreign key (login_id) references login (id)
);

create table student (
    student_id int primary key auto_increment,
    person_id int not null unique,
    student_number varchar(30) not null unique,
    academic_level varchar(50),
    year_level varchar(30),
    section_id int,
    foreign key (person_id) references person (id)
);

create table staff (
    staff_id int primary key auto_increment,
    person_id int not null unique,
    staff_number varchar(30) not null unique,
    position varchar(50) not null,
    department varchar(100),
    foreign key (person_id) references person (id)
);

create table medical_record (
    medical_record_id int primary key auto_increment,
    student_id int not null unique,
    medical_conditions text,
    foreign key (student_id) references student (student_id)
);

create table consultation (
    consultation_id int primary key auto_increment,
    medical_record_id int not null,
    staff_id int not null,
    record_type varchar(50),
    consultation_date datetime not null,
    complaint text,
    diagnosis text not null,
    treatment text not null,
    action_taken text,
    remarks text,
    foreign key (medical_record_id) references medical_record (medical_record_id),
    foreign key (staff_id) references staff (staff_id)
);

create table medicine (
    medicine_id int primary key auto_increment,
    medicine_name varchar(100) not null,
    category varchar(100),
    unit varchar(30) not null
);

create table inventory (
    item_id int primary key auto_increment,
    medicine_id int not null,
    staff_id int not null,
    batch_number varchar(50),
    stock_quantity int not null default 0,
    expiration_date date,
    foreign key (medicine_id) references medicine (medicine_id),
    foreign key (staff_id) references staff(staff_id),
    check (stock_quantity >= 0)
);

create table prescription (
    prescription_id int primary key auto_increment,
    consultation_id int not null,
    item_id int not null,
    staff_id int not null,
    medication_quantity int not null,
    dosage varchar(100) not null,
    frequency varchar(100) not null,
    duration varchar(100),
    instructions text,
    foreign key (consultation_id) references consultation (consultation_id),
    foreign key (item_id) references inventory (item_id),
    foreign key (staff_id) references staff (staff_id),
    check (medication_quantity > 0)
);

-- create test data
insert into role (role, description)
values ('Clinic Staff', 'The account for a clinic staff');

insert into login (username, password, role_id, last_login_date)
values ('staff', '1234', 1, null);


insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number, login_id)
values ('John', 'Michael', 'Doe', 'john.doe@example.com', '2015-05-15', 'Male', '123 Rizal Street, San Pedro, Laguna', '09191223222', 1);

-- CA-6 Consultation test data
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number, login_id) values
('Juan', 'Santos', 'Dela Cruz', 'juan.delacruz@rocs.edu.ph', '2005-03-14', 'Male', 'Quezon City', '09171234501', null),
('Maria', 'Lopez', 'Reyes', 'maria.reyes@rocs.edu.ph', '2004-08-22', 'Female', 'Marikina City', '09171234502', null),
('Pedro', 'Cruz', 'Garcia', 'pedro.garcia@rocs.edu.ph', '2005-01-09', 'Male', 'Pasig City', '09171234503', null),
('Ana', 'Ramos', 'Mendoza', 'ana.mendoza@rocs.edu.ph', '1990-05-17', 'Female', 'Manila', '09171234504', null),
('Carlos', 'Diaz', 'Santos', 'carlos.santos@rocs.edu.ph', '1985-11-02', 'Male', 'Makati City', '09171234505', null),
('Liza', 'Perez', 'Villanueva', 'liza.villanueva@rocs.edu.ph', '1992-07-30', 'Female', 'Taguig City', '09171234506', null);

insert into student (person_id, student_number, academic_level, year_level, section_id) values
((select id from person where email = 'juan.delacruz@rocs.edu.ph'), 'CT22-0166', 'College', '3rd Year', 1),
((select id from person where email = 'maria.reyes@rocs.edu.ph'), 'CT23-0987', 'College', '3rd Year', 1),
((select id from person where email = 'pedro.garcia@rocs.edu.ph'), 'CT33-0333', 'College', '2nd Year', 2);

insert into staff (person_id, staff_number, position, department) values
((select id from person where email = 'ana.mendoza@rocs.edu.ph'), 'STF-001', 'Nurse', 'Clinic'),
((select id from person where email = 'carlos.santos@rocs.edu.ph'), 'STF-002', 'Doctor', 'Clinic'),
((select id from person where email = 'liza.villanueva@rocs.edu.ph'), 'STF-003', 'Nurse', 'Clinic');

insert into medical_record (student_id, medical_conditions) values
(1, 'Asthma'),
(2, 'None'),
(3, 'Peanut allergy');

insert into medicine (medicine_name, category, unit) values
('Paracetamol 500mg', 'Analgesic', 'tablet'),
('Amoxicillin 500mg', 'Antibiotic', 'capsule'),
('Cetirizine 10mg', 'Antihistamine', 'tablet');

insert into inventory (medicine_id, batch_number, stock_quantity, expiration_date) values
(1, 'B-001', 100, '2027-12-31'),
(2, 'B-002', 50, '2027-06-30'),
(3, 'B-003', 80, '2028-01-31');

insert into consultation (medical_record_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks) values
(1, 1, 'Check-up', '2026-10-20 09:30:00', 'Headache and fever', 'Viral fever', 'Rest and plenty of fluids', 'Gave medicine', 'Follow up if fever persists'),
(2, 2, 'Check-up', '2026-10-21 10:15:00', 'Cough and sore throat', 'Bacterial throat infection', 'Antibiotic course', 'Prescribed medicine', 'Return after 7 days'),
(3, 3, 'Emergency', '2026-10-22 14:00:00', 'Itchy skin and rashes', 'Allergic reaction', 'Antihistamine', 'Gave medicine', 'Monitor for 1 hour');

insert into prescription (consultation_id, item_id, staff_id, medication_quantity, dosage, frequency, duration, instructions) values
(1, 1, 1, 10, '500mg', 'Every 6 hours', '3 days', 'Take after meals'),
(2, 2, 2, 21, '500mg', '3 times a day', '7 days', 'Finish all capsules'),
(3, 3, 3, 5, '10mg', 'Once a day', '5 days', 'Take at night');