import java.io.*;
import java.util.*;
public class Log
{
	/*
		Joseph Krambeer
		12/13/14

		Methods:
			public void log(String message)
				This prints the current date/time and then
				the message that is to be logged onto the
				server.

		Modification History:
			December 10, 2014
				Original Version

	*/

public void log(String message)
{
	Calendar now;

	now = Calendar.getInstance();

	//start println
	System.out.println(            ""
	  +now.get(now.YEAR)         + ","
	  +now.get(now.MONTH)        + ","
	  +now.get(now.DAY_OF_MONTH) + " "
	  +now.get(now.HOUR_OF_DAY)  + ":"
	  +now.get(now.MINUTE)       + ":"
	  +now.get(now.SECOND)       + "."
	  +now.get(now.MILLISECOND)  + " "+message);
	//end println

}//log

}//class