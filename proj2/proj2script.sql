drop table logs;
drop table prerequisites;
drop table enrollments;
drop table classes;
drop table courses;
drop table students;

create table students (sid char(4) primary key check (sid like 'B%'),
sname varchar2(20) not null, status varchar2(10)
check (status in ('freshman', 'sophomore', 'junior', 'senior', 'graduate')),
gpa number(3,2) check (gpa between 0 and 4),
email varchar2(20) unique, deptname varchar2(10) not null);

create table courses (dept_code varchar2(4) not null, course# number(3) not null,
title varchar2(20) not null, credits number(1) check (credits in (3, 4)),
primary key (dept_code, course#));

create table prerequisites (dept_code varchar2(4) not null,
course# number(3) not null, pre_dept_code varchar2(4) not null,
pre_course# number(3) not null,
primary key (dept_code, course#, pre_dept_code, pre_course#),
foreign key (dept_code, course#) references courses on delete cascade,
foreign key (pre_dept_code, pre_course#) references courses
on delete cascade);

create table classes (cid char(5) primary key check (cid like 'c%'),
dept_code varchar2(4) not null, course# number(3) not null,
sect# number(3), year number(4), semester varchar2(10)
check (semester in ('Spring', 'Fall', 'Summer')), limit number(3),
class_size number(3), foreign key (dept_code, course#) references courses
on delete cascade, unique(dept_code, course#, sect#, year, semester),
check (class_size <= limit));

create table enrollments (sid char(4) references students, 
cid char(5) references classes,
lgrade char, primary key (sid, cid));

create table logs (logid number(5) primary key, who varchar2(15) not null, 
time date not null, what varchar2(100));

insert into students values ('B001', 'Anne', 'junior', 3.4, 'anne@bu.edu', 'CS');
insert into students values ('B002', 'Terry', 'senior', 2.8, 'terry@bu.edu', 'CS');
insert into students values ('B003', 'Wang', 'senior', 3.2, 'wang@bu.edu', 'Math');
insert into students values ('B004', 'Barbara', 'junior', 2.9, 'barbara@bu.edu', 'ECE');
insert into students values ('B005', 'Smith', 'graduate', 3.5, 'smith@bu.edu', 'Math');
insert into students values ('B006', 'Terry', 'graduate', 3.7, 'terry1@bu.edu', 'CS');
insert into students values ('B007', 'Becky', 'senior', 4.0, 'becky@bu.edu', 'CS');

insert into courses values ('CS', 432, 'database systems', 4);
insert into courses values ('Math', 314, 'discrete math', 4);
insert into courses values ('CS', 240, 'data structure', 4);
insert into courses values ('Math', 221, 'calculus I', 4);
insert into courses values ('CS', 532, 'database systems', 3);
insert into courses values ('CS', 552, 'operating systems', 3);
insert into courses values ('BIOL', 425, 'molecular biology', 4);

insert into prerequisites values ('CS', 432, 'CS', 240);
insert into prerequisites values ('CS', 532, 'CS', 432);
insert into prerequisites values ('Math', 314, 'Math', 221);
insert into prerequisites values ('CS', 432, 'Math', 314);
insert into prerequisites values ('CS', 552, 'CS', 240);

insert into classes values  ('c0001', 'CS', 432, 1, 2009, 'Spring', 3, 1);
insert into classes values  ('c0002', 'Math', 314, 1, 2008, 'Fall', 2, 2);
insert into classes values  ('c0003', 'Math', 314, 2, 2008, 'Fall', 3, 2);
insert into classes values  ('c0004', 'CS', 432, 1, 2008, 'Spring', 2, 1);
insert into classes values  ('c0005', 'CS', 240, 1, 2009, 'Spring', 4, 4);
insert into classes values  ('c0006', 'CS', 532, 1, 2009, 'Spring', 2, 1);
insert into classes values  ('c0007', 'Math', 221, 1, 2009, 'Spring', 5, 4);

insert into enrollments values  ('B001', 'c0001', 'A');
insert into enrollments values  ('B002', 'c0002', 'B');
insert into enrollments values  ('B006', 'c0007', 'A');
insert into enrollments values  ('B004', 'c0005', 'C');
insert into enrollments values  ('B005', 'c0005', 'B');
insert into enrollments values  ('B005', 'c0007', 'B');
insert into enrollments values  ('B006', 'c0003', 'A');
insert into enrollments values  ('B001', 'c0002', 'C');
insert into enrollments values  ('B003', 'c0005', null);
insert into enrollments values  ('B002', 'c0007', 'A');
insert into enrollments values  ('B001', 'c0007', 'B');
insert into enrollments values  ('B001', 'c0006', 'B');
insert into enrollments values  ('B001', 'c0005', 'A');
insert into enrollments values  ('B005', 'c0003', 'B');
insert into enrollments values  ('B005', 'c0004', 'D');