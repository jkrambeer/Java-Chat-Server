import java.io.*;
import java.net.*;

public class TextMessage extends ChatMessage
{
	/*
		Joseph Krambeer
		12/13/14

		A class that is a subclass of ChatMessage that models a message
		that contains text that is suppost to appear on another person's
		server.

		Class Variables:
			serialVersionUID
				State to associate the serialized object with
				a certain version to make sure the sender and
				recipient have the same versions of the class
				to enusure that they are compatible. The
				serialUID for this object is always 1.

			text
				The text of the message that will be sent to the
				recipient. Will most likely be displayed on their
				server(so don't say anything too naughty).

		Constructors:
			TextMessage(InetSocketAddress,String,InetSocketAddress,String)
				Calls ChatMessage's constructor with the sender's server, their name
				and then the recipient server. Initializes the text with the last
				String input.

		Methods:
			public String getText()
				returns the String stored in the text instance variable.

			public ResponseMessage runRequest(Socket socket)
				Prints out a message in the format of Name says text. It then
				tries to update the server's addressBook with the ip of the
				chatClient that sent this message. Returns an EverythingOk
				response.

		Modification History:
			December 10, 2014
				Original Version
	*/

	private static final long serialVersionUID = 1;
	private String            text;

	public TextMessage(InetSocketAddress senderChatServer, String senderName, InetSocketAddress recipientChatServer, String text)
	{
		super(senderChatServer,senderName,recipientChatServer);
		this.text = text;
	}//TextMessage

	public String getText()
	{
		return this.text;
	}//getText

	public ResponseMessage runRequest(Socket socket)
	{
		System.out.println(this.getSenderName()+" says: "+this.getText() );
		AddressBook.getInstance().update(this.getSenderName(),this.getSenderChatServer());//updating AddressBook
		return new EverythingOk();
	}//runRequest

}//class