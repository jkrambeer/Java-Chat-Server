import java.io.*;
import java.net.*;

abstract public class ChatMessage implements Serializable, RequestMessage
{
	/*
		Joseph Krambeer
		12/13/14

		An abstract class to model text messages that will be
		sent from a chatclient to others servers.

		Class Variables:
			serialVersionUID
				State to associate the serialized object with
				a certain version to make sure the sender and
				recipient have the same versions of the class
				to enusure that they are compatible. The
				serialUID for this object is always 1.

			recipientChatServer
				The ip and port number of the chat server that
				will recieve the message being modelled.

			senderChatServer
				The ip and port number of the chat server that
				the message was sent from.

			senderName
				A string that is the name of the client who
				sent the message.

		Constructors:
			ChatMessage(InetSocketAddress,String,InetSocketAddress)
				Makes a new ChatMessage object that hold
				references to the reciepient/sender port and
				ip as well as the sender name.

		Methods:
			public ResponseMessage getOKResponse()
				returns an EverythingOk object that tells that
				the everything went sucessful in sending.

			public InetSocketAddress getRecipientChatServer()
				returns the InetSocketAddress of the server
				recieving the ChatMessage.

			public String getRecipientChatServerIP()
				returns the ip of the recipient in the form
				of a string.

			public int getRecipientChatServerPortNumber()
				returns the port number of the recipient's
				server.

			public String getSenderName()
				returns a String that is the name of the
				person who sent the message.

			public InetSocketAddress getSenderChatServer()
				returns the InetSocketAddress of the sender
				of the ChatMessage object.

			public String getSenderChatServerIP()
				returns the ip of the sender in the form
				of a string .

			public int getSenderChatServerPortNumber()
				returns the port number of the sender.

			public ResponseMessage send()
				sends the object that calls the method
				and returns the response message recieved
				back from the server the ChatMessage was
				sent to.

			public ResponseMessage processResponse(ResponseMessage responseMessage)
				process the response of the inputted
				responseMessage and returns that object.

			public String toString()
				puts all the state of the class into a
				string to make debugging easier.

		Modification History
			December 10, 2014
				Original Version
	*/

	private static final long serialVersionUID = 1;
	private InetSocketAddress recipientChatServer;
	private InetSocketAddress senderChatServer;
	private String            senderName;


	public ChatMessage(InetSocketAddress senderChatServer, String senderName,InetSocketAddress recipientChatServer)
	{
		this.recipientChatServer=recipientChatServer;
		this.senderChatServer=senderChatServer;
		this.senderName=senderName;
	}//ChatMessage


	public ResponseMessage getOKResponse()
	{
		return new EverythingOk();//Make a new class to make an appropriate response
	}//getOKResponse

	public InetSocketAddress getRecipientChatServer()
	{
		return this.recipientChatServer;
	}//getRecipientChatServer

	public String getRecipientChatServerIP()
	{
		int     loc;
		String  address;

		address = ""+recipientChatServer.getAddress();
		loc     = address.indexOf("/");
		//find where the / is because it indicates where the start
		//of the ip number

		return address.substring(loc+1);
	}//getRecipientChatServerIP

	public int getRecipientChatServerPortNumber()
	{
		return recipientChatServer.getPort();
	}//getRecipientChatServerPortNumber

	public String getSenderName()
	{
		return this.senderName;
	}//getSenderName

	public InetSocketAddress getSenderChatServer()
	{
		return this.senderChatServer;
	}//getSenderChatServer

	public String getSenderChatServerIP()
	{
		int     loc;
		String  address;

		address = ""+senderChatServer.getAddress();
		loc     = address.indexOf("/");

		return address.substring(loc+1);
	}//getSenderChatServerIP

	public int getSenderChatServerPortNumber()
	{
		return senderChatServer.getPort();
	}//getSenderChatServerPortNumber

	public ResponseMessage send() throws IOException, ClassNotFoundException
	{
		Socket              connection;
		ObjectOutputStream  out;
		ObjectInputStream   in;
		ResponseMessage     response;

		connection = new Socket(getRecipientChatServerIP(),getRecipientChatServerPortNumber());

		out= new ObjectOutputStream(connection.getOutputStream());
		out.writeObject( this );

		in=new ObjectInputStream(connection.getInputStream());

		response = (ResponseMessage)in.readObject();
		processResponse(response);

		out.close();
		in.close();
		connection.close();

		return getOKResponse();
	}//send

	public ResponseMessage processResponse(ResponseMessage responseMessage)
	{
		responseMessage.runResponse();
		return responseMessage;
	}//returns the passed responseMessage


	public String toString()
	{
		return ""+this.recipientChatServer+" "+this.senderChatServer+" "+this.senderName;
	}//toString


public class EverythingOk implements ResponseMessage,Serializable
{
	/*
	 	A class that implements ReponseMessage
	 	representing that nothing went wrong in
	 	sending and that no errors occured.
	 	It is able to sent as it is serializable.

	 	Methods:
	 		public int getErrorCode()
	 			returns 0 indicating that there
	 			were no errors.

	 		public String getMessage()
	 			returns a response saying that
	 			everything executed ok.

	 		public boolean isError()
	 			returns false as not errors occured.

	 		public void runResponse()
	 			prints out that its response was ran.
	 			This method does not really do much.
	*/


	public int getErrorCode()
	{
		return 0;
	}//getErrorCode

	public String getMessage()
	{
		return "Server says: Everything OK.";
	}//getMessage

	public boolean isError()
	{
		return false;
	}//isError

	public void runResponse()
	{
		System.out.println("Response Ran");
	}//runResponse

}//EverythingOk

}//class