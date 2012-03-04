drop sequence seq_logid;
create sequence seq_logid
increment by 1
start with 10000 maxvalue 1000000;
/
show errors


create or replace trigger chk_class_limit
after insert on enrollments
for each row
declare
v_size classes.class_size%type;
v_limit classes.limit%type;
class_full exception;
begin
select class_size,limit into v_size,v_limit
from classes
where cid = :new.cid;
if(v_size < v_limit) then
update classes set class_size = class_size + 1 where cid = :new.cid;
insert into logs values(seq_logid.nextval,user,sysdate,'A course enrollment was inserted into enrollments table.');
else
raise class_full;
end if;
exception
when class_full then
raise_application_error(-20005,'Class is closed');
end;
/
show errors

CREATE OR REPLACE TRIGGER manage_student_table
AFTER INSERT OR UPDATE OR DELETE ON students
for each row
begin
IF INSERTING THEN
insert into logs values(seq_logid.nextval,user,sysdate,'A student was inserted into student table.');
ELSIF DELETING THEN
delete from enrollments where sid = :old.sid;
insert into logs values(seq_logid.nextval,user,sysdate,'A student was deleted from students table.');
ELSE
insert into logs values(seq_logid.nextval,user,sysdate,'A student information was updated from students table.');
END IF;
end;
/
show errors
/*
CREATE OR REPLACE TRIGGER before_del_courses
before DELETE ON courses
for each row
begin
   Delete from enrollments where cid in
   (
	   select cid from classes WHERE dept_code=:old.dept_code and course#=:old.course#
   );
   Delete from classes WHERE dept_code=:old.dept_code and course# = :old.course#;

end;
/
show errors
*/

CREATE OR REPLACE TRIGGER manage_courses_table
AFTER INSERT OR UPDATE OR DELETE ON courses
for each row
begin
IF INSERTING THEN
insert into logs values(seq_logid.nextval,user,sysdate,'A course was inserted into courses table.');
ELSIF DELETING THEN
insert into logs values(seq_logid.nextval,user,sysdate,'A course was deleted from courses table.');
ELSE
insert into logs values(seq_logid.nextval,user,sysdate,'A course information was updated from courses table.');
END IF;
end;
/
show errors


create or replace trigger manage_classes_table
AFTER INSERT OR UPDATE OR DELETE ON classes
for each row
begin
IF INSERTING THEN
insert into logs values(seq_logid.nextval,user,sysdate,'A class was inserted into classes table.');
ELSIF DELETING THEN
insert into logs values(seq_logid.nextval,user,sysdate,'A class was deleted from classes table.');
ELSE
insert into logs values(seq_logid.nextval,user,sysdate,'A class information was updated from classes table.');
END IF;
end;
/
show errors

create or replace trigger manage_prerequisites_table
AFTER INSERT OR UPDATE OR DELETE ON prerequisites
for each row
begin
IF INSERTING THEN
insert into logs values(seq_logid.nextval,user,sysdate,'A prerequisites course was inserted into prerequisites table.');
ELSIF DELETING THEN
insert into logs values(seq_logid.nextval,user,sysdate,'A prerequisites course was deleted from prerequisites table.');
ELSE
insert into logs values(seq_logid.nextval,user,sysdate,'A prerequisites course information was updated from prerequisites table.');
END IF;
end;
/
show errors

create or replace trigger delete_update
after delete on enrollments
for each row
begin
update classes set class_size = class_size - 1 where cid = :old.cid;
insert into logs values(seq_logid.nextval,user,sysdate,'Course enrollment was deleted from enrollments table.');
end;
/
show errors
