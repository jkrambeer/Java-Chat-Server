import java.io.*;
import java.net.*;

public class ChatClient
{
  /*
    Joseph Krambeer
	12/13/14

	A class that allows the user to communcite
	with others servers through multiple commands

	Class Variables:
		myName
			A string that holds the name that the
			ChatClient was made with.

		myServerAddress
			A InetSocketAddress that contains the
			ip and portnumber on which the chatClient
			are currently running.

		addressBookUpdaterThread
			A reference to a thread that creates
			AddressBookUpdaterMessages to go and
			update the AddressBook of the chatClient.

	Constructors:
		ChatClient(String,int)
			A public constructor that puts gets the
			local ip of the chatServer and the portnumber
			entered to create the InetSocketAddress, puts
			the name made into the name of the ChatClient,
			and creates a thread that when stared goes
			out over and interval to update the AddressBook.

	Methods:
	 	public String getMyName()
	 		returns a String that is the name used to initialize
	 		the name variable of the ChatClient.

	 	public InetSocketAddress getMyServerAddress()
	 		returns a InetSocketAddress which is a combination of
	 		the portNumber used to create the ChatClient and the
	 		ip the current computer is being run on.

	 	public static void main(String[] args)
	 		the methods that creates a new ChatClient with the name
	 		and portNumber desired, updates the addressBook if
	 		necessary, starts the thread that updates the addressBook,
	 		and starts the method to read to chatClient commands.

	 	private Thread getAddressBookUpdaterThread()
	 		a private methods that returns the part  state of the ChatClient
	 		that is a reference to its updater thread so that the thread
	 		can be started.
  */

  private enum Command
  {
     /*
	   An enum  used to represent commands
	   The enums are: NoCommand,UknownCommand,
	   QUIT,STATISTICS,TEXT,UPDATE_ADDRESS_BOOK.
	   Each enum overrides an run() method to
	   run the desired command.

	   Class Variables:
	   		chatClient
	   			a chatClient object that is a
	   			reference to the current chatClient
	   			that is running these commands.

	   		messageData
	   			a string that is the message data
	   			to be send over with the message
	   			the commands create.

	   Constructor:
	   		private Command()
	   			initializes the instance variables
	   			to null.

	   Run methods for the enums:
	   		NoCommand
				Does nothing as no commandw as found.

	   		UnknownCommand
	   			Prints out that the command was unknown
	   			and therefore could not be executed.

	   		QUIT
	   			Shuts down the localServer associated with
	   			this chatClient and stops asking for commands
	   			on the client side.

	   		TEXT
	   			Sends a textMessage if the recipient of the message
	   			is the sender's own server and a textMessageWithEcho
	   			if the message is being sent elsewhere. Gets the
	   			InetSocketAddress of the recipient by checking the
	   			addressBook the client has and sends the second
	   			half of the user's input as the message.

	   		UPDATE_ADDRESS_BOOK
	   			Sends an addressBookUpdaterMessage to the server to
	   			update the client's addressBook with the any new
	   			enteries in the server's addressBook.

	   Other Methods:
	   		private static String readFromKeyboard ( String prompt )
	   			reads data from the keyboard and returns
	   			the read data in string form.

	   		private static String[] parseForCommand ( String userData, char commandPrefix )
	   			trys to format userinput into a command that is
	   			denoted with the commandPrefix. The userData is the
	   			command which this function will call if that is a
	   			valid command. Returns this in string[] with the
	   			format command and rest of string parameter

	   		public static String[] parseForMessage ( String userData )
	   			trys to send the input userdata as a message with the
	   			first part being a key for the addressBook to get the
	   			InetSocketAddress to send the message to. The last half
	   			is used to create a textMessage or a textMessage with echo.
	   			Returns a Sting[] formatted addressBook key and then message
	   			to be sent.

	   		public static void start( ChatClient chatClient, char commandPrefix)
	   			starts reading for commands from the user and formatting
	   			the userinput into commands to be executed.

	   		abstract public void run ()
	   			tells that each enum must have a run method overriding this run
	   			method.

	   		public ChatClient getChatClient()
	   			returns the chatClient object that is currently
	   			running.

	   		public void setChatClient(ChatClient chatClient)
	   			sets the instance variable of Command to the
	   			current chatClient that is running.

	   		public String getMessageData()
	   			returns the info stored in the messageData variable.

	   		public void setMessageData(String messageData)
	   			sets the info stored in the messageData variable.

			public ResponseMessage requestResponse(ChatMessage chatMessage)
				sends a ChatMessage to a server and returns the reponse
				that the server gives back in response to the ChatMessage
				being sent over.

			public ResponseMessage requestResponse(Message message)
				sends a Message to a server and returns the reponse
				that the server gives back in response to the Message
				being sent over.

		Modification History
			December 10, 2014
				Original Version
     */
   NoCommand
    {
     @Override
     public void run(){}  // user didn't enter anything.
    },//NoCommand
   UnknownCommand
    {
     @Override
     public void run(){ System.out.println("Unrecognized command."); }
    },//unknownCommand
   QUIT
    {
     @Override
     public void run()
     {
	  CommandMessage  serverCommand;
	  ResponseMessage response;

	  serverCommand = new ServerCommandMessage(Server.ServerCommand.StopServer, "");
	  response  =  requestResponse(serverCommand);
	  this.getChatClient().getAddressBookUpdaterThread().interrupt();

      System.out.println ("Press any key twice to exit.");
      try { System.in.read(); } catch ( IOException ioe ) {}
      //  keep the terminal window open
     }
    },//Quit
   STATISTICS
    {
     @Override
     public void run()
     {
      ServerStatisticsResponseMessage response;
      CommandMessage                  serverCommand;

      serverCommand = new ServerCommandMessage(Server.ServerCommand.Statistics, "");
      response      = ( ServerStatisticsResponseMessage )
	  (ServerStatisticsResponseMessage)this.requestResponse ( serverCommand );

      System.out.println("Server is running: " +response.serverIsRunning());
      for (ServerStatistics.StatisticsType stat : ServerStatistics.StatisticsType.values())
      {
       System.out.println("   " + stat.name() + ": " + stat.getCount (response ) );
      }
      //response.runResponse();
     }
    },//Statistics
   TEXT
    {
     @Override
     public void run()
     {
      ChatClient        client;
      TextMessage       message;
      InetSocketAddress recipient;
      ResponseMessage   response;
      String[]          userData;

      //  the user data is assumed to be in the form: to whitespace message
      //  the start method ensures it won't be zero length after trimming.
      userData = this.parseForMessage ( this.getMessageData() );

      //  get the server address for this person
      recipient = AddressBook.getInstance().get(userData[0]);
      if ( recipient == null ) { System.out.println ( userData[0] + " is not in the address book." ); }

      //  empty messages are not sent
      if ( recipient != null && userData[1].length() > 0 )
      {
       client   = this.getChatClient();
       if ( client.getMyServerAddress().equals(recipient) )
         {
           message  = new TextMessage ( client.getMyServerAddress(),
		   client.getMyName(), recipient, userData[1]);
         }
         else
         {
          message  = new TextMessageWithEcho (
		  client.getMyServerAddress(), client.getMyName(), recipient, userData[1]);
         }

          response = this.requestResponse ( message );
          //  eventually we will tell the response object to run itself but for now:
          if ( response.isError() )
		  System.out.println(response.getMessage());
          //response.runResponse();

       } // if ( recipient != null && userData[1].length() > 0 )

     }  // run
    },//TEXT
    UPDATE_ADDRESS_BOOK
    {
	 @Override
     public void run()
     {
		AddressBookUpdateMessage message;
		ResponseMessage          response;

		message  = new AddressBookUpdateMessage();
		response = requestResponse(message);
		response.runResponse();

	 }//run
	};//UPDATE_ADDRESS_BOOK


   //  a few static behaviors of the enum
   private static String readFromKeyboard ( String prompt )
   {
    String userData;
    userData = "";
    try
    {
     System.out.print( prompt );
     userData = new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
    catch ( IOException io ) { throw new RuntimeException("Command.readFromKeyboard: " + io.getMessage()); }
    return userData;
   }

   private static String[] parseForCommand ( String userData, char commandPrefix )
   {
    // Return a two element String array. Element zero is the text
    // version of the command. Element one is the remainder of the
    // String parameter. If the String parameter did not contain a
    // command, use either of the three default commands:
    //            NoCommand
    //            UnknownCommand
    //            Text

    int loc;

    //  force the String parameter into a proper command format
    userData = userData.trim();
    if ( userData.length()  == 0 )
    { userData =commandPrefix + Command.NoCommand.name(); }
    if ( userData.charAt(0) != commandPrefix ){ userData = commandPrefix + Command.TEXT.name() + " " + userData;      }

    userData = userData + " ";
    loc      = userData.indexOf(" ");
    return new String[]{ userData.substring(1, loc).trim(),userData.substring(loc+1).trim()};
   }  // parseForCommand

   public static String[] parseForMessage ( String userData )
   {
    //  return a two element String array. element zero contains the name of
    //  the person to whom the message will be sent. element one contains
    //  the message
    int loc;

    userData = userData + " ";
    loc      = userData.indexOf(" ");
    return new String[] { userData.substring(0, loc).trim(),userData.substring(loc+1).trim()};
   }

   public static void start( ChatClient chatClient, char commandPrefix)
   {
    String[]  userData;
    Command   commandFromUser;

    do
    {
     //  read from the user and convert their input into a command and message
     userData = Command.parseForCommand ( readFromKeyboard("> ").trim(),commandPrefix );

     // enums are case sensitve. the user enums in Command are in uppercase.
     // change the user data to be upper case for comparison
     try
      { commandFromUser = Command.valueOf ( userData[0].toUpperCase() ); }
     catch ( IllegalArgumentException iae )
      { commandFromUser = Command.UnknownCommand;}

     //  execute the command
     commandFromUser.setChatClient  ( chatClient );
     commandFromUser.setMessageData ( userData[1] );
     commandFromUser.run();
    }
    while ( commandFromUser != Command.QUIT );
   }

   private ChatClient chatClient;  // instance variables of command
   private String     messageData; // text entered by user

   private void Command() { this.chatClient = null; this.messageData = null; }//constructor for command

   // some non-static behaviors
   abstract public void run ();

   public  ChatClient      getChatClient  (){return this.chatClient;}
   private void            setChatClient  (ChatClient chatClient){this.chatClient = chatClient;}
   public  String          getMessageData (){return this.messageData;}
   private void            setMessageData (String messageData){this.messageData = messageData;}

   public  ResponseMessage requestResponse( ChatMessage chatMessage )
   {
    //  implement the request-response protocol for the passed messgage
    ResponseMessage response;
    try   { response = chatMessage.send(); }
    catch ( Exception e )  { response = new ExceptionRaisedResponse(e,"Exception raised while sending message"); }
    return response;
   }  // requestResponse

   public  ResponseMessage requestResponse( Message message )
   {
      //implements for message objects
   	  Serializable object;
      InetSocketAddress  inetSocketAddress;
      ObjectInputStream  input;
      ObjectOutputStream output;
      ResponseMessage    response;
      Socket             socket;

      input  = null;
      output = null;
      socket = null;

      inetSocketAddress = this.getChatClient().getMyServerAddress();
      try
      {
       socket  = new Socket ( inetSocketAddress.getHostString(),inetSocketAddress.getPort() );

        output  = new ObjectOutputStream ( socket.getOutputStream());
        output.writeObject(message);
        input   = new ObjectInputStream  ( socket.getInputStream() );

        object   = (Serializable)input.readObject();
        response = (ResponseMessage)object;

      }
       catch ( Exception e )  { response = new ExceptionRaisedResponse(e,"Exception raised while sending command"); }
       finally
       {
        try { input.close(); } catch (Exception e) {}
        try { output.close(); } catch (Exception e) {}
        try { socket.close(); } catch (Exception e) {}
       }
      return response;
   }// requestResponse


   //  an inner class ( a non-static nested class ) inside the enum
   private class ExceptionRaisedResponse implements ResponseMessage
   {
	 /*
	   	 A private class that create objects that tell
	   	 an exception was raised in sending/recieving
	   	 an object.

	   	 Class Variables:
	   	 	exception
	   	 		An exception object that is the
	   	 		exception type that caused this
	   	 		object to be created

	   	 	message
	   	 		The message that goes along with
	   	 		the error that caused this object
	   	 		to be made. Stores info on problem.

	   	 Constructor:
	   	 	ExceptionRaisedResponse (Exception,String)
	   	 		Constructors this object with the exception
	   	 		that caused the error and a message that goes
	   	 		along with the error.

	   	 Methods:
	   	 	public int getErrorCode()
	   	 		returns -1 indicating there was an error.

	   	 	public String getMessage()
	   	 		returns the String stored in the classes
	   	 		message variable plus the exception's own
	   	 		message in a String form.

	   	 	public boolean isError()
	   	 		returns true telling that there was an error.

	   	 	public void runResponse()
	   	 		prints out the message stored within the object created.
     */
    private Exception exception;
    private String    message;

    public ExceptionRaisedResponse ( Exception exception, String message)
    {
		this.exception = exception; this.message = message;
	}

     public int     getErrorCode() { return -1;   }
     public String  getMessage  () { return this.message + ": " + this.exception.getMessage(); }
     public boolean isError     () { return true; }
     public void    runResponse () { System.out.println (this.getMessage() ); }
   }  // inner class

  }  // enum

  private class AddressBookUpdater implements Runnable
  {
	/*
 		A class that is threadable that runs to
 		update the AddressBook of the ChatClient.

 		Class Variables:
 			updateIntervalInMilliseconds
 				An integer that represents the millisecond
 				delay between class to update the addressBook

 		Constructors:
 			AddressBookUpdater(int)
 				Creates the AddressBookUpdater with the passed
 				time interval integer.

 		Methods:
 			public void run()
 				sets the UPDATE_ADDRESS_BOOK enum's chatClient
 				as the chatClient that this method is running in.
 				While uninterrupted this updated the AddressBook
 				over the time interval passed and stops when
 				interrupted.

	*/
	  private int updateIntervalInMilliseconds;

	  public AddressBookUpdater(int updateIntervalInMilliseconds)
	  {
		  this.updateIntervalInMilliseconds=updateIntervalInMilliseconds;
	  }//constructor

	  public void run()
	  {
		  Command.UPDATE_ADDRESS_BOOK.setChatClient(ChatClient.this);
		  while(!Thread.currentThread().isInterrupted())
		  {
			try
			{
			  Command.UPDATE_ADDRESS_BOOK.run();
			  Thread.currentThread().sleep(updateIntervalInMilliseconds);
		  	}//try
		  	catch(InterruptedException ie){Thread.currentThread().interrupt();}
		  }//while
	  }//run
  }//AddressBookUpdater

  //  continuation of ChatClient class

  private String            myName;
  private InetSocketAddress myServerAddress;
  private Thread            addressBookUpdater;

  public ChatClient ( String myName, int serverPort ) throws UnknownHostException
  {
   this.myName             = myName;
   this.myServerAddress    = new InetSocketAddress (InetAddress.getLocalHost(), serverPort );
   this.addressBookUpdater = (new Thread ( new AddressBookUpdater(10000) ) );
  }

  public InetSocketAddress getMyServerAddress() { return this.myServerAddress; }
  public String            getMyName         () { return this.myName; }



  public static void main(String[] args) throws Exception
  {
   System.out.println("I am the chatClient");

   ChatMessage          chatMessage;
   ChatClient           client;
   ServerCommandMessage command;
   ResponseMessage      response;

   client = new ChatClient ( "Joey", 17416 );
   // put someone in the AddressBook
   //AddressBook.getInstance().update ( "Jake", new InetSocketAddress ("140.209.121.29", 17416 ) );
   //AddressBook.getInstance().update ( "Me"  , new InetSocketAddress (InetAddress.getLocalHost(), 17416 ) );
   client.getAddressBookUpdaterThread().start();
   Command.start(client, '.');
  }//main

  private Thread getAddressBookUpdaterThread()
  {
   	  return this.addressBookUpdater;
  }//getAddressBookUpdaterThread

}  // class Client