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
    student_id      int primary key auto_increment,
    person_id       int not null unique,
    student_number  varchar(20) not null unique,
    academic_level  enum('Junior High School', 'Senior High School', 'College'),
    year_level      varchar(20),
    section_id      int,
    foreign key (person_id) references person (id)
);

create table staff (
    staff_id        int primary key auto_increment,
    person_id       int not null unique,
    staff_number    varchar(20),
    position        varchar(50),
    department      varchar(100),
    foreign key (person_id) references person (id)
);

create table medical_record (
    medical_record_id int primary key auto_increment,
    student_id        int not null unique,
    medical_conditions text,
    foreign key (student_id) references student(student_id)
);

create table referral (
    referral_id       int primary key auto_increment,
    student_id        int not null,
    medical_record_id int not null,
    staff_id          int not null,
    referred_to       varchar(50),
    referral_date     timestamp,
    reason            text,
    status            enum('Pending', 'Completed'),
    foreign key (student_id) references student (student_id),
    foreign key (medical_record_id) references medical_record (medical_record_id),
    foreign key (staff_id) references staff (staff_id)
);
-- create test data
insert into role (role, description)
values ('Clinic Staff', 'The account for a clinic staff');

insert into login (username, password, role_id, last_login_date)
values ('staff', '1234', 1, null);


insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number, login_id)
values
    ('John', 'Michael', 'Doe', 'john.doe@example.com', '2015-05-15', 'Male', '123 Rizal Street, San Pedro, Laguna', '09191223222', 1),
    ('Ken', 'Reyes', 'Hernandez', 'ken.hernandez@gmail.com', '2006-04-12', 'Male', '31 Emilio Aguinaldo Highway, Tagaytay, Cavite', '09853651475', null),
    ('Maria', 'Torres', 'Bautista', 'maria.bautista@example.com', '2014-05-19', 'Female', '9 Silang Crossing Road, Silang, Cavite', '09789563214', null);

insert into student (person_id, student_number, academic_level, year_level, section_id)
values
((select id from person where last_name = 'Hernandez'), 'CT24-8490', 'College', '3rd Year', 1),
((select id from person where last_name = 'Bautista'), 'CT24-6297', 'College', '3rd Year', 1);


insert into staff (person_id, staff_number, position, department)
values
((select id from person where last_name = 'Doe'), 'STF-001', 'Nurse', 'Clinic');

insert into medical_record (student_id, medical_conditions)
values
    (1, 'Frequent headaches'),
    (2, 'Head injury');

insert into referral (student_id, medical_record_id, staff_id, referred_to, referral_date, reason, status)
values
    (1, 1, 1, 'Evangelista Medical Specialty Hospital', now(), 'Complaints of frequent headaches','Pending'),
    (2, 2, 1, 'Tagaytay Medical Center', now(), 'Needs medical checkup after basketball injury ','Pending');




