import java.io.*;
import java.net.*;

public interface CommandMessage extends Message
{
	/*
		Joseph Krambeer
		12/13/14

		An interface that is a message that prompts
		the server to execute of the the commands that
		is in the server's ServerCommand.

		Methods:
			public Server.ServerCommand getCommand()
				returns the command type that the implentation
				has so the server can figure out what command
				it should execute.

			public String getSecurityCode()
				returns a security code that can be checked by
				the server to make sure only those who know the
				code can send the server commands.

		Modification History:
			December 10, 2014
				Original Version
	*/

	public Server.ServerCommand getCommand();

	public String getSecurityCode();

}//class