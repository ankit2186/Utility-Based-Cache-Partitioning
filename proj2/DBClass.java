import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.jdbc.OracleTypes;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * This class is used to connect to the DB and invoke stored procedures of oracle.
 *
 */
public class DBClass
{
	private Connection itsConn; //connection object
	public CallableStatement itsCallable;
	private List<String> itsResultList; //output will be stored in this List
	private String itsResult;
	private ErrorMessage itsErrorObj;

	/**
	 * Constructor creates the connection
	 * @throws Exception
	 */

	public static void main(String a[])
	{
		try
		{
			DBClass aDB = new DBClass("abiscui1","123456456");

			List aList = aDB.getClassDetailsList("c0001");
			for(int i=0;i<aList.size();i++)
			{
				System.out.println(aList.get(i));
			}
			//("B018", "Gopal", "graduate", "3.0", "gb@bu.edu", "CS");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public DBClass(String theUserName, String thePassword) throws Exception
	{
		//Load Oracle driver
		itsErrorObj = new ErrorMessage();
        DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
        // Connect to the local database
        itsConn = DriverManager.getConnection ("jdbc:oracle:thin:@grouchoIII.cc.binghamton.edu:1521:ACAD111", theUserName, thePassword);
	}//end of method

	/**
	 * This method is used to get all the records in the students table
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getAllStudetnsList() throws Exception
	{
			int aErrorCode=-1;
			//calling procedure
			itsCallable = itsConn.prepareCall("begin ? := studentreg.get_students(:2); end;");
			//registering output parameter
			itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
			itsCallable.registerOutParameter(2, Types.INTEGER);
			//executing the procedure
			itsCallable.execute();
			//getting the resultset
			aErrorCode = itsCallable.getInt(2);
	        System.out.println(aErrorCode);
	        //creating new list for storing result
	        itsResultList = new ArrayList<String>();
	        if(aErrorCode != 0)
	        {
	        	itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
	        }
	        else
	        {
	        	ResultSet rs = (ResultSet)itsCallable.getObject(1);
	        	while (rs.next())
	        	{
	        		itsResult = rs.getString(1) + " | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getDouble(4)+" | "+rs.getString(5)+" | "+rs.getString(6);
	        		itsResultList.add(itsResult);
	        	}//end of while
	        	rs.close();
	        }

	        itsCallable.close();
	        return itsResultList;
	}//end of method

	/**
	 * This method shows detailed information of the given course
	 * @param theCID
	 * @param theCN
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getCourseInfo(String theCID, String theCN)throws Exception
	{
		//preparing to call procedure
		int aErrorCode=-1;
		itsCallable = itsConn.prepareCall("begin ? := studentreg.get_CourseDetails(:2,:3,:4); end;");
		//setting input parameters
		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
		itsCallable.setString(2, theCID);
		itsCallable.setString(3, theCN);
		itsCallable.registerOutParameter(4, Types.INTEGER);

		itsCallable.execute();
		//getting resultset
		itsResultList = new ArrayList<String>();
		aErrorCode = itsCallable.getInt(4);
		System.out.println(aErrorCode);
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
			ResultSet rs = (ResultSet)itsCallable.getObject(1);
			//creating result list
			while (rs.next())
			{
				String forfive = "";
				String forsix = "";
				if(rs.getString(5)!=null)
					forfive = rs.getString(5);
				if(rs.getString(6)!=null)
					forsix = rs.getString(6);
				itsResult = rs.getString(1)+rs.getInt(2)+" | "+rs.getString(3)+" | "+rs.getInt(4) + " | "+ forfive + forsix;
				itsResultList.add(itsResult);
			}//end of while
			rs.close();
		}
		itsCallable.close();
        return itsResultList;
	}


/**
	 * This method shows more information of the given course
	 * @param theCID
	 * @param theCN
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getmoreEnrollmentInfo(String theCID, String theCN)throws Exception
	{
		//preparing to call procedure
		int aErrorCode=-1;
		itsCallable = itsConn.prepareCall("begin ? := studentreg.get_moreEnrollmentInfo(:2,:3,:4); end;");
		//setting input parameters
		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
		itsCallable.setString(2, theCID);
		itsCallable.setString(3, theCN);
		itsCallable.registerOutParameter(4, Types.INTEGER);

		itsCallable.execute();
		//getting resultset
		itsResultList = new ArrayList<String>();
		aErrorCode = itsCallable.getInt(4);
		System.out.println(aErrorCode);
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
			ResultSet rs = (ResultSet)itsCallable.getObject(1);
			//creating result list
			while (rs.next())
			{
				itsResult = rs.getString(1) + " | "+rs.getString(2)+" | "+rs.getString(3);
				itsResultList.add(itsResult);
			}//end of while
			rs.close();
		}
		itsCallable.close();
        return itsResultList;
	}

	/**
	 * This method shows all the courses information i.e records in the courses table
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getAllCoursesList() throws Exception
	{
		int aErrorCode=-1;
		//calling procedure
		itsCallable = itsConn.prepareCall("begin ? := studentreg.get_courses(:2); end;");
		//registering out parameter
		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
		itsCallable.registerOutParameter(2, Types.INTEGER);
		//executing procedure
		itsCallable.execute();
		//getting resultset
		itsResultList = new ArrayList<String>();
		aErrorCode = itsCallable.getInt(2);
		System.out.println(aErrorCode);
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
	        ResultSet rs = (ResultSet)itsCallable.getObject(1);
	        //creating result list
	        while (rs.next())
	        {
	        	itsResult = rs.getString(1) + " | "+rs.getInt(2)+" | "+rs.getString(3)+" | "+rs.getInt(4);
	        	itsResultList.add(itsResult);
	        }//end of while
	        rs.close();
		}
        itsCallable.close();
        return itsResultList;
	}//end of method

	/**
	 * This method shows all the pre requisite information i.e records in prereqsite table
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getAllPreRequisiteList() throws Exception
	{
		int aErrorCode=-1;
		//calling procedure
		itsCallable = itsConn.prepareCall("begin ? := studentreg.get_prereq(:2); end;");
		//registering output parameters
		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
		itsCallable.registerOutParameter(2, Types.INTEGER);
		//executing procedure
		itsCallable.execute();
		//getting result set

		aErrorCode = itsCallable.getInt(2);
		System.out.println(aErrorCode);
		 //creating output list
        itsResultList = new ArrayList<String>();
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
	        ResultSet rs = (ResultSet)itsCallable.getObject(1);

	        while (rs.next())
	        {
	        	itsResult = rs.getString(1) + " | "+rs.getInt(2)+" | "+rs.getString(3)+" | "+rs.getInt(4);
	        	itsResultList.add(itsResult);
	        }//end of while
	        rs.close();
		}
        itsCallable.close();
        return itsResultList;
	}//end of method

	/**
	 * This method gives the informatoin about all the classes i.e all the records in class table
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getAllClassesList() throws Exception
	{
		int aErrorCode=-1;
		//calling the procedure
		itsCallable = itsConn.prepareCall("begin ? := studentreg.get_classes(:2); end;");
		//registering out parameter
		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
		itsCallable.registerOutParameter(2, Types.INTEGER);
		//executing procedure
		itsCallable.execute();

		aErrorCode = itsCallable.getInt(2);
		System.out.println(aErrorCode);
//		creating output list
        itsResultList = new ArrayList<String>();
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
			//getting result set
	        ResultSet rs = (ResultSet)itsCallable.getObject(1);

	        while (rs.next())
	        {
	        	itsResult = rs.getString(1) + " | "+rs.getString(2)+" | "+rs.getInt(3)+" | "+rs.getInt(4)+" | "+rs.getInt(5)+" | "+rs.getString(6)+" | "+rs.getInt(7)+" | "+rs.getInt(8);
	        	itsResultList.add(itsResult);
	        }//end of while
	        rs.close();
		}
        itsCallable.executeUpdate();
        return itsResultList;
	}//end of method

	/**
	 * This method gives all the enrollment information i.e all records in the enrollment table
	 * @return
	 * @throws Exception
	 */
	public List<String> getAllEnrollmentsList() throws Exception
	{
		int aErrorCode=-1;
		//calling proedure
		itsCallable = itsConn.prepareCall("begin ? := studentreg.get_enrollments(:2); end;");
		//registering out parameter
		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
		itsCallable.registerOutParameter(2, Types.INTEGER);
		//executing proedure
		itsCallable.execute();
		aErrorCode = itsCallable.getInt(2);
		System.out.println(aErrorCode);
		 //creating output result list
        itsResultList = new ArrayList<String>();
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
			//getting result list
	        ResultSet rs = (ResultSet)itsCallable.getObject(1);

	        while (rs.next())
	        {
	        	itsResult = rs.getString(1) + " | "+rs.getString(2)+" | "+rs.getString(3);
	        	itsResultList.add(itsResult);
	        }//end of while
	        rs.close();
		}
        itsCallable.executeUpdate();
        return itsResultList;
	}//end of method

	/**
	 * This method gives all the information of the Logs i.e all the reocrds in logs table
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getAllLogsList() throws Exception
	{
		int aErrorCode=-1;
		//preparing call for the proedure
		itsCallable = itsConn.prepareCall("begin ? := studentreg.get_logs(:2); end;");
		//registering out paramter

		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
		itsCallable.registerOutParameter(2, Types.INTEGER);
		//executing procedure
		itsCallable.execute();

		aErrorCode = itsCallable.getInt(2);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
			//getting result set
	        ResultSet rs = (ResultSet)itsCallable.getObject(1);
	        //creating new output list

	        while (rs.next())
	        {
	        	itsResult = rs.getInt(1) + " | "+rs.getString(2)+" | "+rs.getDate(3)+" | "+rs.getString(4);
	        	itsResultList.add(itsResult);
	        }//end of while
	        rs.close();
		}
        itsCallable.executeUpdate();
        return itsResultList;
	}//end of method


	/**
	 * This method adds a new PreRequisite Course information in the PreRequisite table.
	 * @param theDEPT_CODE
	 * @param theCOURSE
	 * @param thePRE_DEPT_CODE
	 * @param thePRE_COURSE
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> addNewPreRequisite(String theDEPT_CODE, String theCOURSE, String thePRE_DEPT_CODE, String thePRE_COURSE) throws Exception
	{
		int aErrorCode=-1;
		//preparing to call procedure
		itsCallable = itsConn.prepareCall("begin studentreg.add_prequisite_course(:1,:2,:3,:4,:5); end;");
		//setting the paramter values for the procedure
		itsCallable.setString(1, theDEPT_CODE);
		itsCallable.setInt(2, Integer.parseInt(theCOURSE));
		itsCallable.setString(3, thePRE_DEPT_CODE);
		itsCallable.setInt(4, Integer.parseInt(thePRE_COURSE));
		itsCallable.registerOutParameter(5, Types.INTEGER);
		//executing update
		System.out.println("ADDING INFO");
		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(5);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		return itsResultList;
		//itsCallable.close();
	}//end of method



/**
	 * This method adds a new Class information in the courses table.
	 * @param theaCID
	 * @param theaDEPT_CODE
	 * @param theaCOURSE
	 * @param theaSECT
	 * @param theaYEAR
	 * @param theaSEMESTER
	 * @param theaLIMIT
	 * @param theaCLASS_SIZE
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> addNewClass(String theaCID, String theaDEPT_CODE, String theaCOURSE, String theaSECT, String theaYEAR, String theaSEMESTER, String theaLIMIT, String theaCLASS_SIZE) throws Exception
	{
		int aErrorCode=-1;
		//preparing to call procedure
		itsCallable = itsConn.prepareCall("begin studentreg.add_class(:1,:2,:3,:4,:5,:6,:7,:8,:9); end;");
		//setting the paramter values for the procedure
		itsCallable.setString(1, theaCID);
		itsCallable.setString(2, theaDEPT_CODE);
		itsCallable.setInt(3, Integer.parseInt(theaCOURSE));
		itsCallable.setInt(4, Integer.parseInt(theaSECT));
		itsCallable.setInt(5, Integer.parseInt(theaYEAR));
		itsCallable.setString(6, theaSEMESTER);
		itsCallable.setInt(7, Integer.parseInt(theaLIMIT));
		itsCallable.setInt(8, Integer.parseInt(theaCLASS_SIZE));
		itsCallable.registerOutParameter(9, Types.INTEGER);
		//executing update
		System.out.println("ADDING INFO");
		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(9);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		return itsResultList;
		//itsCallable.close();
	}//end of method

	/**
	 * This method adds a new Course information in the courses table.
	 * @param theDEPT_CODE
	 * @param theCOURSE
	 * @param theTITLE
	 * @param theCREDITS
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> addNewCourse(String theDEPT_CODE, String theCOURSE, String theTITLE, String theCREDITS) throws Exception
	{
		int aErrorCode=-1;
		//preparing to call procedure
		itsCallable = itsConn.prepareCall("begin studentreg.add_course(:1,:2,:3,:4,:5); end;");
		//setting the paramter values for the procedure
		itsCallable.setString(1, theDEPT_CODE);
		itsCallable.setInt(2, Integer.parseInt(theCOURSE));
		itsCallable.setString(3, theTITLE);
		itsCallable.setInt(4, Integer.parseInt(theCREDITS));
		itsCallable.registerOutParameter(5, Types.INTEGER);
		//executing update
		System.out.println("ADDING INFO");
		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(5);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		return itsResultList;
		//itsCallable.close();
	}//end of method

/**
	 * This method Update a Course information in the courses table.
	 * @param theDEPT_CODE
	 * @param theCOURSE
	 * @param theTITLE
	 * @param theCREDITS
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> UpdateCourse(String theDEPT_CODE, String theCOURSE,String theNEWDEPT_CODE, String theNEWCOURSE, String theTITLE, String theCREDITS) throws Exception
	{
		int aErrorCode=-1;
		//preparing to call procedure
		itsCallable = itsConn.prepareCall("begin studentreg.update_course(:1,:2,:3,:4,:5,:6,:7); end;");
		//setting the paramter values for the procedure
		itsCallable.setString(1, theDEPT_CODE);
		itsCallable.setInt(2, Integer.parseInt(theCOURSE));
		itsCallable.setString(3, theNEWDEPT_CODE);
		itsCallable.setInt(4, Integer.parseInt(theNEWCOURSE));
		itsCallable.setString(5, theTITLE);
		itsCallable.setInt(6, Integer.parseInt(theCREDITS));
		itsCallable.registerOutParameter(7, Types.INTEGER);
		//executing update
		System.out.println("ADDING INFO");
		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(7);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		return itsResultList;
		//itsCallable.close();
	}//end of method


	/**
	 * This method adds a new student information in the students table.
	 * @param theSID
	 * @param theSNAME
	 * @param theSTATUS
	 * @param theGPA
	 * @param theEMAIL
	 * @param theDEPTNAME
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> addNewStudent(String theSID, String theSNAME, String theSTATUS, String theGPA, String theEMAIL, String theDEPTNAME) throws Exception
	{
		int aErrorCode=-1;
		//preparing to call procedure
		itsCallable = itsConn.prepareCall("begin studentreg.set_students(:1,:2,:3,:4,:5,:6,:7); end;");
		//setting the paramter values for the procedure
		itsCallable.setString(1, theSID);
		itsCallable.setString(2, theSNAME);
		itsCallable.setString(3, theSTATUS);
		itsCallable.setDouble(4, new Double(theGPA).doubleValue());
		itsCallable.setString(5, theEMAIL);
		itsCallable.setString(6, theDEPTNAME);
		itsCallable.registerOutParameter(7, Types.INTEGER);
		//executing update
		System.out.println("ADDING INFO");
		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(7);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		return itsResultList;
		//itsCallable.close();
	}//end of method

	/**
	 * This method gives the courses information of given student.
	 * @param theSID
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getStudentCoursesInfo(String theSID) throws Exception
	{
		int aErrorCode=-1;
		//prepare to call procedure
		itsCallable = itsConn.prepareCall("begin studentreg.check_sid(:1,:2); end;");

		itsCallable.registerOutParameter(2, Types.INTEGER);
		itsCallable.setString(1, theSID);

		itsCallable.executeQuery();

		aErrorCode = itsCallable.getInt(2);
		itsResultList = new ArrayList<String>();

		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{

			itsCallable = itsConn.prepareCall("begin ? := studentreg.get_stdrec(:2,:3); end;");
			//registering out paramters
			itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
			//setting input parameter
			itsCallable.setString(2, theSID);
			//registering out parameter
			itsCallable.registerOutParameter(3, Types.INTEGER);
			//executing the procedure
			itsCallable.executeQuery();
			// getting result set
	        ResultSet rs = (ResultSet)itsCallable.getObject(1);
	        //creating output list
	        aErrorCode = itsCallable.getInt(3);
			System.out.println(aErrorCode);
			PreparedStatement aStmt = itsConn.prepareStatement("select sid,sname from students where sid=?");
			//String aStr = "select sid,sname from students where sid='"+theSID+"'";
			aStmt.setString(1, theSID);
			ResultSet aResultSet = aStmt.executeQuery();
			if(aResultSet.next())
			{
				itsResultList.add(aResultSet.getString(1)+" | "+aResultSet.getString(2));
			}

			aStmt.close();
			aResultSet.close();

			if(aErrorCode!=0)
			{
				itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
			}
			else
			{
				int i=0;
		        while (rs.next())
		        {

	        		itsResult = rs.getString(1) + " | "+rs.getString(2)+" | "+rs.getString(3);
		        	itsResultList.add(itsResult);

		        }//end of while
		        rs.close();
			}//end of else
		}//end of else
        itsCallable.close();
        return itsResultList;
	}//end of method

	/**
	 * This method gives the prerequisite information about the given course with dept name
	 * and course number.
	 * @param theDeptcode
	 * @param theCourse
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getPreRequisiteCoursesList(String theDeptcode, String theCourse) throws Exception
	{
		itsResultList = new ArrayList<String>();
		showprereq(theDeptcode, theCourse);
		return itsResultList;
	}//end of method

	/**
	 * This is the recursive call to the recursive Procedure
	 * @param theDeptcode
	 * @param theCourse
	 * @throws Exception
	 */
	public void showprereq(String theDeptcode,String theCourse) throws Exception
	{
		//int aErrorNo = -1;
		itsCallable = itsConn.prepareCall("begin ?:=studentreg.show_prereq(?,?,?); end;");
         //register the out parameter (the second parameter)
		itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
        //set the in parameter (the first parameter)
		itsCallable.setString(2, theDeptcode);
		itsCallable.setString(3, theCourse);
		itsCallable.registerOutParameter(4, Types.INTEGER);
        //execute the store procedure
		itsCallable.executeQuery();

        ResultSet rs = (ResultSet)itsCallable.getObject(1);
        //get the out parameter result.
         while (rs.next()) {

        	 itsResultList.add(rs.getString(1)+" | "+rs.getString(2));
            //System.out.println(rs.getString(1) + "\t" +
            //    rs.getString(2));
                showprereq(rs.getString(1),rs.getString(2));
        }//end of while
        rs.close();
        itsCallable.close();
	}//end of method

	/**
	 * This method gives the details about a given class ID.
	 * @param theCID
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> getClassDetailsList(String theCID)throws Exception
	{
		int aErrorCode=-1;
		//prepare to call procedure
		itsCallable = itsConn.prepareCall("begin studentreg.check_cid(:1,:2); end;");

		itsCallable.registerOutParameter(2, Types.INTEGER);
		itsCallable.setString(1, theCID);

		itsCallable.executeQuery();
		itsResultList = new ArrayList<String>();
		aErrorCode = itsCallable.getInt(2);
		itsResultList = new ArrayList<String>();
		if(aErrorCode!=0)
		{
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		}
		else
		{
			itsCallable = itsConn.prepareCall("begin ? := studentreg.get_class_details(:2,:3); end;");
			//registering out paramter
			itsCallable.registerOutParameter(1, OracleTypes.CURSOR);
			//setting input parameter
			itsCallable.setString(2, theCID);
			//registering out paramter
			itsCallable.registerOutParameter(3,  Types.INTEGER);
			//executing procedure and getting result set
			itsCallable.executeQuery();
	        aErrorCode = itsCallable.getInt(3);
			System.out.println(aErrorCode);
			PreparedStatement aStmt = itsConn.prepareStatement("select cid,title from classes c1, courses c2 where c2.dept_code=c1.dept_code and c2.course#=c1.course# and c1.cid=?");
			//String aStr = "select sid,sname from students where sid='"+theSID+"'";
			aStmt.setString(1, theCID);
			ResultSet aResultSet = aStmt.executeQuery();
			if(aResultSet.next())
			{
				itsResultList.add(aResultSet.getString(1)+" | "+aResultSet.getString(2));
			}

			aStmt.close();
			aResultSet.close();

			if(aErrorCode!=0)
			{
				itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
			}
			else
			{
				ResultSet rs = (ResultSet)itsCallable.getObject(1);
		        while (rs.next())
		        {
		        	itsResult = rs.getString(1) + " | "+rs.getString(2);
		        	itsResultList.add(itsResult);
		        }//end of while
		        rs.close();
			}
		}
        itsCallable.close();
        return itsResultList;
	}//end of method

	/**
	 * This method enrolls a student in the given class
	 * @param theSID
	 * @param theCID
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> enrollStudent(String theSID, String theCID)throws Exception
	{
		//preparing to call procedure
		int aErrorCode=-1;
		itsCallable = itsConn.prepareCall("begin studentreg.enroll_students(:1,:2,:3); end;");
		//setting input parameters
		itsCallable.setString(1, theSID);
		itsCallable.setString(2, theCID);
		itsCallable.registerOutParameter(3, Types.INTEGER);

		//executing
		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(3);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		itsCallable.close();
		return itsResultList;
	}

	public List<String> deleteCourse(String theDEPT_CODE, String theCOURSE) throws Exception
		{
			int aErrorCode = -1;
			itsCallable = itsConn.prepareCall("begin studentreg.delete_course(:1,:2,:3); end;");
			itsCallable.setString(1, theDEPT_CODE);
			itsCallable.setString(2, theCOURSE);
			itsCallable.registerOutParameter(3, Types.INTEGER);

			itsCallable.executeUpdate();

			aErrorCode = itsCallable.getInt(3);
			System.out.println(aErrorCode);
			itsResultList = new ArrayList<String>();
			itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
			itsCallable.close();
			return itsResultList;
	}

	public List<String> dropClass(String theSID, String theCID) throws Exception
	{
		int aErrorCode = -1;
		itsCallable = itsConn.prepareCall("begin studentreg.drop_class(:1,:2,:3); end;");
		itsCallable.setString(1, theSID);
		itsCallable.setString(2, theCID);
		itsCallable.registerOutParameter(3, Types.INTEGER);

		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(3);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		itsCallable.close();
		return itsResultList;
	}

	public List<String> deleteStudent(String theSID)throws Exception
	{
		int aErrorCode = -1;
		itsCallable = itsConn.prepareCall("begin studentreg.remove_students(:1,:2); end;");
		itsCallable.setString(1, theSID);
		itsCallable.registerOutParameter(2, Types.INTEGER);
		itsCallable.executeUpdate();

		aErrorCode = itsCallable.getInt(2);
		System.out.println(aErrorCode);
		itsResultList = new ArrayList<String>();
		itsResultList.add(itsErrorObj.getErrorMessage(aErrorCode+""));
		itsCallable.close();
		return itsResultList;
	}

	public void closeConnection()
	{
		try
		{
			itsConn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
