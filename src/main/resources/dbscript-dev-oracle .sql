-- use oraclepdb container
-- alter session set container = oraclepdb;

-- drop user
drop user rcis;

-- create user
create user rcis identified by Changeme0;

-- grant basic permissions
grant create session to rcis;
grant create table, create view, create sequence to rcis;
alter user rcis quota unlimited on users;

-- switch to user
alter session set current_schema = rcis;

-- drop db objects (child tables first)
drop table gatepass;
drop table medical_record;
drop table student;
drop table staff;
drop table person;

-- create tables
create table person (
     person_id       number(19,0)  generated always as identity
    ,last_name       varchar2(50)
    ,first_name      varchar2(50)
    ,middle_name     varchar2(50)
    ,email           varchar2(100)
    ,date_of_birth   date
    ,gender          varchar2(20)
    ,address         varchar2(255)
    ,contact_number  varchar2(20)
);

create table staff (
     staff_id        number(19,0)  generated always as identity
    ,person_id       number(19,0)
    ,staff_number    varchar2(20)
    ,position        varchar2(50)
    ,department      varchar2(100)
);

create table student (
     student_id      number(19,0)  generated always as identity
    ,person_id       number(19,0)
    ,student_number  varchar2(20)
    ,academic_level  varchar2(30)
    ,year_level      varchar2(20)
    ,section_id      number(19,0)
);

create table medical_record (
     medical_record_id  number(19,0)  generated always as identity
    ,student_id         number(19,0)
    ,medical_conditions clob
);

create table gatepass (
     gatepass_id        number(19,0)  generated always as identity
    ,student_id         number(19,0)
    ,staff_id           number(19,0)
    ,medical_record_id  number(19,0)
    ,medical_reason     clob
    ,issued_at          timestamp default systimestamp
);

-- create primary keys
alter table person
    add constraint PERSON_PK primary key (person_id);

alter table staff
    add constraint STAFF_PK primary key (staff_id);

alter table student
    add constraint STUDENT_PK primary key (student_id);

alter table medical_record
    add constraint MEDICAL_RECORD_PK primary key (medical_record_id);

alter table gatepass
    add constraint GATEPASS_PK primary key (gatepass_id);

-- create unique constraints
alter table staff
    add constraint STAFF_NUMBER_UQ unique (staff_number);

alter table student
    add constraint STUDENT_NUMBER_UQ unique (student_number);

-- create foreign keys
alter table staff
    add constraint STAFF_PERSON_FK
        foreign key (person_id) references person (person_id);

alter table student
    add constraint STUDENT_PERSON_FK
        foreign key (person_id) references person (person_id);

alter table medical_record
    add constraint MEDICAL_RECORD_STUDENT_FK
        foreign key (student_id) references student (student_id);

alter table gatepass
    add constraint GATEPASS_STUDENT_FK
        foreign key (student_id) references student (student_id);

alter table gatepass
    add constraint GATEPASS_STAFF_FK
        foreign key (staff_id) references staff (staff_id);

alter table gatepass
    add constraint GATEPASS_MEDICAL_RECORD_FK
        foreign key (medical_record_id) references medical_record (medical_record_id);

-- create test data
-- staff person
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Doe', 'John', 'A', 'john.doe@rcis.edu', date '1985-03-15', 'Male', '123 Main St, San Pedro, Laguna', '09171234567');

-- student person
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Santos', 'Maria', 'L', 'maria.santos@rcis.edu', date '2008-07-21', 'Female', '45 Rizal Ave, San Pedro, Laguna', '09181234567');

insert into staff (person_id, staff_number, position, department)
values (1, 'STF-0001', 'Clinic Nurse', 'Clinic');

insert into student (person_id, student_number, academic_level, year_level, section_id)
values (2, 'STU-0001', 'Senior High School', 'Grade 11', 1);

insert into medical_record (student_id, medical_conditions)
values (1, 'Asthma');

insert into gatepass (student_id, staff_id, medical_record_id, medical_reason, issued_at)
values (1, 1, 1, 'Asthma attack, needs to go home for rest', systimestamp);

select * from person;
select * from staff;
select * from student;

commit;