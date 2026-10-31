-- drop user and database
drop user if exists 'rcis'@'localhost';
drop database if exists rcis;

-- create database and user
create database rcis;
create user 'rcis'@'localhost' identified by 'Changeme0';
grant all privileges on rcis.* to 'rcis'@'localhost';
flush privileges;

use rcis;

create table clinic_staff (
    staff_id      int primary key auto_increment,
    person_id     int not null,
    staff_number  varchar(20),
    position      varchar(50),
    department    varchar(100)
);

create table user_session (
    session_id   int primary key auto_increment,
    staff_id     int not null,
    login_time   timestamp not null default current_timestamp,
    logout_time  timestamp null default null,
    status       varchar(100) not null default 'ACTIVE',
    foreign key (staff_id) references clinic_staff (staff_id)
);

-- test data: staff is logged in (session 1)
insert into clinic_staff (person_id, staff_number, position, department)
values (1, 'STF-001', 'Nurse', 'Clinic');

insert into user_session (staff_id) values (1);

-- click Log Out -> session ends, redirect to login page
update user_session
set status = 'ENDED', logout_time = now()
where session_id = 1 and status = 'ACTIVE';

select session_id, status, logout_time from user_session where session_id = 1;

select session_id from user_session
where session_id = 1 and status = 'ACTIVE';

update user_session
set status = 'ACTIVE', logout_time = null
where session_id = 1 and status = 'ACTIVE';


insert into user_session (staff_id) values (1);

select 'You have been logged out.' as message, 'LoginPage' as redirect_to
from user_session
where session_id = 1 and status = 'ENDED';


-- final check
select * from user_session;