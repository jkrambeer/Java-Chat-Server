import java.util.*;
import java.net.*;
import java.io.*;
public class AddressBook implements Serializable
{
	/*
 		Joseph Krambeer
		12/13/14

		A class that models an AddressBook with names
		being keys to get InetSocketAddress of servers
		linked with those names.

		Class Variables:
			serialVersionUID
				State to associate the serialized object with
				a certain version to make sure the sender and
				recipient have the same versions of the class
				to enusure that they are compatible. The
				serialUID for this object is always 1.

			instance
				a new AddressBook object that is static so that
				one thing can only have one AddressBook at a time.

			addressBook
				a HashMap that uses strings a keys to retrieve
				InetSocketAddresses that are linked to servers.

		Constructors:
			private AddressBook()
				creates a new HashMap for the addressBook instance
				variable. Is private so the class implements singleton
				structure.

		Methods:
			public static AddressBook getInstance()
				returns the static instance of the AddressBook created for
				the method that holds the AddressBook

			public static AddressBook newInstance()
				calls getInstance()

			private String convertToKey(String name)
				returns the inputted string trimmed and changed
				to lowercase.

			public synchronized InetSocketAddress get(String name)
				gets the InetSocketAddress mapped to the
				inputted name. Synchronized to make sure that
				the AddressBook's values are being stored and
				accessed only one at a time.

			public synchronized String[] getKeys()
				returns a String[] that contains all the keys an
				instance of the addressBook has. Synchronized to
				make sure that the AddressBook's values are being stored
				and accessed only one at a time.

			private void put(String name,InetSocketAddress address)
				puts the name as a key in the hashmap and associates
				it with the passed InetSocketAddress.


			public synchronized void update(String name,InetSocketAddress address)
				checks if the address or name is already used and takes
				appropriate action if one or either is true and then
				might update the hashmap by calling put(). Synchronized to
				make sure that the AddressBook's values are being stored
				and accessed only one at a time.

			private HashMap<String,InetSocketAddress> getAddressBook()
				returns a reference to the hashmap this instance of the
				addressBook contains

		Modification History:
			December 10, 2014
				Original Version


	*/

	private static final long                 serialVersionUID = 1;
	private static AddressBook                instance = new AddressBook();
	private HashMap<String,InetSocketAddress> addressBook;

	public static AddressBook getInstance()
	{
		return AddressBook.instance;
	}//getInstance

	public static AddressBook newInstance()
	{
		return AddressBook.getInstance();
	}//newInstance

	private AddressBook()
	{
		this.addressBook = new HashMap<String,InetSocketAddress>();
	}//constructor

	private String convertToKey(String name)
	{
		return name.trim().toLowerCase();
	}//convertToKey

	public synchronized InetSocketAddress get(String name)
	{
		return addressBook.get(convertToKey(name));
	}//get

	public synchronized String[] getKeys()
	{
		String[] keys = addressBook.keySet().toArray(new String[0]);
		for(int i=0;i<keys.length;i++)
		{
			keys[i] = convertToKey(keys[i]);
		}
		return keys;
	}//getKeys

	private void put(String name,InetSocketAddress address)
	{
		addressBook.put(name,address);
	}//put

	public synchronized void update(String name,InetSocketAddress address)
	{
		String originalName;

		name         = convertToKey(name);
		originalName = name;

		if(name != null && address != null)
		{
			if(!addressBook.containsValue(address))
			{
				for(int i=1;addressBook.containsKey(name);i++)
				{
					name = originalName+i;
				}//loops until a name is unique
				put(name,address);
			}//if address is not in values
		}//if value not null
	}//update

	private HashMap<String,InetSocketAddress> getAddressBook()
	{
		return this.addressBook;
	}//getAddressBook

}//class