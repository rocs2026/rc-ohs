-- use ORCLPDB container
-- Needed when connected to the root container (CDB$ROOT). If you connect directly to the oraclepdb service, you can comment it out.
alter session set container = oraclepdb;

--drop user
drop user rcis;

--create user
create user rcis identified by Changeme0;

-- grant basic permissions
grant create session to rcis;
grant create table, create view, create sequence to rcis;
alter user rcis quota unlimited on users;

-- switch user
alter session set current_schema = rcis;


-- CA-6 Record Patient Consultation (Oracle)
-- Tables: person, student, staff, medical_record, consultation, medicine, inventory, prescription


create table person (
    person_id        number(10) generated always as identity
  , last_name        varchar2(50) not null
  , first_name       varchar2(50) not null
  , middle_name      varchar2(50)
  , email            varchar2(100)
  , date_of_birth    date
  , gender           varchar2(20)
  , address          varchar2(255)
  , contact_number   varchar2(20)
  , constraint pk_person primary key (person_id)
  , constraint uq_person_email unique (email)
);


create table student (
    student_id       number(10) generated always as identity
  , person_id        number(10) not null
  , student_number   varchar2(30) not null
  , academic_level   varchar2(50)
  , year_level       varchar2(30)
  , section_id       number(10)
  , constraint pk_student primary key (student_id)
  , constraint uq_student_person unique (person_id)
  , constraint uq_student_number unique (student_number)
  , constraint fk_student_person foreign key (person_id) references person (person_id)
);


create table staff (
    staff_id         number(10) generated always as identity
  , person_id        number(10) not null
  , staff_number     varchar2(30) not null
  , position         varchar2(50) not null
  , department       varchar2(100)
  , constraint pk_staff primary key (staff_id)
  , constraint uq_staff_person unique (person_id)
  , constraint uq_staff_number unique (staff_number)
  , constraint fk_staff_person foreign key (person_id) references person (person_id)
);


create table medical_record (
    medical_record_id number(10) generated always as identity
  , student_id       number(10) not null
  , medical_conditions varchar2(4000)
  , constraint pk_medical_record primary key (medical_record_id)
  , constraint uq_medical_record_student unique (student_id)
  , constraint fk_medical_record_student foreign key (student_id) references student (student_id)
);


create table consultation (
    consultation_id  number(10) generated always as identity
  , medical_record_id number(10) not null
  , staff_id         number(10) not null
  , record_type      varchar2(50)
  , consultation_date timestamp not null
  , complaint        varchar2(4000)
  , diagnosis        varchar2(4000) not null
  , treatment        varchar2(4000) not null
  , action_taken     varchar2(4000)
  , remarks          varchar2(4000)
  , constraint pk_consultation primary key (consultation_id)
  , constraint fk_consultation_record foreign key (medical_record_id) references medical_record (medical_record_id)
  , constraint fk_consultation_staff foreign key (staff_id) references staff (staff_id)
);


create table medicine (
    medicine_id      number(10) generated always as identity
  , medicine_name    varchar2(100) not null
  , category         varchar2(100)
  , unit             varchar2(30) not null
  , constraint pk_medicine primary key (medicine_id)
);


create table inventory (
    item_id          number(10) generated always as identity
  , medicine_id      number(10) not null
  , batch_number     varchar2(50)
  , stock_quantity   number(10) default 0 not null
  , expiration_date  date
  , constraint pk_inventory primary key (item_id)
  , constraint ck_inventory_stock check (stock_quantity >= 0)
  , constraint fk_inventory_medicine foreign key (medicine_id) references medicine (medicine_id)
);


create table prescription (
    prescription_id  number(10) generated always as identity
  , consultation_id  number(10) not null
  , item_id          number(10) not null
  , staff_id         number(10) not null
  , medication_quantity number(10) not null
  , dosage           varchar2(100) not null
  , frequency        varchar2(100) not null
  , duration         varchar2(100)
  , instructions     varchar2(4000)
  , constraint pk_prescription primary key (prescription_id)
  , constraint ck_prescription_qty check (medication_quantity > 0)
  , constraint fk_prescription_consultation foreign key (consultation_id) references consultation (consultation_id)
  , constraint fk_prescription_item foreign key (item_id) references inventory (item_id)
  , constraint fk_prescription_staff foreign key (staff_id) references staff (staff_id)
);


insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Dela Cruz', 'Juan', 'Santos', 'juan.delacruz@rocs.edu.ph', date '2005-03-14', 'Male', 'Quezon City', '09171234501');
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Reyes', 'Maria', 'Lopez', 'maria.reyes@rocs.edu.ph', date '2004-08-22', 'Female', 'Marikina City', '09171234502');
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Garcia', 'Pedro', 'Cruz', 'pedro.garcia@rocs.edu.ph', date '2005-01-09', 'Male', 'Pasig City', '09171234503');
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Mendoza', 'Ana', 'Ramos', 'ana.mendoza@rocs.edu.ph', date '1990-05-17', 'Female', 'Manila', '09171234504');
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Santos', 'Carlos', 'Diaz', 'carlos.santos@rocs.edu.ph', date '1985-11-02', 'Male', 'Makati City', '09171234505');
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Villanueva', 'Liza', 'Perez', 'liza.villanueva@rocs.edu.ph', date '1992-07-30', 'Female', 'Taguig City', '09171234506');

insert into student (person_id, student_number, academic_level, year_level, section_id)
values (1, 'CT22-0199', 'College', '3rd Year', 1);
insert into student (person_id, student_number, academic_level, year_level, section_id)
values (2, 'CT33-0777', 'College', '3rd Year', 1);
insert into student (person_id, student_number, academic_level, year_level, section_id)
values (3, 'CT27-0987', 'College', '2nd Year', 2);

insert into staff (person_id, staff_number, position, department)
values (4, 'STF-001', 'Nurse', 'Clinic');
insert into staff (person_id, staff_number, position, department)
values (5, 'STF-002', 'Doctor', 'Clinic');
insert into staff (person_id, staff_number, position, department)
values (6, 'STF-003', 'Nurse', 'Clinic');

insert into medical_record (student_id, medical_conditions) values (1, 'Asthma');
insert into medical_record (student_id, medical_conditions) values (2, 'None');
insert into medical_record (student_id, medical_conditions) values (3, 'Peanut allergy');

insert into medicine (medicine_name, category, unit) values ('Paracetamol 500mg', 'Analgesic', 'tablet');
insert into medicine (medicine_name, category, unit) values ('Amoxicillin 500mg', 'Antibiotic', 'capsule');
insert into medicine (medicine_name, category, unit) values ('Cetirizine 10mg', 'Antihistamine', 'tablet');

insert into inventory (medicine_id, batch_number, stock_quantity, expiration_date)
values (1, 'B-001', 100, date '2027-12-31');
insert into inventory (medicine_id, batch_number, stock_quantity, expiration_date)
values (2, 'B-002', 50, date '2027-06-30');
insert into inventory (medicine_id, batch_number, stock_quantity, expiration_date)
values (3, 'B-003', 80, date '2028-01-31');

insert into consultation (medical_record_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks)
values (1, 1, 'Check-up', timestamp '2026-10-20 09:30:00', 'Headache and fever', 'Viral fever', 'Rest and plenty of fluids', 'Gave medicine', 'Follow up if fever persists');
insert into consultation (medical_record_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks)
values (2, 2, 'Check-up', timestamp '2026-10-21 10:15:00', 'Cough and sore throat', 'Bacterial throat infection', 'Antibiotic course', 'Prescribed medicine', 'Return after 7 days');
insert into consultation (medical_record_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks)
values (3, 1, 'Emergency', timestamp '2026-10-22 14:00:00', 'Itchy skin and rashes', 'Allergic reaction', 'Antihistamine', 'Gave medicine', 'Monitor for 1 hour');

insert into prescription (consultation_id, item_id, staff_id, medication_quantity, dosage, frequency, duration, instructions)
values (1, 1, 1, 10, '500mg', 'Every 6 hours', '3 days', 'Take after meals');
insert into prescription (consultation_id, item_id, staff_id, medication_quantity, dosage, frequency, duration, instructions)
values (2, 2, 2, 21, '500mg', '3 times a day', '7 days', 'Finish all capsules');
insert into prescription (consultation_id, item_id, staff_id, medication_quantity, dosage, frequency, duration, instructions)
values (3, 3, 1, 5, '10mg', 'Once a day', '5 days', 'Take at night');

commit;