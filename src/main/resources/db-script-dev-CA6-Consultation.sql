Drop database if exists rcis;
Create database if not exists rcis;

USE rcis;

create user if not exists 'rcis'@'localhost' identified by '1234';

Grant create on rcis.* to 'rcis'@'localhost';

Grant select, insert, update, delete on rcis.* to 'rcis'@'localhost';

Flush privileges;

Drop table if exists prescription;
Drop table if exists consultation;
Drop table if exists inventory;
Drop table if exists medicine;
Drop table if exists medical_record;
Drop table if exists staff;
Drop table if exists student;
Drop table if exists person;

create table if not exists person (
    person_id int auto_increment primary key,
    last_name varchar(50) not null,
    first_name varchar(50) not null,
    middle_name varchar(50),
    email varchar(100) unique,
    date_of_birth date,
    gender varchar(20),
    address varchar(255),
    contact_number varchar(20)
);

create table if not exists student (
    student_id int auto_increment primary key,
    person_id int not null unique,
    student_number varchar(30) not null unique,
    academic_level varchar(50),
    year_level varchar(30),
    section_id int,
    foreign key (person_id) references person(person_id)
);

create table if not exists staff (
    staff_id int auto_increment primary key,
    person_id int not null unique,
    staff_number varchar(30) not null unique,
    position varchar(50) not null,
    department varchar(100),
    foreign key (person_id) references person(person_id)
);

create table if not exists medical_record (
    medical_record_id int auto_increment primary key,
    student_id int not null unique,
    medical_conditions text,
    foreign key (student_id) references student(student_id)
);

create table if not exists consultation (
    consultation_id int auto_increment primary key,
    medical_record_id int not null,
    staff_id int not null,
    record_type varchar(50),
    consultation_date datetime not null,
    complaint text,
    diagnosis text not null,
    treatment text not null,
    action_taken text,
    remarks text,
    foreign key (medical_record_id) references medical_record(medical_record_id),
    foreign key (staff_id) references staff(staff_id)
);

create table if not exists medicine (
    medicine_id int auto_increment primary key,
    medicine_name varchar(100) not null,
    category varchar(100),
    unit varchar(30) not null
);

create table if not exists inventory (
    item_id int auto_increment primary key,
    medicine_id int not null ,
    batch_number varchar(50),
    stock_quantity int not null default 0,
    expiration_date date,
    foreign key (medicine_id) references medicine(medicine_id),
    check (stock_quantity >= 0)
);

create table if not exists prescription (
    prescription_id int auto_increment primary key,
    consultation_id int not null,
    item_id int not null,
    staff_id int not null,
    medication_quantity int not null,
    dosage varchar(100) not null,
    frequency varchar(100) not null,
    duration varchar(100),
    instructions text,
    foreign key (consultation_id) references consultation(consultation_id),
    foreign key (item_id) references inventory(item_id),
    foreign key (staff_id) references staff(staff_id),
    check (medication_quantity > 0)
);

insert into person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number) VALUES
('Dela Cruz', 'Juan', 'Santos', 'juan.delacruz@rocs.edu.ph', '2005-03-14', 'Male', 'Quezon City', '09171234501'),
('Reyes', 'Maria', 'Lopez', 'maria.reyes@rocs.edu.ph', '2004-08-22', 'Female', 'Marikina City', '09171234502'),
('Garcia', 'Pedro', 'Cruz', 'pedro.garcia@rocs.edu.ph', '2005-01-09', 'Male', 'Pasig City', '09171234503'),
('Mendoza', 'Ana', 'Ramos', 'ana.mendoza@rocs.edu.ph', '1990-05-17', 'Female', 'Manila', '09171234504'),
('Santos', 'Carlos', 'Diaz', 'carlos.santos@rocs.edu.ph', '1985-11-02', 'Male', 'Makati City', '09171234505'),
('Villanueva', 'Liza', 'Perez', 'liza.villanueva@rocs.edu.ph', '1992-07-30', 'Female', 'Taguig City', '09171234506');

insert into student (person_id, student_number, academic_level, year_level, section_id) VALUES
(1, 'CT22-0199', 'College', '3rd Year', 1),
(2, 'CT33-0777', 'College', '3rd Year', 1),
(3, 'CT27-0987', 'College', '2nd Year', 2);

insert into staff (person_id, staff_number, position, department) VALUES
(4, 'STF-001', 'Nurse', 'Clinic'),
(5, 'STF-002', 'Doctor', 'Clinic'),
(6, 'STF-003', 'Nurse', 'Clinic');

insert into medical_record (student_id, medical_conditions) VALUES
(1, 'Asthma'),
(2, 'None'),
(3, 'Peanut allergy');

insert into medicine (medicine_name, category, unit) VALUES
('Paracetamol 500mg', 'Analgesic', 'tablet'),
('Amoxicillin 500mg', 'Antibiotic', 'capsule'),
('Cetirizine 10mg', 'Antihistamine', 'tablet');

insert into inventory (medicine_id, batch_number, stock_quantity, expiration_date) VALUES
(1, 'B-001', 100, '2027-12-31'),
(2, 'B-002', 50, '2027-06-30'),
(3, 'B-003', 80, '2028-01-31');

insert into consultation (medical_record_id, staff_id, record_type, consultation_date, complaint, diagnosis, treatment, action_taken, remarks) VALUES
(1, 1, 'Check-up', '2026-10-20 09:30:00', 'Headache and fever', 'Viral fever', 'Rest and plenty of fluids', 'Gave medicine', 'Follow up if fever persists'),
(2, 2, 'Check-up', '2026-10-21 10:15:00', 'Cough and sore throat', 'Bacterial throat infection', 'Antibiotic course', 'Prescribed medicine', 'Return after 7 days'),
(3, 1, 'Emergency', '2026-10-22 14:00:00', 'Itchy skin and rashes', 'Allergic reaction', 'Antihistamine', 'Gave medicine', 'Monitor for 1 hour');

insert into prescription (consultation_id, item_id, staff_id, medication_quantity, dosage, frequency, duration, instructions) VALUES
(1, 1, 1, 10, '500mg', 'Every 6 hours', '3 days', 'Take after meals'),
(2, 2, 2, 21, '500mg', '3 times a day', '7 days', 'Finish all capsules'),
(3, 3, 1, 5, '10mg', 'Once a day', '5 days', 'Take at night');
 
commit;