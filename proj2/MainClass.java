import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;
import java.io.Console;
import java.sql.SQLException;
//import javax.swing.*;
//import java.awt.*;
/*
 *  This class is the Main Class used in the application. It is responsible
 *  of showing menu and taking input from the user.
 */
public class MainClass
{
	private boolean isExit=false; //stores the exit status
	private BufferedReader itsBr; //reader for taking input
	private int itsInput=0;		  //
	private DBClass itsDBClassObj;
	private List<String> itsResultList;

	public MainClass() throws Exception
	{
		itsBr = new BufferedReader(new InputStreamReader(System.in));
	}

	public void showLoginMenu() throws Exception
	{
		 boolean isLogin=false;
		 Console console = System.console();
	      if (console == null)
	      {
	            System.out.println("Couldn't get Console instance, maybe you're running this from within an IDE?");
	            System.exit(0);
	      }
	      else
	      {
			System.out.println("Enter you login details :");
			System.out.print("Username:");
			String aUserName = itsBr.readLine();
			aUserName = "abiscui1";
			String aPassword = new String(console.readPassword("Password:"));
			aPassword = "123456456";
			//try
			//{
			itsDBClassObj = new DBClass(aUserName,aPassword);
			isLogin=true;
			//}
			//catch(SQLException ex)
			//{
				//System.out.println(ex.getErrorCode());
			//	if(ex.getErrorCode()==1017)
			//		isLogin=false;
			//}
			//if(isLogin=true)
				showMainMenu();
			//else
			//{
			//	System.out.println("!! Invalid Username or Password.");
				//showLoginMenu();
			//}
	      }
	}

	public void showMainMenu() throws Exception
	{
		try{
			while(isExit==false)
			{

				System.out.println("Select from the following options:-");
				System.out.println("1. Show Information.");
				System.out.println("2. Add information.");
				System.out.println("3. Search or Delete information.");
				System.out.println("4. Update Information. ");
				System.out.println("5. EXIT");
				System.out.print("Enter Your Choice:");
				itsInput = Integer.parseInt(itsBr.readLine());
				clearScreen();
				switch(itsInput)
				{
					case 1: showViewMenu();
							break;
					case 2: showAddUpdateMenu();
							break;
					case 3: showSearchDeleteMenu();
							break;
					case 4: showUpdateMenu();
							break;
					case 5: isExit = true;
							break;
				}
			}
		}
		catch(Exception e){
			System.out.println("Either the input entered is invalid or operation is not supported!! ");
			System.out.println("Exception is :" + e.getMessage());
		}
	}

	public void showUpdateMenu() throws Exception
		{
			while(isExit==false)
			{
				System.out.println();
				System.out.println();
				System.out.println("Select the option for update :");
				System.out.println("1. Update course information.");
				System.out.println("2. BACK.");
				System.out.println("3. EXIT");
				System.out.print("Enter Your Choice: ");
				itsInput = Integer.parseInt(itsBr.readLine());
				clearScreen();
				switch(itsInput)
				{
					case 1: showUpdateCourse();
							break;
					case 2: showMainMenu();
							break;
					case 3: isExit = true;
							break;
				}
			}
	}

	public void showUpdateCourse() throws Exception
		{
			String aDEPT_CODE=null;
			String aCOURSE=null;
			String nDEPT_CODE=null;
			String nCOURSE=null;
			String aTITLE=null;
			String aCREDITS = null;
			System.out.println("Enter Course Information for update");
			System.out.print("Previous Dept_code :");
			aDEPT_CODE = itsBr.readLine();
			System.out.print("Previous Course#: ");
			aCOURSE = itsBr.readLine();
			System.out.print("New Dept_code :");
			nDEPT_CODE = itsBr.readLine();
			System.out.print("New Course#: ");
			nCOURSE = itsBr.readLine();

			System.out.print("New Title: ");
			aTITLE = itsBr.readLine();
			System.out.print("New Credits: ");
			aCREDITS = itsBr.readLine();
			itsResultList = itsDBClassObj.UpdateCourse(aDEPT_CODE, aCOURSE,nDEPT_CODE, nCOURSE, aTITLE, aCREDITS);
			for(int i=0;i<itsResultList.size();i++)
			{
				System.out.println(itsResultList.get(i));
			}
	}

	public void showViewMenu() throws Exception
	{
		while(isExit==false)
		{
			System.out.println();
			System.out.println();
			System.out.println("Select the information to be displayed :");
			System.out.println("1. Show all students information.");
			System.out.println("2. Show all courses information.");
			System.out.println("3. Show all classes information.");
			System.out.println("4. Show all enrollment information.");
			System.out.println("5. Show all prereqisite information.");
			System.out.println("6. Show all logs information.");
			System.out.println("7. Show more information about a course. ");
			System.out.println("8. Show more information about a student. ");
			System.out.println("9. Show more information about enrollment. ");
			System.out.println("10. BACK.");
			System.out.println("11. EXIT");
			System.out.print("Enter Your Choice: ");
			itsInput = Integer.parseInt(itsBr.readLine());
			clearScreen();
			switch(itsInput)
			{
				case 1: showAllStudents();
						break;
				case 2: showAllCourses();
						break;
				case 3: showAllClasses();
						break;
				case 4: showAllEnrollments();
						break;
				case 5: showAllPreReq();
						break;
				case 6: showAllLogs();
						break;
				case 7: showCourseInfo();
						break;
				case 8: getStudentcourseInformation();
						break;
				case 9: getmoreEnrollmentInfo();
						break;
				case 10: showMainMenu();
						break;
				case 11: isExit = true;
						break;
			}
		}
	}

	public void getmoreEnrollmentInfo() throws Exception
	{
		System.out.print("Enter dept_code of the course: ");
		String aCID = itsBr.readLine();
		System.out.print("Enter course# of the course: ");
		String aCN = itsBr.readLine();
		itsResultList = itsDBClassObj.getmoreEnrollmentInfo(aCID, aCN);
		for(int i=0;i<itsResultList.size();i++)
			System.out.println(itsResultList.get(i));
		showViewMenu();
	}

	public void getStudentcourseInformation() throws Exception
	{
		System.out.print("Enter SID of the student:");
		String aSID = itsBr.readLine();
		itsResultList = itsDBClassObj.getStudentCoursesInfo(aSID);
		for(int i=0;i<itsResultList.size();i++)
			System.out.println(itsResultList.get(i));
		showViewMenu();
	}


	public void showCourseInfo() throws Exception
	{
		System.out.print("Enter dept_code of the course:");
		String aCID = itsBr.readLine();
		System.out.print("Enter course# of the course:");
		String aCN = itsBr.readLine();
		itsResultList = itsDBClassObj.getCourseInfo(aCID, aCN);
		for(int i=0;i<itsResultList.size();i++)
			System.out.println(itsResultList.get(i));
		showViewMenu();
	}


	public void showAllStudents()throws Exception
	{
		itsResultList = itsDBClassObj.getAllStudetnsList();
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
		showViewMenu();
	}

	public void showAllCourses() throws Exception
	{
		itsResultList = itsDBClassObj.getAllCoursesList();
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
		showViewMenu();
	}

	public void showAllClasses() throws Exception
	{
		itsResultList = itsDBClassObj.getAllClassesList();
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
		showViewMenu();
	}

	public void showAllLogs()throws Exception
	{
		itsResultList = itsDBClassObj.getAllLogsList();
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
		showViewMenu();
	}

	public void showAllEnrollments() throws Exception
	{
		itsResultList = itsDBClassObj.getAllEnrollmentsList();
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
		showViewMenu();
	}

	public void showAllPreReq() throws Exception
	{
		itsResultList = itsDBClassObj.getAllPreRequisiteList();
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
		showViewMenu();
	}

	public void showAddUpdateMenu()throws Exception
	{
		while(isExit==false)
		{
			System.out.println("1. Add new student.");
			System.out.println("2. Enroll student into a class.");
			System.out.println("3. Add new course.");
			System.out.println("4. Add new class.");
			System.out.println("5. Add New prerequisite course.");
			System.out.println("6. BACK");
			System.out.println("7. EXIT");
			System.out.print("Enter Your Choice:");
			itsInput = Integer.parseInt(itsBr.readLine());
			clearScreen();
			switch(itsInput)
			{
				case 1: showAddNewStudent();
						break;
				case 2: showEnrollStudent();
						break;
				case 3: showAddNewCourse();
						break;
				case 4: showAddNewClass();
						break;
				case 5: showAddNewprerequisite();
						break;
				case 6: showMainMenu();
						break;
				case 7: isExit = true;
			}
		}
	}

	public void showAddNewprerequisite() throws Exception
		{
			String aDEPT_CODE=null;
			String aCOURSE=null;
			String aPRE_DEPT_CODE=null;
			String aPRE_COURSE=null;

			System.out.println("Enter New Course Information");
			System.out.print("Dept_code :");
			aDEPT_CODE = itsBr.readLine();
			System.out.print("Course#: ");
			aCOURSE = itsBr.readLine();
			System.out.print("prerequisite Dept_code :");
			aPRE_DEPT_CODE = itsBr.readLine();
			System.out.print("prerequisite Course#: ");
			aPRE_COURSE = itsBr.readLine();

			itsResultList = itsDBClassObj.addNewPreRequisite(aDEPT_CODE, aCOURSE, aPRE_DEPT_CODE, aPRE_COURSE);
			for(int i=0;i<itsResultList.size();i++)
			{
				System.out.println(itsResultList.get(i));
			}
	}

	public void showAddNewClass() throws Exception
			{
				String aCID=null;
				String aDEPT_CODE=null;
				String aCOURSE=null;
				String aSECT = null;
				String aYEAR = null;
				String aSEMESTER = null;
				String aLIMIT = null;
				String aCLASS_SIZE = null;

				System.out.println("Enter New Class Information");
				System.out.print("Class ID :");
				aCID = itsBr.readLine();
				System.out.print("Dept_code : ");
				aDEPT_CODE = itsBr.readLine();
				System.out.print("Course# : ");
				aCOURSE = itsBr.readLine();
				System.out.print("Section: ");
				aSECT = itsBr.readLine();
				System.out.print("Year: ");
				aYEAR = itsBr.readLine();
				System.out.print("Semester: ");
				aSEMESTER = itsBr.readLine();
				System.out.print("Limit: ");
				aLIMIT = itsBr.readLine();
				System.out.print("Class_size: ");
				aCLASS_SIZE = itsBr.readLine();

				itsResultList = itsDBClassObj.addNewClass(aCID,aDEPT_CODE, aCOURSE,aSECT,aYEAR,aSEMESTER, aLIMIT,aCLASS_SIZE);
				for(int i=0;i<itsResultList.size();i++)
				{
					System.out.println(itsResultList.get(i));
				}
	}

	public void showAddNewCourse() throws Exception
		{
			String aDEPT_CODE=null;
			String aCOURSE=null;
			String aTITLE=null;
			String aCREDITS = null;
			System.out.println("Enter New Course Information");
			System.out.print("Dept_code :");
			aDEPT_CODE = itsBr.readLine();
			System.out.print("Course#: ");
			aCOURSE = itsBr.readLine();
			System.out.print("Title: ");
			aTITLE = itsBr.readLine();
			System.out.print("Credits: ");
			aCREDITS = itsBr.readLine();
			itsResultList = itsDBClassObj.addNewCourse(aDEPT_CODE, aCOURSE, aTITLE, aCREDITS);
			for(int i=0;i<itsResultList.size();i++)
			{
				System.out.println(itsResultList.get(i));
			}
	}

	public void showAddNewStudent() throws Exception
	{
		String aSID=null;
		String aSNAME=null;
		String aSTATUS=null;
		String aGPA = "0.0";
		String aEmail = null;
		String aDeptName = null;
		System.out.println("Enter New Student Information");
		System.out.print("SID :");
		aSID = itsBr.readLine();
		System.out.print("Name: ");
		aSNAME = itsBr.readLine();
		System.out.print("Status: ");
		aSTATUS = itsBr.readLine();
		System.out.print("GPA: ");
		aGPA = itsBr.readLine();
		System.out.print("Email: ");
		aEmail = itsBr.readLine();
		System.out.print("Department: ");
		aDeptName = itsBr.readLine();
		itsResultList = itsDBClassObj.addNewStudent(aSID, aSNAME, aSTATUS, aGPA, aEmail, aDeptName);
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
	}

	public void getClassDetails() throws Exception
	{
		System.out.print("Enter the Class ID :");
		String aCID = itsBr.readLine();
		itsResultList = itsDBClassObj.getClassDetailsList(aCID);
		for(int i=0;i<itsResultList.size();i++)
		{
			System.out.println(itsResultList.get(i));
		}
		showAddUpdateMenu();
	}

	public void getAllPreReqInformation() throws Exception
	{
		System.out.print("Enter the Department Code:");
		String aDeptCode = itsBr.readLine();
		System.out.print("Enter the Course Number:");
		String aCourseNo = itsBr.readLine();
		itsResultList = itsDBClassObj.getPreRequisiteCoursesList(aDeptCode, aCourseNo);
		for(int i=0;i<itsResultList.size();i++)
			System.out.println(itsResultList.get(i));
		showAddUpdateMenu();
	}


	public void showEnrollStudent() throws Exception
	{
		System.out.print("Enter SID of the student:");
		String aSID = itsBr.readLine();
		System.out.print("Enter CID of the class:");
		String aCID = itsBr.readLine();
		itsResultList = itsDBClassObj.enrollStudent(aSID, aCID);
		for(int i=0;i<itsResultList.size();i++)
			System.out.println(itsResultList.get(i));
		showAddUpdateMenu();
	}

	public void showSearchDeleteMenu() throws Exception
	{
		while(isExit==false)
		{
			System.out.println("Select from the following options :");
			System.out.println("1. Delete student enrollment from class.");
			System.out.println("2. Delete a student.");
			System.out.println("3. Delete a course. ");
			System.out.println("4. BACK");
			System.out.println("5. EXIT");
			System.out.print("Enter Your Choice:");
			itsInput = Integer.parseInt(itsBr.readLine());
			switch (itsInput)
			{
				case 1: showDropClass();
						break;
				case 2: showDeleteStudent();
						break;
				case 3: showDeleteCourse();
						break;
				case 4: showMainMenu();
						break;
				case 5: isExit=true;

			}

		}
	}

	public void showDeleteCourse() throws Exception
		{
			System.out.print("Enter dept_code: ");
			String aDEPT_CODE = itsBr.readLine();
			System.out.print("Enter course#: ");
			String aCOURSE = itsBr.readLine();
			itsResultList = itsDBClassObj.deleteCourse(aDEPT_CODE, aCOURSE);
			for(int i=0;i<itsResultList.size();i++)
				System.out.println(itsResultList.get(i));
			showSearchDeleteMenu();
	}

	public void showDropClass() throws Exception
	{
		System.out.print("Enter SID of the student:");
		String aSID = itsBr.readLine();
		System.out.print("Enter CID of the class:");
		String aCID = itsBr.readLine();
		itsResultList = itsDBClassObj.dropClass(aSID, aCID);
		for(int i=0;i<itsResultList.size();i++)
			System.out.println(itsResultList.get(i));
		showSearchDeleteMenu();
	}

	public void showDeleteStudent() throws Exception
	{
		System.out.print("Enter SID of the student:");
		String aSID = itsBr.readLine();
		itsResultList = itsDBClassObj.deleteStudent(aSID);
		for(int i=0;i<itsResultList.size();i++)
			System.out.println(itsResultList.get(i));
		showSearchDeleteMenu();
	}

	public void clearScreen()
	{
		System.out.println("\u001b[2J");
	}

	public static void main(String a[])
	{
		MainClass aMain = null;
		try
		{
			aMain = new MainClass();

			aMain.showLoginMenu();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			System.out.println("Closing DB Connection");
			aMain.itsDBClassObj.closeConnection();
		}
	}
}
