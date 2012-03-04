import java.util.*;
public class ErrorMessage
{
	public Map<String,String> itsHashMap = null;

	public ErrorMessage()
	{
		itsHashMap = new HashMap<String, String>();
		initMap();
	}

	public void initMap()
	{
		itsHashMap.put("0","!! Record Updated Sucessfully.");
		itsHashMap.put("1","!! No Data Found.");
		itsHashMap.put("2","!! SID Not Valid.");
		itsHashMap.put("3", "!! CID Not Valid.");
		itsHashMap.put("4", "!! The student has not been enrolled in any course.");
		itsHashMap.put("5", "!! No Student enrolled in class.");
		itsHashMap.put("6", "!! Student Already in class.");
		itsHashMap.put("7", "!! Student cannont be enrolled in more than 4 classes.");
		itsHashMap.put("8", "!! Class is Closed");
		itsHashMap.put("9", "!! Prequesite not done.");
		itsHashMap.put("10", "!! Student not enrolled in class.");
		itsHashMap.put("11", "!! The class has now no students.");
		itsHashMap.put("12", "!! Student is not enrolled in any class.");
		itsHashMap.put("13", "!! Class now has no students / student not enrolled in classes.");
		itsHashMap.put("14", "!! Email ID already exists.");
		itsHashMap.put("15", "!! Student already exists.");
		itsHashMap.put("18", "!! Invalid course id. ");
		itsHashMap.put("19", "!! Course Information is invalid. ");
		itsHashMap.put("20", "!! No student has been enrolled in the course. ");
		itsHashMap.put("21", "!! This course is already in the table. ");
		itsHashMap.put("22", "!! The credit of a course can either be 3 or 4. ");
		itsHashMap.put("23", "!! This class is already in the table. ");
		itsHashMap.put("24", "!! Entry already exist. ");
		itsHashMap.put("25", "!! Updated a course with full information. ");
	}

	public String getErrorMessage(String theErrorCode)
	{
		return itsHashMap.get(theErrorCode);
	}
}
