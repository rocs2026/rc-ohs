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
    id int primary key auto_increment,
    role varchar(128),
    description varchar(64)
);

create table login (
    id int primary key auto_increment,
    username varchar(16),
    password varchar(64),
    role_id int,
    last_login_date timestamp null,
    foreign key (role_id) references role (id)
);

create table person (
    id int primary key auto_increment,
    first_name varchar(16),
    middle_name varchar(16),
    last_name varchar(64),
    email varchar(128),
    date_of_birth date,
    gender varchar(16),
    address varchar(255),
    contact_number varchar(16),
    login_id int,
    foreign key (login_id) references login (id)
);

create table department (
    department_id int primary key auto_increment,
    department_name enum('Junior High School', 'Senior High School', 'College') not null unique
);

create table position (
    position_id int primary key auto_increment,
    position_name varchar(100) not null unique,
    category varchar(20) not null,
    department_id int,
    foreign key (department_id) references department (department_id),
    check (category in ('RC Employee', 'Community', 'Clinic Staff'))
);

create table personnel (
    personnel_id int primary key auto_increment,
    person_id int not null unique,
    employee_id varchar(30) unique,
    foreign key (person_id) references person (id)
);

create table personnel_position (
    personnel_id int not null,
    position_id int not null,
    primary key (personnel_id, position_id),
    foreign key (personnel_id) references personnel (personnel_id),
    foreign key (position_id) references position (position_id)
);

create table section (
    section_id int primary key auto_increment,
    code varchar(30) not null,
    name varchar(100) not null,
    department_id int not null,
    year_level varchar(30) not null,
    semester varchar(30),
    academic_year varchar(20) not null,
    adviser_id int,
    foreign key (department_id) references department (department_id),
    foreign key (adviser_id) references personnel (personnel_id),
    unique (name, semester, academic_year)
);

create table student (
    student_id int primary key auto_increment,
    person_id int not null unique,
    student_number varchar(30) not null unique,
    section_id int,
    foreign key (person_id) references person (id),
    foreign key (section_id) references section (section_id)
);

create table medical_record (
    medical_record_id int primary key auto_increment,
    person_id int not null unique,
    medical_conditions text,
    allergies text,
    foreign key (person_id) references person (id),
    unique (medical_record_id, person_id)
);

create table consultation (
    consultation_id int primary key auto_increment,
    medical_record_id int not null,
    patient_id int not null,
    personnel_id int not null,
    record_type varchar(50) not null,
    consultation_date datetime not null,
    complaint text,
    diagnosis text not null,
    treatment text not null,
    action_taken text,
    remarks text,
    foreign key (patient_id) references person (id),
    foreign key (medical_record_id, patient_id) references medical_record (medical_record_id, person_id),
    foreign key (personnel_id) references personnel (personnel_id),
    check (record_type in ('Check-up', 'Follow-up'))
);

create table medicine_category (
    category_id int primary key auto_increment,
    category_name varchar(100) not null unique
);

create table medicine (
    medicine_id int primary key auto_increment,
    medicine_name varchar(100) not null,
    category_id int,
    unit varchar(30) not null,
    foreign key (category_id) references medicine_category (category_id)
);

create table inventory (
    item_id int primary key auto_increment,
    medicine_id int not null,
    personnel_id int not null,
    batch_number varchar(50),
    stock_quantity int not null default 0,
    expiration_date date,
    foreign key (medicine_id) references medicine (medicine_id),
    foreign key (personnel_id) references personnel (personnel_id),
    check (stock_quantity >= 0)
);

create table prescription (
    prescription_id int primary key auto_increment,
    consultation_id int not null,
    item_id int not null,
    personnel_id int not null,
    quantity_to_dispense int not null,
    dose varchar(100) not null,
    frequency varchar(100) not null,
    duration varchar(100),
    instructions text,
    foreign key (consultation_id) references consultation (consultation_id),
    foreign key (item_id) references inventory (item_id),
    foreign key (personnel_id) references personnel (personnel_id),
    check (quantity_to_dispense > 0)
);
-- create test data
insert into role (role, description) values
('Clinic Staff', 'The account for a clinic staff');

insert into login (username, password, role_id, last_login_date) values
('staff', '1234', 1, null);

insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number, login_id) values
('John', 'Michael', 'Doe', 'john.doe@example.com', '2015-05-15', 'Male', '123 Rizal Street, San Pedro, Laguna', '09191223222', 1);

insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number, login_id) values
('Jowan', 'Carl', 'Quirona', 'jowan123@rocs.edu.ph', '2005-03-14', 'Male', 'Langkaan', '09171234501', null),
('Allison', null, 'Reyes', 'allison23@rocs.edu.ph', '2004-08-22', 'Female', 'Silang', '09171234502', null),
('Ana', 'Ramos', 'Corpuz', 'anaramos@rocs.edu.ph', '1990-05-17', 'Female', 'Tagaytay', '09171234504', null),
('Carlos', 'Diaz', 'Gona', 'carlosyolo@rocs.edu.ph', '1985-11-02', 'Male', 'General Trias', '09171234505', null),
('Elena', null, 'Bautista', 'elenabautista@rocs.edu.ph', '1980-02-11', 'Female', 'Lalaan Dos', '09171234507', null),
('Roberto', null, 'Aquino', 'robertoaquino@rocs.edu.ph', '1978-09-25', 'Male', 'Manila', '09171234508', null),
('Grace', 'Mae', 'Villar', 'villarmae@rocs.edu.ph', '1988-04-03', 'Female', 'Alfonso, Tagaytay', '09171234509', null),
('Antonio', null, 'Ferrer', 'ferrerantonio@rocs.edu.ph', '1975-06-12', 'Male', 'Silang', '09171234510', null);

insert into department (department_name) values
('Junior High School'),
('Senior High School'),
('College');

insert into position (position_name, category, department_id) values
('Nurse', 'Clinic Staff', null),
('Doctor', 'Clinic Staff', null),
('College Teacher', 'RC Employee', 3),
('Adviser', 'RC Employee', null),
('Cashier', 'RC Employee', null),
('Maintenance Personnel', 'Community', null);

insert into personnel (person_id, employee_id) values
(4, 'EMP-001'),
(5, 'EMP-002'),
(6, 'EMP-003'),
(7, 'EMP-004'),
(8, 'EMP-005'),
(9, null);

insert into personnel_position (personnel_id, position_id) values
(1, 1),
(2, 2),
(3, 3),
(3, 4),
(4, 3),
(4, 4),
(5, 5),
(6, 6);

insert into section (code, name, department_id, year_level, semester, academic_year, adviser_id) values
('401', 'BSIT 401', 3, '2nd Year', '2nd Semester', '2026-2027', 3),
('601', 'BSIT 601', 3, '3th Year', '2nd Semester', '2026-2027', 4);

insert into student (person_id, student_number, section_id) values
(2, 'CT22-0166', 1),
(3, 'CT23-0987', 2);

insert into medical_record (person_id, medical_conditions, allergies) values
(2, 'Asthma', null),
(9, 'Hypertension', null);

insert into medicine_category (category_name) values
('Analgesic'),
('Antibiotic');

insert into medicine (medicine_name, category_id, unit) values
('Paracetamol 500mg', 1, 'tablet'),
('Amoxicillin 500mg', 2, 'capsule');

insert into inventory (medicine_id, personnel_id, batch_number, stock_quantity, expiration_date) values
(1, 1, 'B-001', 88, '2027-12-31'),
(2, 1, 'B-002', 29, '2027-06-30');

insert into consultation (medical_record_id, patient_id, personnel_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks) values
(1, 2, 2, 'Check-up', '2026-10-20 09:30:00', 'Cough, sore throat and fever', 'Bacterial throat infection', 'Antibiotic course and rest', 'Prescribed medicine', 'Return after 7 days'),
(2, 9, 1, 'Follow-up', '2026-10-22 14:00:00', 'Dizziness', 'Mild dehydration', 'Drink plenty of water and rest', 'Advised rest', 'No medicine needed');

insert into prescription (consultation_id, item_id, personnel_id, quantity_to_dispense, dose, frequency, duration, instructions) values
(1, 1, 2, 12, '500mg', 'Every 6 hours', '3 days', 'Take after meals'),
(1, 2, 2, 21, '500mg', '3 times a day', '7 days', 'Finish all capsules');

commit;