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
    id          int primary key auto_increment,
    first_name  varchar(16),
    last_name   varchar(64),
    login_id    int,
    foreign key (login_id) references login (id)
);

-- create test data
insert into role (id, role, description)
values (1, 'Clinic Staff', 'The account for a clinic staff');

insert into login (id, username, password, role_id, last_login_date)
values (1, 'staff', '1234', 1, null);

insert into person (id, first_name, last_name, login_id)
values (3, 'John', 'Doe', 1);