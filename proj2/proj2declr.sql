create or replace package studentreg as
type ref_cursor is ref cursor;
function get_students(errno out number)
return ref_cursor;
function get_courses(errno out number)
return ref_cursor;
function get_classes(errno out number)
return ref_cursor;
function get_logs(errno out number)
return ref_cursor;
function get_prereq(errno out number)
return ref_cursor;
function get_enrollments(errno out number)
return ref_cursor;

function get_CourseDetails(std_dept_code in courses.dept_code%type,std_cn in courses.course#%type,errno out number)
return ref_cursor;
function get_moreEnrollmentInfo(std_dept_code in courses.dept_code%type,std_cn in courses.course#%type,errno out number)
return ref_cursor;

procedure update_course
(v_dept_code in courses.dept_code%type,v_course# in courses.course#%type, new_dept_code in courses.dept_code%type,new_course# in courses.course#%type, v_title in courses.title%type, v_credits in courses.credits%type,errno out number); 
procedure add_prequisite_course
(std_dept_code in prerequisites.dept_code%type, std_course# in prerequisites.course#%type, std_pre_dept_code in prerequisites.pre_dept_code%type, std_pre_course# in prerequisites.pre_course#%type, errno out number);
procedure add_class
(v_cid in classes.cid%type, v_dept_code in classes.dept_code%type, v_course# in classes.course#%type, v_sect# in classes.sect#%type, v_year in classes.year%type, v_semester in classes.semester%type, v_limit in classes.limit%type, v_class_size in classes.class_size%type,errno out number);
procedure add_course
(std_dept_code in courses.dept_code%type,std_cn in courses.course#%type,std_title in courses.title%type,std_credits in courses.credits%type,errno out number);
procedure delete_course
(v_dept_code in courses.dept_code%type,v_course# in courses.course#%type,errno out number);


procedure set_students
(std_sid in students.sid%type,std_sname in students.sname%type,std_status in students.status%type,std_gpa in students.gpa%type,std_email in students.email%type,std_deptname 
in students.deptname%type,errno out number);
function get_stdrec
(std_sid in students.sid%type,errno out number)
return ref_cursor;
procedure remove_students
(s_sid in students.sid%type,errno out number);
function show_prereq
(s_dept_code in prerequisites.dept_code%type,s_course# in prerequisites.course#%type,reccount in number,errno out number)
return ref_cursor;
function get_course_details
(sin_dept in courses.dept_code%type,sin_cn in courses.course#%type,errno out number)
return ref_cursor;
procedure enroll_students
(s_sid in students.sid%type,
s_cid in classes.cid%type,
errno out number);
procedure drop_class
(s_sid in students.sid%type,
s_cid in classes.cid%type,
errno out number);
procedure check_sid(s_sid in students.sid%type,errno out number);
procedure check_cid(s_cid in classes.cid%type,errno out number); 
end;
/
show errors

