set serveroutput on

create or replace package body studentreg as

/*PROCEDURE TO GET STUDENTS*/
function get_students(errno out number) 
return ref_cursor as
rc ref_cursor;

begin
errno:=0;
open rc for
select * from students;
if(rc%notfound) then
errno:=1;
end if;
return rc;
end;

/*PROCEDURE TO GET COURSES*/
function get_courses(errno out number) 
return ref_cursor as
rc ref_cursor;

begin
errno:=0;
open rc for
select * from courses;
if(rc%notfound) then
errno:=1;
end if;
return rc;
end;



/*PROCEDURE TO GET ENROLLMENTS*/
function get_enrollments(errno out number)    
return ref_cursor as               
rc ref_cursor;

begin
errno:=0;
open rc for
select * from enrollments;
if(rc%notfound) then
errno:=1;
end if; 
return rc;
end;


/*PROCEDURE TO GET CLASSES*/
function get_classes(errno out number)    
return ref_cursor as               
rc ref_cursor;

begin
errno:=0;
open rc for
select * from classes;
if(rc%notfound) then
errno:=1;
end if; 
return rc;
end;


/*PROCEDURE TO GET LOGS*/
function get_logs(errno out number)    
return ref_cursor as               
rc ref_cursor;

begin
errno:=0;
open rc for
select * from logs;
if(rc%notfound) then
errno:=1;
end if; 
return rc;
end;


/*PROCEDURE TO GET PREREQUISITES*/
function get_prereq(errno out number)    
return ref_cursor as               
rc ref_cursor;

begin
errno:=0;
open rc for
select * from prerequisites;
if(rc%notfound) then
errno:=1;
end if; 
return rc;
end;


/*GET MORE ENROLLMENT INFO*/
function get_moreEnrollmentInfo
  (std_dept_code in courses.dept_code%type,
  std_cn in courses.course#%type,errno out number)
  return ref_cursor as
  rc ref_cursor;

/* If course is not present in Courses table */
cursor c2 is 
Select cr.title from courses cr where cr.dept_code = std_dept_code and cr.course# = std_cn;
c2_rec c2%rowtype;

/* If no student has taken or is taking in the class */
cursor c3 is 
Select distinct cr.title, s.sid, s.sname from courses cr, students s, classes cl, enrollments e where s.sid = e.sid and e.cid  = cl.cid and cl.dept_code = cr.dept_code and cl.course# = cr.course# and cr.dept_code = std_dept_code and cr.course# = std_cn;
c3_rec c3%rowtype;
begin

errno:=0;

open c2;
fetch c2 into c2_rec;

if(c2%notfound) then
errno:=19;
end if;
close c2;

open c3;
fetch c3 into c3_rec;

if(c3%notfound and errno < 1) then
  errno:=20;
end if;

close c3;


open rc for
Select distinct cr.title, s.sid, s.sname from courses cr, students s, classes cl, enrollments e where s.sid = e.sid and e.cid  = cl.cid and cl.dept_code = cr.dept_code and cl.course# = cr.course# and cr.dept_code = std_dept_code and cr.course# = std_cn;
return rc;

end;


/*GET COURSE DETAILS*/
function get_CourseDetails(
std_dept_code in courses.dept_code%type,
std_cn in courses.course#%type,
errno out number
)
return ref_cursor as
rc ref_cursor;

cursor c2 is 
Select * from courses where dept_code = std_dept_code and course# = std_cn;
c2_rec c2%rowtype;

begin

errno:=0;

open c2;
fetch c2 into c2_rec;

if(c2%notfound) then
errno:=18;
end if;
close c2;
open rc for
select distinct * from (select c.dept_code,c.course#,c.title,c.credits,p.pre_dept_code,p.pre_course# from courses c 
left join prerequisites p on c.dept_code = p.dept_code and c.course# = p.course# union select c.dept_code,c.course#,
c.title,c.credits,p.pre_dept_code,p.pre_course# from courses c right join prerequisites p on c.dept_code = p.dept_code and c.course# = p.course# )
where ( dept_code = std_dept_code and course# = std_cn) Or (dept_code,course#) in (select pre_dept_code, pre_course# from Prerequisites 
where dept_code = std_dept_code and course# = std_cn);
return rc;

end;

/*
open c1;
fetch c1 into c1_rec;

open rc for
select * from courses where dept_code = c1.

course_exist exception;
stdcount number;
begin
errno:=0;
select count(*) into stdcount from courses where dept_code = std_dept_code and course# = std_cn;
if(stdcount !=0) then
raise course_exist;
end if;
open rc for
select * from courses where dept_code = std_dept_code and course# = std_cn;
exception
when course_exist then
dbms_output.put_line('Invalid course id');
errno:=18;
end;
*/

/* UPDATE COURSE INFORMATION */
procedure update_course
(v_dept_code in courses.dept_code%type,
 v_course# in courses.course#%type,
 new_dept_code in courses.dept_code%type,
 new_course# in courses.course#%type,
 v_title in courses.title%type,
 v_credits in courses.credits%type,
 errno out number)
is
course_exist exception;
cursor c1 is select c.dept_code,c.course# from courses c where c.dept_code=v_dept_code and c.course#=v_course#;
c1_rec c1%rowtype;
begin
errno := 0;
open c1;
fetch c1 into c1_rec;
if(c1%rowcount = 0) then
	raise course_exist;
        close c1;
else
	update courses set dept_code=new_dept_code,course#=new_course#,title=v_title,credits=v_credits where dept_code=v_dept_code and course#=v_course#; 
	if(v_title = 'unknown') then
		errno := 25;
		dbms_output.put_line('Update a course with full information');
	end if;
end if;
exception
when course_exist then
	dbms_output.put_line('Invalid course id.');
	errno := 18;
end;

/*ADD COURSE*/
procedure add_course(
std_dept_code in courses.dept_code%type,
std_cn in courses.course#%type,
std_title in courses.title%type,
std_credits in courses.credits%type,
errno out number
)is
course_exist exception;
credits_OutOfBound exception;
stdcount number;

begin
errno:=0;
select count(*) into stdcount from courses where course# = std_cn and dept_code = std_dept_code;

if(stdcount !=0) then
raise course_exist;
end if;

if(std_credits !=3 and std_credits !=4) then
raise credits_OutOfBound;
end if;

insert into Courses values
(std_dept_code, std_cn, std_title, std_credits);
exception

when course_exist then
errno:=21;
when credits_OutOfBound then
errno:=22;
end;

/*ADD CLASS*/
procedure add_class
(v_cid in classes.cid%type,
 v_dept_code in classes.dept_code%type,
 v_course# in classes.course#%type,
 v_sect# in classes.sect#%type,
 v_year in classes.year%type,
 v_semester in classes.semester%type,
 v_limit in classes.limit%type,
 v_class_size in classes.class_size%type,
 errno out number)
is
class_exist exception;
credits number;
coursetitle varchar2(20);
cursor c1 is select c.dept_code,c.course# from courses c where c.dept_code=v_dept_code and c.course#=v_course#;
c1_rec c1%rowtype;

cursor c2 is select * from classes where cid = v_cid;
c2_rec c2%rowtype;
begin

errno := 0;
open c2;
fetch c2 into c2_rec;
if(c2%rowcount > 0) then
	raise class_exist;
end if;

open c1;
fetch c1 into c1_rec;
if(c1%rowcount = 0) then
	credits := 4;
	coursetitle := 'unknown';
	add_course(v_dept_code,v_course#,coursetitle,credits,errno);
        dbms_output.put_line('A new course ('|| v_dept_code || v_course# || ') is added to courses table, update its information later.');
	close c1;
end if;
if(errno <1) then
	insert into classes values(v_cid,v_dept_code,v_course#,v_sect#,v_year,v_semester,v_limit,v_class_size);
	dbms_output.put_line('one record successfully added');
end if;
exception
when class_exist then
dbms_output.put_line('This class is already in the table');
errno := 23;
end;

/* ADD PRE-REQUISITE COURSE */
procedure add_prequisite_course
(std_dept_code in prerequisites.dept_code%type,
 std_course# in prerequisites.course#%type,
 std_pre_dept_code in prerequisites.pre_dept_code%type,
 std_pre_course# in prerequisites.pre_course#%type,
 errno out number)
is
entry_exist exception;
credits number;
coursetitle varchar2(20);
cursor c1 is select c.dept_code,c.course# from courses c where c.dept_code=std_dept_code and c.course#=std_course#;
c1_rec c1%rowtype;
cursor c2 is select c.dept_code,c.course# from courses c where c.dept_code=std_pre_dept_code and c.course#=std_pre_course#;
c2_rec c2%rowtype;
cursor c3 is select * from courses c where c.dept_code=std_pre_dept_code and c.course#=std_pre_course# and c.dept_code=std_dept_code and c.course#=std_course#;
c3_rec c3%rowtype;

begin
errno := 0;

open c3;
fetch c3 into c3_rec;
if(c3%rowcount > 0 ) then
	raise entry_exist;
end if;

open c1;
fetch c1 into c1_rec;
if(c1%rowcount < 1 ) then
	credits := 4;
        coursetitle := 'unknown';
        add_course(std_dept_code,std_course#,coursetitle,credits,errno);
        close c1;
end if;
open c2;
fetch c2 into c2_rec;
if(c2%rowcount <1 and errno < 1) then
        credits := 4;
        coursetitle := 'unknown';
        add_course(std_pre_dept_code,std_pre_course#,coursetitle,credits,errno);
        close c1;
end if;
if (errno < 1) then
	insert into prerequisites values(std_dept_code,std_course#,std_pre_dept_code,std_pre_course#);
	dbms_output.put_line('One record successfully added');
end if;

exception
when entry_exist then
dbms_output.put_line('This entry already exist');
errno := 24;
end;



/*SET STUDENTS*/
procedure set_students(
std_sid in students.sid%type,
std_sname in students.sname%type,
std_status in students.status%type,
std_gpa in students.gpa%type,
std_email in students.email%type,
std_deptname in students.deptname%type,
errno out number
)is
student_exist exception;
stdcount number;
emailcount number;
email_found exception;
begin
errno:=0;
select count(*) into stdcount from students where sid = std_sid;
select count(*) into emailcount from students where email=std_email;
if(stdcount !=0) then
raise student_exist;
end if;
if(emailcount !=0) then
raise email_found;
end if;
insert into Students values
(std_sid,std_sname,std_status,std_gpa,std_email,std_deptname);
exception
when student_exist then
errno:=15;
when email_found then
errno:=14;
end;


/*GET STUDENT RECORD*/
function get_stdrec(
std_sid in students.sid%type,
errno out number
)
return ref_cursor as
rc ref_cursor;
cursor c1 is select * from enrollments e where e.sid = std_sid;
c1_rec c1%rowtype;
begin

open c1;
fetch c1 into c1_rec;

open rc for
select distinct c.dept_code,c.course#,c.title from courses c,classes c1,enrollments e
where e.cid=c1.cid and c1.dept_code=c.dept_code and c1.course# = c.course# and e.sid=std_sid;
errno:=0;


if(c1%notfound) then
errno:=4; 
end if;
close c1;
return rc;
end;


/*SHOW PREREQUISITE*/
function show_prereq
(s_dept_code in prerequisites.dept_code%type,
s_course# in prerequisites.course#%type,
reccount in number,
errno out number)
return ref_cursor as
rc ref_cursor;
cursor c1 is select * from prerequisites p where p.dept_code = s_dept_code and p.course# = s_course#;
c1_rec c1%rowtype; 
begin

open c1;
fetch c1 into c1_rec;

errno:=0;
if(c1%notfound AND reccount=0) then
errno:=16;
end if;
close c1;

open rc for
select pre_dept_code,pre_course# from prerequisites p where p.dept_code=s_dept_code and p.course# = s_course#;
return rc;

exception
when no_data_found then
errno:=1;
end;

/*REMOVE STUDENT RECORD*/
procedure remove_students
(s_sid in students.sid%type,
errno out number
)is
begin
delete from students s where s.sid=s_sid;
errno:=0;
exception
when no_data_found then
dbms_output.put_line('The sid is invalid');
errno:=2;
end;


/*GET COURSE DETAILS*/
function get_course_details
 (sin_dept in courses.dept_code%type,
 sin_cn in courses.course#%type,
 errno out number)

	return ref_cursor as
	rc ref_cursor;

	cursor c2 is
	select * from courses where dept_code = sin_dept and course# = sin_cn;
	c2_rec c2%rowtype;
begin

	errno:=0;

	open c2;
	fetch c2 into c2_rec;

	if(c2%notfound) then
		errno:=5;
	end if;
	close c2;
	open rc for
		select * from courses where dept_code = sin_dept and course# = sin_cn;
	return rc;

end;

/*STUDENT ENROLL*/
procedure enroll_students
(s_sid in Students.sid%type,
s_cid in Classes.cid%type,
errno out number)is

cursor c1 is select * from Students s where s.sid=s_sid;
c1_rec c1%rowtype;
cursor c2 is select * from classes c where c.cid = s_cid;
c2_rec c2%rowtype;
cursor c3 is 
select * from enrollments e where e.sid =s_sid and e.cid in (select c1.cid from classes c1 where  (c1.dept_code,c1.course#) in (select dept_code,course# from classes where 
cid =s_cid));
c3_rec c3%rowtype;
if_enrolled number;
sem_enrollment number;
prereq_count number;
prereq_taken number;
student_not_found exception;
class_not_found exception;
already_class exception;
cannot_enroll exception;
class_closed exception;
prereq_notdone exception;
student_overloading exception;
begin

open c1;
fetch c1 into c1_rec;
if(c1%found != TRUE) then
raise student_not_found;
end if;
close c1;

open c2;
fetch c2 into c2_rec;
if(c2%found !=TRUE) then
raise class_not_found;
end if;

if(c2_rec.limit = c2_rec.class_size) then
raise class_closed;
end if;
close c2;

open c3;
fetch c3 into c3_rec;
if(c3%found) then
raise already_class;
end if;
close c3;

select count(*) into prereq_taken from enrollments where sid=s_sid and cid in  
(
select cid from classes where dept_code||course# in
(
select pre_dept_code||pre_course# from prerequisites p ,classes c
start with p.course# = c.course# and p.dept_code =c.dept_code and cid = s_cid
connect by prior p.pre_course# = p.course# and prior p.pre_dept_code = p.dept_code));


select count(*) into prereq_count from prerequisites p
start with p.dept_code||p.course# in (select dept_code||course# from classes c where cid =s_cid)
connect by prior p.pre_course# = p.course# and prior p.pre_dept_code = p.dept_code;

if(prereq_count>prereq_taken) then
raise prereq_notdone;
end if;

select count(*) into sem_enrollment from enrollments e where e.sid=s_sid and e.cid in (select c1.cid from classes c1 where (c1.year,c1.semester) in (select 
c.year,c.semester from classes c where c.cid =s_cid));

if(sem_enrollment>=4) then
raise cannot_enroll;
end if;
if(sem_enrollment = 3) then
insert into enrollments values (s_sid,s_cid,null);
raise student_overloading;
else
insert into enrollments values (s_sid,s_cid,null);
errno:=0;
end if;

exception
when student_not_found then
dbms_output.put_line('The sid is invalid');
errno:=2;
when class_not_found then
dbms_output.put_line('The cid is invalid');
errno:=3;
when already_class then
dbms_output.put_line('Student is already in the class');
errno:=6;
when cannot_enroll then
dbms_output.put_line('Students cannot be enrolled in more than four classes in the same semester.');
errno:=7;
when class_closed then
dbms_output.put_line('Class is closed');
errno:=8;
when prereq_notdone then
dbms_output.put_line('Prerequisites not completed');
errno:=9;
when student_overloading then
dbms_output.put_line('You are overloading');
errno:=17;
end;

/* DELETE COURSE */
procedure delete_course
(v_dept_code in courses.dept_code%type,v_course# in courses.course#%type,errno out number)
is
cursor c1 is select c.dept_code,c.course# from courses c where c.dept_code=v_dept_code and c.course#=v_course#;
c1_rec c1%rowtype;
invalid_course exception;

cursor c2 is select cid from classes where dept_code=v_dept_code and course#=v_course#;
c2_rec c2%rowtype;

begin
errno := 0;
open c1;
fetch c1 into c1_rec;
if(c1%rowcount = 0) then
	raise invalid_course;
	close c1;
else
        open c2;
		fetch c2 into c2_rec;
		if(c2%rowcount != 0) then
			close c2;
			for c2_rec in c2 loop
				delete from enrollments where cid=c2_rec.cid;
				delete from classes where cid=c2_rec.cid;
			end loop;
		else 
			close c2;
		end if;
		delete from prerequisites where dept_code=v_dept_code and course#=v_course#;
		delete from prerequisites where pre_dept_code=v_dept_code and pre_course#=v_course#;
		delete from courses where dept_code=v_dept_code and course#=v_course#;
end if;
        exception
        when invalid_course then
        dbms_output.put_line('Invalid course information.');
        errno := 19;
end;


/*DROP A CLASS STUDENT*/   
procedure drop_class
(s_sid in Students.sid%type,
s_cid in Classes.cid%type,
errno out number)is

cursor c1 is select * from Students s where s.sid=s_sid;
c1_rec c1%rowtype;
cursor c2 is select * from classes c where c.cid = s_cid;
c2_rec c2%rowtype;
student_enrollment number;   
class_enrollment number;
if_enrolled number;

student_not_found exception;
class_not_found exception;
not_enrolled exception;
empty_class_student exception;
empty_class exception;
empty_student exception;

begin
errno:=0;
open c1;
fetch c1 into c1_rec;
if(c1%found != TRUE) then
raise student_not_found;
end if;
close c1;

open c2;
fetch c2 into c2_rec;
if(c2%found !=TRUE) then
raise class_not_found;
end if;

select count(*) into if_enrolled from enrollments e where e.sid = s_sid and e.cid = s_cid;
if(if_enrolled = 0) then
raise not_enrolled;
end if;
close c2;

delete from enrollments e where e.sid=s_sid and e.cid = s_cid;
errno:=0;
select count(*) into student_enrollment from enrollments e where e.sid=s_sid;

select count(*) into class_enrollment from enrollments e where e.cid=s_cid;

if(student_enrollment = 0 AND class_enrollment = 0) then
raise empty_class_student;
end if;
if(student_enrollment = 0 AND class_enrollment != 0) then
raise empty_student;
end if;
if(student_enrollment !=0 AND class_enrollment = 0) then
raise empty_class;
end if; 

exception
when student_not_found then
dbms_output.put_line('The sid is invalid');
errno:=2;
when class_not_found then
dbms_output.put_line('The cid is invalid');
errno:=3;
when not_enrolled then
dbms_output.put_line('The Student is not enrolled in class');
errno:=10;
when empty_class then
dbms_output.put_line('The class now has no students.');
errno:=11;
when empty_student then
dbms_output.put_line('This student is not enrolled in any classes.');
errno:=12;
when empty_class_student then
dbms_output.put_line('The class now has no students.');
dbms_output.put_line('This student is not enrolled in any classes.');
errno:=13;
end;

/*CHECK FOR SID*/ 
procedure check_sid(
s_sid in students.sid%type,
errno out number)is
tcount number;
begin
select count(*) into tcount from students where sid=s_sid;
if(tcount!=1) then
errno:=2;
end if;
if(tcount=1) then
errno:=0;
end if;
end;

/*CHECK FOR CID*/
procedure check_cid(
s_cid in classes.cid%type,
errno out number)is
tcount number;
begin
select count(*) into tcount from classes where cid=s_cid;
if(tcount!=1) then
errno:=3;
end if;
if(tcount=1) then
errno:=0;
end if;
end;

end studentreg;
/
show errors
