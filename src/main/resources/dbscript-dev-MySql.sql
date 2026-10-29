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

CREATE TABLE IF NOT EXISTS person (
    person_id INT AUTO_INCREMENT PRIMARY KEY
  , last_name VARCHAR(50) NOT NULL
  , first_name VARCHAR(50) NOT NULL
  , middle_name VARCHAR(50)
  , email VARCHAR(100) UNIQUE
  , date_of_birth DATE
  , gender VARCHAR(20)
  , address VARCHAR(255)
  , contact_number VARCHAR(20)
);
 
 
CREATE TABLE IF NOT EXISTS staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY
  , person_id INT NOT NULL UNIQUE
  , staff_number VARCHAR(20) NOT NULL UNIQUE
  , position VARCHAR(50) NOT NULL
  , department VARCHAR(100)
  , FOREIGN KEY (person_id) REFERENCES person(person_id)
);
 
 
CREATE TABLE IF NOT EXISTS medicine (
    medicine_id INT AUTO_INCREMENT PRIMARY KEY
  , medicine_name VARCHAR(100) NOT NULL
  , category VARCHAR(50)
  , unit VARCHAR(30)
);
 
 
CREATE TABLE IF NOT EXISTS inventory (
    item_id INT AUTO_INCREMENT PRIMARY KEY
  , medicine_id INT NOT NULL
  , staff_id INT NOT NULL
  , batch_number VARCHAR(50) NOT NULL
  , stock_quantity INT NOT NULL CHECK (stock_quantity >= 0)
  , expiration_date DATE NOT NULL
  , FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id)
  , FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
  , UNIQUE (medicine_id, batch_number)
);

INSERT INTO person (last_name, first_name, middle_name, email, date_of_birth, gender, address, contact_number) VALUES
    ('Ambunan', 'Zymonne', 'Rubiano', 'ambunanz@gmail.com', DATE '2005-09-20', 'Male', 'Lumil-Silang-Cavite', '09397756953');
INSERT INTO staff (person_id, staff_number, position, department) VALUES
    (1, 'N-01', 'Nurse', 'Clinic');
INSERT INTO medicine (medicine_name, category, unit) VALUES
    ('Paracetamol 500mg', 'Analgesic', 'tablet');
INSERT INTO inventory (medicine_id, staff_id, batch_number, stock_quantity, expiration_date) VALUES
    (1, 1, 'PARACETAMOL-10-28', 200, DATE '2028-03-31');

commit;