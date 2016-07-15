import java.net.*;
import java.io.*;
public interface RequestMessage extends Message
{
	/*
		Joseph Krambeer
		12/13/14

		An interface that is a message which sends a
		request to the server and has the server execute
		that request.

		Methods:
			public ResponseMessage runRequest(Socket socket)
				Has the server do the particular request
				that the implementation of this interface has.

		Modification History:
			December 10, 2014
				Original Version
	*/
public ResponseMessage runRequest(Socket socket);

}//class