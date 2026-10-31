begin
execute immediate 'drop user rcis cascade';
exception
    when others then null;
end;
/

create user rcis identified by Changeme0;
grant connect, resource, unlimited tablespace to rcis;

alter session set current_schema = rcis;

create table clinic_staff (
    staff_id      number generated always as identity primary key,
    person_id     number not null,
    staff_number  varchar2(20),
    position      varchar2(50),
    department    varchar2(100)
);

create table user_session (
    session_id   number generated always as identity primary key,
    staff_id     number not null,
    login_time   timestamp default systimestamp not null,
    logout_time  timestamp null,
    status       varchar2(100) default 'ACTIVE' not null,
    foreign key (staff_id) references clinic_staff (staff_id)
);

-- test data: staff is logged in (session 1)
insert into clinic_staff (person_id, staff_number, position, department)
values (1, 'STF-001', 'Nurse', 'Clinic');

insert into user_session (staff_id) values (1);

update user_session
set status = 'ENDED', logout_time = systimestamp
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


commit;

-- final check
select * from user_session;