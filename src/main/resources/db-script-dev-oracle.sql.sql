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

