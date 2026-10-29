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
drop table inventory;
drop table medicine;
drop table staff;
drop table person;

-- create tables
create table person (
     person_id       number(19,0)  generated always as identity
    ,last_name       varchar2(50)  not null
    ,first_name      varchar2(50)  not null
    ,middle_name     varchar2(50)
    ,email           varchar2(100)
    ,date_of_birth   date
    ,gender          varchar2(20)
    ,address         varchar2(255)
    ,contact_number  varchar2(20)
);

create table staff (
     staff_id        number(19,0)  generated always as identity
    ,person_id       number(19,0)  not null
    ,staff_number    varchar2(20)  not null
    ,position        varchar2(50)  not null
    ,department      varchar2(100)
);

create table medicine (
     medicine_id     number(19,0)  generated always as identity
    ,medicine_name   varchar2(100) not null
    ,category        varchar2(50)
    ,unit            varchar2(30)
);

create table inventory (
     item_id          number(19,0)  generated always as identity
    ,medicine_id      number(19,0)  not null
    ,staff_id         number(19,0)  not null
    ,batch_number     varchar2(50)  not null
    ,stock_quantity   number(10,0)  not null
    ,expiration_date  date          not null
);

-- create primary keys
alter table person
    add constraint PERSON_PK primary key (person_id);

alter table staff
    add constraint STAFF_PK primary key (staff_id);

alter table medicine
    add constraint MEDICINE_PK primary key (medicine_id);

alter table inventory
    add constraint INVENTORY_PK primary key (item_id);

-- create unique constraints
alter table person
    add constraint PERSON_EMAIL_UQ unique (email);

alter table staff
    add constraint STAFF_PERSON_UQ unique (person_id);

alter table staff
    add constraint STAFF_NUMBER_UQ unique (staff_number);

alter table inventory
    add constraint INVENTORY_BATCH_UQ unique (medicine_id, batch_number);

-- create check constraints
alter table inventory
    add constraint INVENTORY_STOCK_CK check (stock_quantity >= 0);

-- create foreign keys
alter table staff
    add constraint STAFF_PERSON_FK
        foreign key (person_id) references person (person_id);

alter table inventory
    add constraint INVENTORY_MEDICINE_FK
        foreign key (medicine_id) references medicine (medicine_id);

alter table inventory
    add constraint INVENTORY_STAFF_FK
        foreign key (staff_id) references staff (staff_id);

-- create test data
-- staff person
insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number)
values ('Ambunan', 'Zymonne', 'Rubiano', 'ambunanz@gmail.com', date '2005-09-20', 'Male', 'Lumil-Silang-Cavite', '09397756953');

insert into staff (person_id, staff_number, position, department)
values (1, 'N-01', 'Nurse', 'Clinic');

insert into medicine (medicine_name, category, unit)
values ('Paracetamol 500mg', 'Analgesic', 'tablet');

insert into inventory (medicine_id, staff_id, batch_number, stock_quantity, expiration_date)
values (1, 1, 'PARACETAMOL-10-28', 200, date '2028-03-31');

select * from person;
select * from staff;
select * from medicine;
select * from inventory;

show con_name;

commit;