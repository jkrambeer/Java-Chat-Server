import java.io.*;
import java.net.*;
public class ServerStatisticsResponseMessage extends ServerStatistics implements Serializable,ResponseMessage
{
	/*
		Joseph Krambeer
		12/13/14

		Class Variables:
			errorCode
				sets the error code for this class's implementation
				of ResponseMessage.

			isError
				sets whether or not this contained an error for this
				class's implementation of ResponseMessage.

			responseMessage
				sets the reponseMessage for this class's implmentation
				of ResponseMessage.

			serialVersionUID
				State to associate the serialized object with
				a certain version to make sure the sender and
				recipient have the same versions of the class
				to enusure that they are compatible. The
				serialUID for this object is always 1.

		Constructors:
			ServerStatisticsResponseMessage(ServerStatistics,int,String,boolean)
				this calls the second constructor in ServerStatistics to
				put the passed ServerStatistics into this object's state
				and then sets the errorCode with the int, the message with
				the String, and the isError with the boolean passed.

		Methods:
			public void runResponse()
				print out some arbitrary string at the
				moment but could be implemented to do
				other stuff later on.

			public int getErrorCode()
				returns the value in the errorCode instance
				variable.

			public String getMessage()
				returns the value in the message instance
				variable.

			public boolean isError()
				returns the value in the isError instance
				variable.

		Modification History:
			December 10, 2014
				Original Version

	*/

	private int               errorCode;
	private boolean           isError;
	private String            responseMessage;
	private static final long serialVersionUID = 1;

	public ServerStatisticsResponseMessage(ServerStatistics statistics, int errorCode, String responseMessage, boolean isError)
	{
		super(statistics);
		this.errorCode       = errorCode;
		this.responseMessage = responseMessage;
		this.isError         = isError;
	}//constructor

	public void runResponse()
	{
		System.out.println("ServerStatsResponseMsg Ran Request");
	}//runResponse

	public int getErrorCode()
	{
		return this.errorCode;
	}//getErrorCode

	public String getMessage()
	{
		return this.responseMessage;
	}//getMessage

	public boolean isError()
	{
		return this.isError;
	}//isError

}