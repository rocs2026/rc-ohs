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