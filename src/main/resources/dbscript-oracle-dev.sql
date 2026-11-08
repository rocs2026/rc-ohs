-- use oraclepdb container
-- alter session set container = oraclepdb;

-- drop user
drop user rcis;

-- create user
create user rcis identified by Changeme0;

-- grant basic permissions
grant create session to rcis;
grant create table, create view, create procedure to rcis;
alter user rcis quota unlimited on users;

-- switch to user
alter session set current_schema = rcis;

-- create tables
create table role (
     id raw(16) default sys_guid() not null
    ,role            varchar2(128)
    ,description     varchar2(64)
);

create table login (
     id raw(16) default sys_guid() not null
    ,username         varchar2(16)
    ,password         varchar2(64)
    ,role_id          raw(16)
    ,last_login_date  timestamp null
    ,person_id        raw(16) not null
);

create table person (
     id raw(16) default sys_guid() not null
    ,first_name       varchar2(16)
    ,middle_name      varchar2(16)
    ,last_name        varchar2(64)
    ,email            varchar2(128)
    ,date_of_birth    date
    ,gender           varchar2(16)
    ,address          varchar2(255)
    ,contact_number   varchar2(16)
);

create table department (
     id raw(16) default sys_guid() not null
    ,department_name      varchar2(30) not null
);

create table position (
     id raw(16) default sys_guid() not null
    ,position_name        varchar2(100) not null
    ,category             varchar2(20) not null
    ,department_id        raw(16)
);

create table personnel (
     id raw(16) default sys_guid() not null
    ,person_id            raw(16) not null
    ,employee_id          varchar2(30)
);

create table personnel_position (
     personnel_id         raw(16) not null
    ,position_id          raw(16) not null
    ,department_id        raw(16)
);

create table section (
     id raw(16) default sys_guid() not null
    ,code                 varchar2(30) not null
    ,name                 varchar2(100) not null
    ,department_id        raw(16) not null
    ,year_level           varchar2(30) not null
    ,period             varchar2(30)
    ,academic_year        varchar2(20) not null
    ,adviser_id           raw(16)
);

create table student (
     id raw(16) default sys_guid() not null
    ,person_id            raw(16) not null
    ,student_number       varchar2(30) not null
);

create table student_section (
     id raw(16) default sys_guid() not null
    ,student_id           raw(16) not null
    ,section_id           raw(16) not null
    ,enrolled_date        timestamp default systimestamp not null
);

create table consultation (
     id raw(16) default sys_guid() not null
    ,patient_id           raw(16) not null
    ,staff_id             raw(16) not null
    ,record_type          varchar2(50) not null
    ,consultation_date    timestamp not null
    ,complaint            varchar2(4000)
    ,diagnosis            varchar2(4000) not null
    ,treatment            varchar2(4000) not null
    ,action_taken         varchar2(4000)
    ,remarks              varchar2(4000)
);

create table medical_record (
     id raw(16) default sys_guid() not null
    ,patient_id           raw(16) not null
    ,consultation_id      raw(16)
    ,medical_conditions   varchar2(4000)
    ,allergies            varchar2(4000)
);

create table medicine_category (
     id raw(16) default sys_guid() not null
    ,category_name        varchar2(100) not null
);

create table medicine (
     id raw(16) default sys_guid() not null
    ,medicine_name        varchar2(100) not null
    ,category_id          raw(16)
    ,unit                 varchar2(30) not null
);

create table medicine_stock (
     id raw(16) default sys_guid() not null
    ,medicine_id          raw(16) not null
    ,personnel_id         raw(16) not null
    ,batch_number         varchar2(50)
    ,quantity_received    number(10,0) not null
    ,expiration_date      date
    ,status               varchar2(20) not null
);

create table prescription (
     id raw(16) default sys_guid() not null
    ,consultation_id      raw(16) not null
    ,medicine_stock_id    raw(16) not null
    ,personnel_id         raw(16) not null
    ,quantity_to_dispense number(19,0) not null
    ,dose                 varchar2(100) not null
    ,frequency            varchar2(100) not null
    ,instructions         varchar2(4000)
);
-- create table constraints
alter table role
    add constraint ROLE_PK
        primary key (id);

alter table login
    add constraint LOGIN_PK
        primary key (id);

alter table person
    add constraint PERSON_PK
        primary key (id);

alter table person
    add constraint PERSON_GENDER_CK
        check (gender in ('Male', 'Female'));

alter table login
    add constraint LOGIN_ROLE_FK
        foreign key (role_id)
            references role (id);

alter table login
    add constraint LOGIN_PERSON_FK
        foreign key (person_id)
            references person (id);

alter table department
    add constraint DEPARTMENT_PK
        primary key (id);

alter table department
    add constraint DEPARTMENT_NAME_UQ
        unique (department_name);

alter table position
    add constraint POSITION_PK
        primary key (id);

alter table position
    add constraint POSITION_DEPARTMENT_FK
        foreign key (department_id)
            references department (id);

alter table personnel
    add constraint PERSONNEL_PK
        primary key (id);

alter table personnel
    add constraint PERSONNEL_PERSON_UQ
        unique (person_id);

alter table personnel
    add constraint PERSONNEL_EMPLOYEE_UQ
        unique (employee_id);

alter table personnel
    add constraint PERSONNEL_PERSON_FK
        foreign key (person_id)
            references person (id);

alter table personnel_position
    add constraint PERSONNEL_POSITION_PK
        primary key (personnel_id, position_id);

alter table personnel_position
    add constraint PERS_POS_PERSONNEL_FK
        foreign key (personnel_id)
            references personnel (id);

alter table personnel_position
    add constraint PERS_POS_POSITION_FK
        foreign key (position_id)
            references position (id);

alter table personnel_position
    add constraint PERS_POS_DEPARTMENT_FK
        foreign key (department_id)
            references department (id);

alter table section
    add constraint SECTION_PK
        primary key (id);

alter table section
    add constraint SECTION_DEPARTMENT_FK
        foreign key (department_id)
            references department (id);

alter table section
    add constraint SECTION_PERSONNEL_FK
        foreign key (adviser_id)
            references personnel (id);

alter table student
    add constraint STUDENT_PK
        primary key (id);

alter table student
    add constraint STUDENT_PERSON_UQ
        unique (person_id);

alter table student
    add constraint STUDENT_NUMBER_UQ
        unique (student_number);

alter table student
    add constraint STUDENT_PERSON_FK
        foreign key (person_id)
            references person (id);

alter table student_section
    add constraint STUDENT_SECTION_PK
        primary key (id);

alter table student_section
    add constraint STUDENT_SECTION_STUDENT_FK
        foreign key (student_id)
            references student (id);

alter table student_section
    add constraint STUDENT_SECTION_SECTION_FK
        foreign key (section_id)
            references section (id);

alter table consultation
    add constraint CONSULTATION_PK
        primary key (id);

alter table consultation
    add constraint CONSULTATION_PERSON_FK
        foreign key (patient_id)
            references person (id);

alter table consultation
    add constraint CONSULTATION_PERSONNEL_FK
        foreign key (staff_id)
            references personnel (id);

alter table consultation
    add constraint CONSULTATION_TYPE_CK
        check (record_type in ('Check-up', 'Follow-up'));

alter table medical_record
    add constraint MEDICAL_RECORD_PK
        primary key (id);

alter table medical_record
    add constraint MEDICAL_RECORD_PERSON_FK
        foreign key (patient_id)
            references person (id);

alter table medical_record
    add constraint MEDICAL_RECORD_CONSULTATION_FK
        foreign key (consultation_id)
            references consultation (id);

alter table medicine_category
    add constraint MEDICINE_CATEGORY_PK
        primary key (id);

alter table medicine_category
    add constraint MEDICINE_CATEGORY_NAME_UQ
        unique (category_name);

alter table medicine
    add constraint MEDICINE_PK
        primary key (id);

alter table medicine
    add constraint MEDICINE_MEDICINE_CATEGORY_FK
        foreign key (category_id)
            references medicine_category (id);

alter table medicine_stock
    add constraint MEDICINE_STOCK_PK
        primary key (id);

alter table medicine_stock
    add constraint MEDICINE_STOCK_MEDICINE_FK
        foreign key (medicine_id)
            references medicine (id);

alter table medicine_stock
    add constraint MEDICINE_STOCK_PERSONNEL_FK
        foreign key (personnel_id)
            references personnel (id);

alter table medicine_stock
    add constraint MEDICINE_STOCK_QTY_CK
        check (quantity_received >= 0);

alter table prescription
    add constraint PRESCRIPTION_PK
        primary key (id);

alter table prescription
    add constraint PRESCRIPTION_CONSULTATION_FK
        foreign key (consultation_id)
            references consultation (id);

alter table prescription
    add constraint PRESCRIPTION_STOCK_FK
        foreign key (medicine_stock_id)
            references medicine_stock (id);

alter table prescription
    add constraint PRESCRIPTION_PERSONNEL_FK
        foreign key (personnel_id)
            references personnel (id);

alter table prescription
    add constraint PRESCRIPTION_QTY_CK
        check (quantity_to_dispense > 0);

-- create test data

insert into role (role, description)
values ('Clinic Staff', 'The account for a clinic staff');

insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('John', 'Michael', 'Doe', 'john.doe@example.com', date '2015-05-15', 'Male', '123 Rizal Street, San Pedro, Laguna', '09191223222');

insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Jowan', 'Carl', 'Quirona', 'jowan123@rocs.edu.ph', date '2005-03-14', 'Male', 'Langkaan', '09171234501');
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Allison', null, 'Reyes', 'allison23@rocs.edu.ph', date '2004-08-22', 'Female', 'Silang', '09171234502');
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Ana', 'Ramos', 'Corpuz', 'anaramos@rocs.edu.ph', date '1990-05-17', 'Female', 'Tagaytay', '09171234504');
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Carlos', 'Diaz', 'Gona', 'carlosyolo@rocs.edu.ph', date '1985-11-02', 'Male', 'General Trias', '09171234505');
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Elena', null, 'Bautista', 'elenabautista@rocs.edu.ph', date '1980-02-11', 'Female', 'Lalaan Dos', '09171234507');
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Roberto', null, 'Aquino', 'robertoaquino@rocs.edu.ph', date '1978-09-25', 'Male', 'Manila', '09171234508');
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Grace', 'Mae', 'Villar', 'villarmae@rocs.edu.ph', date '1988-04-03', 'Female', 'Alfonso, Tagaytay', '09171234509');
insert into person (first_name, middle_name, last_name, email, date_of_birth, gender, address, contact_number)
values ('Antonio', null, 'Ferrer', 'ferrerantonio@rocs.edu.ph', date '1975-06-12', 'Male', 'Silang', '09171234510');

insert into login (username, password, role_id, last_login_date, person_id)
values ('staff', '1234', (select id from role where role = 'Clinic Staff'), null, (select id from person where email = 'john.doe@example.com'));

insert into department (department_name)
values ('Junior High School');
insert into department (department_name)
values ('Senior High School');
insert into department (department_name)
values ('College');

insert into position (position_name, category, department_id)
values ('Nurse', 'Clinic Staff', null);
insert into position (position_name, category, department_id)
values ('Doctor', 'Clinic Staff', null);
insert into position (position_name, category, department_id)
values ('College Teacher', 'RC Employee', (select id from department where department_name = 'College'));
insert into position (position_name, category, department_id)
values ('Adviser', 'RC Employee', null);
insert into position (position_name, category, department_id)
values ('Cashier', 'RC Employee', null);
insert into position (position_name, category, department_id)
values ('Maintenance Personnel', 'Community', null);

insert into personnel (person_id, employee_id)
values ((select id from person where email = 'anaramos@rocs.edu.ph'), 'EMP-001');
insert into personnel (person_id, employee_id)
values ((select id from person where email = 'carlosyolo@rocs.edu.ph'), 'EMP-002');
insert into personnel (person_id, employee_id)
values ((select id from person where email = 'elenabautista@rocs.edu.ph'), 'EMP-003');
insert into personnel (person_id, employee_id)
values ((select id from person where email = 'robertoaquino@rocs.edu.ph'), 'EMP-004');
insert into personnel (person_id, employee_id)
values ((select id from person where email = 'villarmae@rocs.edu.ph'), 'EMP-005');
insert into personnel (person_id, employee_id)
values ((select id from person where email = 'ferrerantonio@rocs.edu.ph'), null);

insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where employee_id = 'EMP-001'), (select id from position where position_name = 'Nurse'), null);
insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where employee_id = 'EMP-002'), (select id from position where position_name = 'Doctor'), null);
insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where employee_id = 'EMP-003'), (select id from position where position_name = 'College Teacher'), (select id from department where department_name = 'College'));
insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where employee_id = 'EMP-003'), (select id from position where position_name = 'Adviser'), (select id from department where department_name = 'College'));
insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where employee_id = 'EMP-004'), (select id from position where position_name = 'College Teacher'), (select id from department where department_name = 'College'));
insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where employee_id = 'EMP-004'), (select id from position where position_name = 'Adviser'), (select id from department where department_name = 'College'));
insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where employee_id = 'EMP-005'), (select id from position where position_name = 'Cashier'), null);
insert into personnel_position (personnel_id, position_id, department_id)
values ((select id from personnel where person_id = (select id from person where email = 'ferrerantonio@rocs.edu.ph')), (select id from position where position_name = 'Maintenance Personnel'), null);

insert into section (code, name, department_id, year_level, period, academic_year, adviser_id)
values ('401', 'BSIT 401', (select id from department where department_name = 'College'), '2nd Year', '2nd Semester', '2026-2027', (select id from personnel where employee_id = 'EMP-003'));
insert into section (code, name, department_id, year_level, period, academic_year, adviser_id)
values ('601', 'BSIT 601', (select id from department where department_name = 'College'), '3rd Year', '2nd Semester', '2026-2027', (select id from personnel where employee_id = 'EMP-004'));

insert into student (person_id, student_number)
values ((select id from person where email = 'jowan123@rocs.edu.ph'), 'CT22-0166');
insert into student (person_id, student_number)
values ((select id from person where email = 'allison23@rocs.edu.ph'), 'CT23-0987');

insert into student_section (student_id, section_id)
values ((select id from student where student_number = 'CT22-0166'), (select id from section where code = '401'));
insert into student_section (student_id, section_id)
values ((select id from student where student_number = 'CT23-0987'), (select id from section where code = '601'));

insert into consultation (patient_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks)
values ((select id from person where email = 'jowan123@rocs.edu.ph'), (select id from personnel where employee_id = 'EMP-002'), 'Check-up', timestamp '2026-10-20 09:30:00', 'Cough, sore throat and fever', 'Bacterial throat infection', 'Antibiotic course and rest', 'Prescribed medicine', 'Return after 7 days');
insert into consultation (patient_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks)
values ((select id from person where email = 'ferrerantonio@rocs.edu.ph'), (select id from personnel where employee_id = 'EMP-001'), 'Follow-up', timestamp '2026-10-22 14:00:00', 'Dizziness', 'Mild dehydration', 'Drink plenty of water and rest', 'Advised rest', 'No medicine needed');

insert into medical_record (patient_id, consultation_id, medical_conditions, allergies)
values ((select id from person where email = 'jowan123@rocs.edu.ph'), (select id from consultation where patient_id = (select id from person where email = 'jowan123@rocs.edu.ph') and consultation_date = timestamp '2026-10-20 09:30:00'), 'Asthma', null);
insert into medical_record (patient_id, consultation_id, medical_conditions, allergies)
values ((select id from person where email = 'ferrerantonio@rocs.edu.ph'), (select id from consultation where patient_id = (select id from person where email = 'ferrerantonio@rocs.edu.ph') and consultation_date = timestamp '2026-10-22 14:00:00'), 'Hypertension', null);

insert into medicine_category (category_name)
values ('Analgesic');
insert into medicine_category (category_name)
values ('Antibiotic');

insert into medicine (medicine_name, category_id, unit)
values ('Paracetamol 500mg', (select id from medicine_category where category_name = 'Analgesic'), 'tablet');
insert into medicine (medicine_name, category_id, unit)
values ('Amoxicillin 500mg', (select id from medicine_category where category_name = 'Antibiotic'), 'capsule');

insert into medicine_stock (medicine_id, personnel_id, batch_number, quantity_received, expiration_date, status)
values ((select id from medicine where medicine_name = 'Paracetamol 500mg'), (select id from personnel where employee_id = 'EMP-001'), 'B-001', 50, date '2027-12-31', 'Enough');
insert into medicine_stock (medicine_id, personnel_id, batch_number, quantity_received, expiration_date, status)
values ((select id from medicine where medicine_name = 'Amoxicillin 500mg'), (select id from personnel where employee_id = 'EMP-001'), 'B-002', 50, date '2027-06-30', 'Enough');

insert into prescription (consultation_id, medicine_stock_id, personnel_id, quantity_to_dispense, dose, frequency, instructions)
values ((select id from consultation where patient_id = (select id from person where email = 'jowan123@rocs.edu.ph') and consultation_date = timestamp '2026-10-20 09:30:00'), (select id from medicine_stock where batch_number = 'B-001'), (select id from personnel where employee_id = 'EMP-002'), 1, '500mg', 'Every 6 hours', 'Take after meals');
insert into prescription (consultation_id, medicine_stock_id, personnel_id, quantity_to_dispense, dose, frequency, instructions)
values ((select id from consultation where patient_id = (select id from person where email = 'jowan123@rocs.edu.ph') and consultation_date = timestamp '2026-10-20 09:30:00'), (select id from medicine_stock where batch_number = 'B-002'), (select id from personnel where employee_id = 'EMP-002'), 2, '500mg', '3 times a day', 'Finish all capsules');

-- create join, view, index, procedure, function
create function fn_remaining_stock (p_medicine_stock_id in raw)
return number
is
    v_remaining number;
begin
    select ms.quantity_received - coalesce(sum(p.quantity_to_dispense), 0)
      into v_remaining
      from medicine_stock ms
      left join prescription p on p.medicine_stock_id = ms.id
     where ms.id = p_medicine_stock_id
     group by ms.id, ms.quantity_received;

    return v_remaining;
end;
/

select fn_remaining_stock((select id from medicine_stock where batch_number = 'B-001')) as remaining from dual;
select fn_remaining_stock((select id from medicine_stock where batch_number = 'B-002')) as remaining from dual;

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