-- drop user and database
drop user if exists 'rcis'@'localhost';
drop database if exists rcis;

-- create database
create database rcis;

-- create user
create user 'rcis'@'localhost' identified by 'Changeme0';

-- grant
grant select, insert, update, delete on rcis.* to 'rcis'@'localhost';
grant create, alter, references on rcis.* to 'rcis'@'localhost';
grant create view, show view on rcis.* to 'rcis'@'localhost';
grant create routine, execute on rcis.* to 'rcis'@'localhost';
flush privileges;

use rcis;

-- create tables
create table role (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    role varchar(128),
    description varchar(64)
);

create table person (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    first_name varchar(16),
    middle_name varchar(16),
    last_name varchar(64),
    email varchar(128),
    date_of_birth date,
    gender enum('Male', 'Female'),
    address varchar(255),
    contact_number varchar(16)
);

create table login (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    username varchar(16),
    password varchar(64),
    role_id binary(16),
    last_login_date timestamp null,
    person_id binary(16) not null,
    foreign key (role_id) references role (id),
    foreign key (person_id) references person (id)
);

create table department (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    department_name varchar(30) not null unique
);

create table position (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    position_name varchar(100) not null,
    category varchar(20) not null,
    department_id binary(16),
    foreign key (department_id) references department (id)
);

create table personnel (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    person_id binary(16) not null unique,
    employee_id varchar(30) unique,
    foreign key (person_id) references person (id)
);

create table personnel_position (
    personnel_id binary(16) not null,
    position_id binary(16) not null,
    department_id binary(16),
    primary key (personnel_id, position_id),
    foreign key (personnel_id) references personnel (id),
    foreign key (position_id) references position (id),
    foreign key (department_id) references department (id)
);

create table section (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    code varchar(30) not null,
    name varchar(100) not null,
    department_id binary(16) not null,
    year_level varchar(30) not null,
    period varchar(30),
    academic_year varchar(20) not null,
    adviser_id binary(16),
    foreign key (department_id) references department (id),
    foreign key (adviser_id) references personnel (id)
);

create table student (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    person_id binary(16) not null unique,
    student_number varchar(30) not null unique,
    foreign key (person_id) references person (id)
);

create table student_section (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    student_id binary(16) not null,
    section_id binary(16) not null,
    enrolled_date timestamp not null default current_timestamp,
    foreign key (student_id) references student (id),
    foreign key (section_id) references section (id)
);

create table consultation (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    patient_id binary(16) not null,
    staff_id binary(16) not null,
    record_type varchar(50) not null,
    consultation_date datetime not null,
    complaint text,
    diagnosis text not null,
    treatment text not null,
    action_taken text,
    remarks text,
    foreign key (patient_id) references person (id),
    foreign key (staff_id) references personnel (id),
    check (record_type in ('Check-up', 'Follow-up'))
);

create table medical_record (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    patient_id binary(16) not null,
    consultation_id binary(16),
    medical_conditions text,
    allergies text,
    foreign key (patient_id) references person (id),
    foreign key (consultation_id) references consultation (id)
);

create table medicine_category (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    category_name varchar(100) not null unique
);

create table medicine (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    medicine_name varchar(100) not null,
    category_id binary(16),
    unit varchar(30) not null,
    foreign key (category_id) references medicine_category (id)
);

create table medicine_stock (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    medicine_id binary(16) not null,
    personnel_id binary(16) not null,
    batch_number varchar(50),
    quantity_received int not null,
    expiration_date date,
    status varchar(20) not null,
    foreign key (medicine_id) references medicine (id),
    foreign key (personnel_id) references personnel (id),
    check (quantity_received >= 0)
);

create table prescription (
    id binary(16) primary key default (uuid_to_bin(uuid(), 1)),
    consultation_id binary(16) not null,
    medicine_stock_id binary(16) not null,
    personnel_id binary(16) not null,
    quantity_to_dispense int not null,
    dose varchar(100) not null,
    frequency varchar(100) not null,
    instructions text,
    foreign key (consultation_id) references consultation (id),
    foreign key (medicine_stock_id) references medicine_stock (id),
    foreign key (personnel_id) references personnel (id),
    check (quantity_to_dispense > 0)
);

-- create test data

insert into role (role, description) values
('Clinic Staff', 'The account for a clinic staff');

insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number) values
('John', 'Michael', 'Doe', 'john.doe@example.com', '2015-05-15', 'Male', '123 Rizal Street, San Pedro, Laguna', '09191223222');

insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number) values
('Jowan', 'Carl', 'Quirona', 'jowan123@rocs.edu.ph', '2005-03-14', 'Male', 'Langkaan', '09171234501'),
('Allison', null, 'Reyes', 'allison23@rocs.edu.ph', '2004-08-22', 'Female', 'Silang', '09171234502'),
('Ana', 'Ramos', 'Corpuz', 'anaramos@rocs.edu.ph', '1990-05-17', 'Female', 'Tagaytay', '09171234504'),
('Carlos', 'Diaz', 'Gona', 'carlosyolo@rocs.edu.ph', '1985-11-02', 'Male', 'General Trias', '09171234505'),
('Elena', null, 'Bautista', 'elenabautista@rocs.edu.ph', '1980-02-11', 'Female', 'Lalaan Dos', '09171234507'),
('Roberto', null, 'Aquino', 'robertoaquino@rocs.edu.ph', '1978-09-25', 'Male', 'Manila', '09171234508'),
('Grace', 'Mae', 'Villar', 'villarmae@rocs.edu.ph', '1988-04-03', 'Female', 'Alfonso, Tagaytay', '09171234509'),
('Antonio', null, 'Ferrer', 'ferrerantonio@rocs.edu.ph', '1975-06-12', 'Male', 'Silang', '09171234510');

insert into login (username, password, role_id, last_login_date, person_id) values
('staff', '1234', (select id from role where role = 'Clinic Staff'), null, (select id from person where email = 'john.doe@example.com'));

insert into department (department_name) values
('Junior High School'),
('Senior High School'),
('College');

insert into position (position_name, category, department_id) values
('Nurse', 'Clinic Staff', null),
('Doctor', 'Clinic Staff', null),
('College Teacher', 'RC Employee', (select id from department where department_name = 'College')),
('Adviser', 'RC Employee', null),
('Cashier', 'RC Employee', null),
('Maintenance Personnel', 'Community', null);

insert into personnel (person_id, employee_id) values
((select id from person where email = 'anaramos@rocs.edu.ph'), 'EMP-001'),
((select id from person where email = 'carlosyolo@rocs.edu.ph'), 'EMP-002'),
((select id from person where email = 'elenabautista@rocs.edu.ph'), 'EMP-003'),
((select id from person where email = 'robertoaquino@rocs.edu.ph'), 'EMP-004'),
((select id from person where email = 'villarmae@rocs.edu.ph'), 'EMP-005'),
((select id from person where email = 'ferrerantonio@rocs.edu.ph'), null);

insert into personnel_position (personnel_id, position_id, department_id) values
((select id from personnel where employee_id = 'EMP-001'), (select id from position where position_name = 'Nurse'), null),
((select id from personnel where employee_id = 'EMP-002'), (select id from position where position_name = 'Doctor'), null),
((select id from personnel where employee_id = 'EMP-003'), (select id from position where position_name = 'College Teacher'), (select id from department where department_name = 'College')),
((select id from personnel where employee_id = 'EMP-003'), (select id from position where position_name = 'Adviser'), (select id from department where department_name = 'College')),
((select id from personnel where employee_id = 'EMP-004'), (select id from position where position_name = 'College Teacher'), (select id from department where department_name = 'College')),
((select id from personnel where employee_id = 'EMP-004'), (select id from position where position_name = 'Adviser'), (select id from department where department_name = 'College')),
((select id from personnel where employee_id = 'EMP-005'), (select id from position where position_name = 'Cashier'), null),
((select id from personnel where person_id = (select id from person where email = 'ferrerantonio@rocs.edu.ph')), (select id from position where position_name = 'Maintenance Personnel'), null);

insert into section (code, name, department_id, year_level, period, academic_year, adviser_id) values
('401', 'BSIT 401', (select id from department where department_name = 'College'), '2nd Year', '2nd Semester', '2026-2027', (select id from personnel where employee_id = 'EMP-003')),
('601', 'BSIT 601', (select id from department where department_name = 'College'), '3rd Year', '2nd Semester', '2026-2027', (select id from personnel where employee_id = 'EMP-004'));

insert into student (person_id, student_number) values
((select id from person where email = 'jowan123@rocs.edu.ph'), 'CT22-0166'),
((select id from person where email = 'allison23@rocs.edu.ph'), 'CT23-0987');

insert into student_section (student_id, section_id) values
((select id from student where student_number = 'CT22-0166'), (select id from section where code = '401')),
((select id from student where student_number = 'CT23-0987'), (select id from section where code = '601'));

insert into consultation (patient_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks) values
((select id from person where email = 'jowan123@rocs.edu.ph'), (select id from personnel where employee_id = 'EMP-002'), 'Check-up', '2026-10-20 09:30:00', 'Cough, sore throat and fever', 'Bacterial throat infection', 'Antibiotic course and rest', 'Prescribed medicine', 'Return after 7 days'),
((select id from person where email = 'ferrerantonio@rocs.edu.ph'), (select id from personnel where employee_id = 'EMP-001'), 'Follow-up', '2026-10-22 14:00:00', 'Dizziness', 'Mild dehydration', 'Drink plenty of water and rest', 'Advised rest', 'No medicine needed');

insert into medical_record (patient_id, consultation_id, medical_conditions, allergies) values
((select id from person where email = 'jowan123@rocs.edu.ph'), (select id from consultation where patient_id = (select id from person where email = 'jowan123@rocs.edu.ph') and consultation_date = '2026-10-20 09:30:00'), 'Asthma', null),
((select id from person where email = 'ferrerantonio@rocs.edu.ph'), (select id from consultation where patient_id = (select id from person where email = 'ferrerantonio@rocs.edu.ph') and consultation_date = '2026-10-22 14:00:00'), 'Hypertension', null);

insert into medicine_category (category_name) values
('Analgesic'),
('Antibiotic');

insert into medicine (medicine_name, category_id, unit) values
('Paracetamol 500mg', (select id from medicine_category where category_name = 'Analgesic'), 'tablet'),
('Amoxicillin 500mg', (select id from medicine_category where category_name = 'Antibiotic'), 'capsule');

insert into medicine_stock (medicine_id, personnel_id, batch_number, quantity_received, expiration_date, status) values
((select id from medicine where medicine_name = 'Paracetamol 500mg'), (select id from personnel where employee_id = 'EMP-001'), 'B-001', 50, '2027-12-31', 'Enough'),
((select id from medicine where medicine_name = 'Amoxicillin 500mg'), (select id from personnel where employee_id = 'EMP-001'), 'B-002', 50, '2027-06-30', 'Enough');

insert into prescription (consultation_id, medicine_stock_id, personnel_id, quantity_to_dispense, dose, frequency, instructions) values
((select id from consultation where patient_id = (select id from person where email = 'jowan123@rocs.edu.ph') and consultation_date = '2026-10-20 09:30:00'), (select id from medicine_stock where batch_number = 'B-001'), (select id from personnel where employee_id = 'EMP-002'), 1, '500mg', 'Every 6 hours', 'Take after meals'),
((select id from consultation where patient_id = (select id from person where email = 'jowan123@rocs.edu.ph') and consultation_date = '2026-10-20 09:30:00'), (select id from medicine_stock where batch_number = 'B-002'), (select id from personnel where employee_id = 'EMP-002'), 2, '500mg', '3 times a day', 'Finish all capsules');

-- create join, function, view, procedure
delimiter $$
create function fn_remaining_stock (p_medicine_stock_id binary(16))
returns int
reads sql data
begin
    declare v_remaining int;

    select ms.quantity_received - coalesce(sum(p.quantity_to_dispense), 0)
      into v_remaining
      from medicine_stock ms
      left join prescription p on p.medicine_stock_id = ms.id
     where ms.id = p_medicine_stock_id
     group by ms.id, ms.quantity_received;

    return v_remaining;
end$$
delimiter ;

select fn_remaining_stock((select id from medicine_stock where batch_number = 'B-001'));
select fn_remaining_stock((select id from medicine_stock where batch_number = 'B-002'));

create view inventory_view as
select ms.id as medicine_stock_id,
       m.id as medicine_id,
       m.medicine_name,
       mc.category_name,
       m.unit,
       ms.batch_number,
       ms.quantity_received,
       fn_remaining_stock(ms.id) as remaining_quantity,
       ms.expiration_date,
       ms.status
  from medicine_stock ms
  join medicine m on m.id = ms.medicine_id
  left join medicine_category mc on mc.id = m.category_id;

select * from inventory_view;

commit;