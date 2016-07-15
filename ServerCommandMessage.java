import java.io.*;
import java.net.*;
public class ServerCommandMessage implements Serializable,CommandMessage
{
	/*
		Joseph Krambeer
		12/13/14

		A class that is a type of commandMessage that is sent
		to a server to have it execute a certain command.

		Class Variables:
			serialVersionUID
				State to associate the serialized object with
				a certain version to make sure the sender and
				recipient have the same versions of the class
				to enusure that they are compatible. The
				serialUID for this object is always 1.

			command
				an enum type that is a command on the Server that
				tells the server what kind of command this message
				wants it to execute.

			securityCode
				an String that contains a security code that can
				be checked by the server before running the command
				requested by this message.

		Constructors:
			ServerCommandMessage(Server.ServerCommand command,String securityCode)
				Constructs this object with the command enum for the server and
				the sercurity code inputted in the object creation.

		Methods:
			public Server.ServerCommand getCommand()
				returns the command used to construct the object

			public String getSecurityCode()
				returns the securityCode used to construct the object

		Modification History:
			December 10, 2014
				Original Version


	*/

	private static final long    serialVersionUID = 1;
	private Server.ServerCommand command;
	private String               securityCode;

	public ServerCommandMessage(Server.ServerCommand command,String securityCode)
	{
		this.command      = command;
		this.securityCode = securityCode;
	}//constructor

	public Server.ServerCommand getCommand()
	{
		return this.command;
	}//getCommand

	public String getSecurityCode()
	{
		return this.securityCode;
	}//getSecurityCode

}//class