import java.io.*;
import java.net.*;

public class TextMessageWithEcho extends TextMessage
{
	/*
		Joseph Krambeer
		12/13/14

		A class that extends TextMessage that in addition to doing as
		TextMessage does also sends a TextMessage to your own server.

		Class Variables:
			serialVersionUID
				State to associate the serialized object with
				a certain version to make sure the sender and
				recipient have the same versions of the class
				to enusure that they are compatible. The
				serialUID for this object is always 1.

		Constructors:
			TextMessageWithEcho(InetSocketAddress,String,InetSocketAddress,String)
				Calls TextMessage's constructor with the passed parameters.

		Methods:
			public ResponseMessage send()
				An implementation of send that sends the object that calls this
				to calling ChatClient's server first then calls the ChatClients send
				method to send the object to the recipient and get a response from
				their server.

		Modification History:
			December 10, 2014
				Original Version
	*/

	private static final long serialVersionUID = 1;

	public TextMessageWithEcho(InetSocketAddress senderChatServer, String senderName,InetSocketAddress recipientChatServer, String text)
	{
		super(senderChatServer,senderName,recipientChatServer,text);
	}//TextMessage


	public ResponseMessage send() throws ClassNotFoundException, IOException
	{

		Socket              connection;
		ObjectInputStream   in;
		ObjectOutputStream  out;
		ResponseMessage     response;

		connection = new Socket(getSenderChatServerIP(),getSenderChatServerPortNumber());

		out = new ObjectOutputStream(connection.getOutputStream());
		out.writeObject( this );

		in  = new ObjectInputStream(connection.getInputStream());

		response = (ResponseMessage)in.readObject();//we don't care about what our server sends back

		out.close();
		in.close();
		connection.close();

		return super.send();

	}//send


}//class