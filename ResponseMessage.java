import java.io.*;
import java.net.*;

public interface ResponseMessage extends Message
{
	/*
		Joseph Krambeer
		12/13/14

		An interface that is a type of message that gives
		the client a response on how their sent object was
		handled and if any errors occured. Can run a response
		for the client.

		Methods:
			public int getErrorCode()
				returns an int that represents what kind of
				error occurred. -1 for now is a generic error
				and 0 is no error.

			public String getMessage()
				returns a message that is associated with a
				certain implmentation of this interface.

			public boolean isError()
				returns whether an error occured or not.

			public void runResponse()
				runs a response of some sort for the client
				after they have recieved this message.

		Modification History:
			December 10, 2014
				Original Version
	*/

	public int getErrorCode();
	public String getMessage();
	public boolean isError();
	public void runResponse();

}//class