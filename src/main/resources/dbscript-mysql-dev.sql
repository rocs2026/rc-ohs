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

drop table if exists inventory;
drop table if exists medicine;
drop table if exists person;
drop table if exists login;
drop table if exists role;

create table medicine (
    medicine_id int auto_increment primary key,
    medicine_name varchar(150) not null,
    category varchar(100),
    unit varchar(50)
);

create table inventory (
    inventory_id int auto_increment primary key,
    medicine_id int not null,
    batch_number varchar(100),
    stock_quantity int not null default 0,
    expiration_date date,
    constraint fk_clinic_inventory_medicine
        foreign key (medicine_id)
        references medicine(medicine_id)
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

insert into medicine (medicine_name, category, unit)
values
    ('Paracetamol', 'Analgesic', 'tablet'),
    ('Amoxicillin', 'Antibiotic', 'capsule'),
    ('Cetirizine', 'Antihistamine', 'tablet');

insert into inventory
    (medicine_id, batch_number, stock_quantity, expiration_date)
values
    (1, 'BATCH-001', 100, '2027-06-30'),
    (2, 'BATCH-002', 50, '2027-08-31'),
    (3, 'BATCH-003', 75, '2027-12-31');
	
insert into role (id, role, description)
values (1, 'Clinic Staff', 'The account for a clinic staff');

insert into login (id, username, password, role_id, last_login_date)
values (1, 'staff', '1234', 1, null);

insert into person (id, first_name, last_name, login_id)
values (3, 'John', 'Doe', 1);