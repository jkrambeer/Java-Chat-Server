import java.io.*;
import java.net.*;
public class AddressBookUpdateMessage implements Serializable,ResponseMessage,RequestMessage
{
	/*
	 	Joseph Krambeer
		12/13/14

		A class that implements both response/request message
		that can be sent to the client's server and be sent
		back with the server's addressBook to update the client's
		addressBook

		Class Variables:
			serversAddressBook
				A copy of the server's addressBook

		Constructors:
			AddressBookUpdateMessage()
				sets the addressBook variable as null

		Methods:
			public ResponseMessage runRequest(Socket socket)
				sets the server's copy of addressBook as its
				own copy of addressBook and sends itself back
				to the sender.

			public int getErrorCode()
				returns 0 as there were no errors

			public String getMessage()
				returns an arbitrary message linked to this class

			public boolean isError()
				returns false as, once more, there were no errors

			public void runResponse()
				when called in the chatClient it trys to use the keys and
				InetSocketAddresses linked to those keys to update the
				client's addressBook

		Modification History
			December 10, 2014
				Original Version
	*/


	private AddressBook serversAddressBook;

	public AddressBookUpdateMessage()
	{
		this.serversAddressBook=null;//not needed but if a constructor is need this is nice to have
	}//constructor

	public ResponseMessage runRequest(Socket socket)
	{
		this.serversAddressBook = AddressBook.getInstance();
		return this;
	}//runRequest

 	public int getErrorCode()
 	{
		return 0;
	}//getErrorCode

	public String getMessage()
	{
		return "AddressBookUpdater";
	}//getMessage

	public boolean isError()
	{
		return false;
	}//isError

	public void runResponse()
	{
		String [] keys;
		keys = this.serversAddressBook.getKeys();
		for(int i=0;i<keys.length;i++)
		{
			AddressBook.getInstance().update( keys[i],this.serversAddressBook.get(keys[i]) );
		}
	}//runResponse

}//class