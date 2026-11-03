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


create database if not exists rcis;
use rcis;

create table person (
                        person_id int auto_increment primary key,
                        last_name varchar(50),
                        first_name varchar(50),
                        middle_name varchar(50),
                        email varchar(100),
                        date_of_birth date,
                        gender varchar(20),
                        address varchar(255),
                        contact_number varchar(20)
);

create table staff (
                       staff_id int auto_increment primary key,
                       person_id int not null unique,
                       staff_number varchar(20) not null unique,
                       position varchar(50),
                       department varchar(100),

                       constraint fk_staff_person
                           foreign key (person_id)
                               references person(person_id)
                               on update cascade
                               on delete restrict
);

create table medicine (
                          medicine_id int auto_increment primary key,
                          medicine_name varchar(100) not null,
                          category varchar(50),
                          unit varchar(30)
);

create table inventory (
                           item_id int auto_increment primary key,
                           medicine_id int not null,
                           staff_id int not null,
                           batch_number varchar(50),
                           stock_quantity int not null default 0,
                           minimum_stock_level int not null default 0,
                           expiration_date date,

                           constraint fk_inventory_medicine
                               foreign key (medicine_id)
                                   references medicine(medicine_id)
                                   on update cascade
                                   on delete restrict,

                           constraint fk_inventory_staff
                               foreign key (staff_id)
                                   references staff(staff_id)
                                   on update cascade
                                   on delete restrict,

                           constraint chk_stock_quantity
                               check (stock_quantity >= 0),

                           constraint chk_minimum_stock
                               check (minimum_stock_level >= 0)
);

insert into person (
    last_name, first_name, middle_name,
    email, date_of_birth, gender,
    address, contact_number
)
values
    ('Santos', 'Maria', 'Reyes',
     'maria@example.com', '2000-05-15', 'Female',
     'Dasmarinas, Cavite', '09123456789'),

    ('Dela Cruz', 'Juan', 'Garcia',
     'juan@example.com', '1998-08-20', 'Male',
     'Cavite, Philippines', '09987654321');

insert into staff (
    person_id, staff_number, position, department
)
values
    (1, 'STF-001', 'Pharmacist', 'Pharmacy'),
    (2, 'STF-002', 'Inventory Clerk', 'Inventory');

insert into medicine (
    medicine_name, category, unit
)
values
    ('Paracetamol', 'Pain Reliever', 'Tablet'),
    ('Amoxicillin', 'Antibiotic', 'Capsule'),
    ('Vitamin C', 'Vitamin', 'Bottle');

insert into inventory (
    medicine_id, staff_id, batch_number,
    stock_quantity, minimum_stock_level, expiration_date
)
values
    (1, 1, 'PAR-2026-001', 500, 100, '2027-12-31'),
    (2, 1, 'AMO-2026-001', 50, 100, '2027-06-30'),
    (3, 2, 'VIT-2026-001', 200, 50, '2028-01-31');

create view inventory_view as
select
    i.item_id,
    m.medicine_id,
    m.medicine_name,
    m.category,
    m.unit,
    i.batch_number,
    i.stock_quantity,
    i.minimum_stock_level,
    i.expiration_date,
    s.staff_id,
    s.staff_number,
    s.position,
    s.department,
    p.person_id,
    p.first_name,
    p.last_name
from inventory i
         join medicine m
              on i.medicine_id = m.medicine_id
         join staff s
              on i.staff_id = s.staff_id
         join person p
              on s.person_id = p.person_id;

select * from inventory_view;

select
    medicine_name,
    stock_quantity,
    minimum_stock_level
from inventory_view
where stock_quantity <= minimum_stock_level;

select
    medicine_name,
    batch_number,
    expiration_date
from inventory_view
where expiration_date < curdate();

select
    p.first_name,
    p.last_name,
    s.staff_number,
    s.position,
    m.medicine_name,
    i.stock_quantity
from inventory i
         join medicine m
              on i.medicine_id = m.medicine_id
         join staff s
              on i.staff_id = s.staff_id
         join person p
              on s.person_id = p.person_id;