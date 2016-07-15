import java.net.*;
import java.io.*;
import java.util.*;
public class Server implements Runnable
{
	/*
		Joseph Krambeer
		12/13/14

		Class Variables:
			portNumber
				An integer that represents the port
				that is being used on the current
				computer to run this server and that
				is being used to comunicate on.

			serverLog
				creates a Log object for the object
				so that it can use the Log objects
				log methods to log events on the
				server.

			status
				A ServerStatistics object that holds
				whether the server is currently running
				and the counts of each things such as
				valid/invalid messages recieved.

		Constructors:
			Server(int,Log)
				initializes the server's log with the passed
				Log and its portNumber is set to the passed int.
				Then the server creates a new ServerStatistics
				to use as its status.

		Methods:
			public static void main (String[] args)
				runs to create a new server on port 17416 and with
				a new Log object. Then it runs the Threadable Server
				object it created.

			public ServerStatistics getServerStatistics()
				returns the status instance variable that the current
				server has.

			public void run()
				implements the runnable interface for the server so it
				is threadable. It creates a new serverSocket on which the
				server talks and while its status is true the server accepts
				clients and threads these connections out with
				new ConnectionHandlers.

			public synchronized boolean serverIsRunning()
				returns the state of the ServerStatistics objec the server
				has and whether the boolean is true or false.

			public synchronized ServerStatistics stopServer()
				this method stops the server by setting its running status to
				false and then pings the server to move it off the IOBlock and
				which will then allow it to see the while loop is false. This
				will stop the server from running until to restarted manually.

			private void writeLogMessage(String input)
				calls the server's Log object to log the string inputted,

			private void updateServerStatistics(ServerStatistics.StatisticsType type)
				updates the serverStatistics based on the type of enum that is
				passed to the function. It calls functions in ServerStatistics
				to update the ServerStatistic's count[].

			public void exceptionCaught(String message, Exception exception)
				this methods prints out on the server a message and tells
				about the exception that was caught.

		Modification History:
			December 10, 2014
				Original Version

	*/

public enum ServerCommand
{
   /*
	  A enum that sets the types of commands the
	  server can recieve and execute, such as
	  getting the server's ServerStatistics or
	  stopping the server.
   */

  StopServer
  {
   @Override
   public ServerStatistics execute ( Server server ) { return server.stopServer(); }
  },
  Statistics
  {
   @Override
   public ServerStatistics execute ( Server server ) { return (ServerStatistics) server.status.clone(); }
  };
  // no constructor required because the enum has no state variables
  abstract public ServerStatistics execute ( Server server );
}//serverCommand

	private int portNumber;
	private Log serverLog;
	private ServerStatistics status;

	public Server(int portNumber,Log serverLog)
	{
		if(serverLog==null){throw new IllegalArgumentException("The serverLog passed is null");}
		if(portNumber<0)   {throw new IllegalArgumentException("PortNumber has to be greater than or equal to zero.");}

		this.portNumber = portNumber;
		this.serverLog  = serverLog;
		this.status     = new ServerStatistics();
	}//server constructor

	public static void main (String[] args)
	{
		Server me;

		System.out.println("I am the server");
		me = new Server(17416,new Log());
		me.run();

	}//main

	public ServerStatistics getServerStatistics()
	{
		return this.status.clone();
	}//getServerStatistics

	public void run()
	{
		try
		{
			Socket            client;
			ObjectInputStream input;
			ServerSocket      theServer;

			theServer = new ServerSocket(this.portNumber);//create serverSocket
			this.status.setServerIsRunning(true);

			while(status.serverIsRunning() )
			{
				client = theServer.accept();//InputBlock
				if(status.serverIsRunning())
				{
					try
					{
						//wait for connection
						new Thread(new ConnectionHandler(client)).start();
					}//try
					catch(Exception e){writeLogMessage(e.getMessage());}//catch
				}//if
			}//while
		}//try
		catch(Exception e)
		{
			writeLogMessage(e.getMessage());
		}//failed in creating serverSocket

	}//run

	public synchronized boolean serverIsRunning()
	{
		return this.status.serverIsRunning();
	}//serverIsRunning

	public synchronized ServerStatistics stopServer()
	{
		try
		{
			if(this.status.serverIsRunning())
			{
				InetAddress server;
				Socket ping;

				this.status.setServerIsRunning(false);
				server = InetAddress.getLocalHost();
				ping   = new Socket(server,17416);
			}//if
		}//try
		catch(Exception e){}
		return this.status.clone();
	}//stopServer

	private void writeLogMessage(String input)
	{
		this.serverLog.log(input);
	}//writeLogMessage

	private void updateServerStatistics(ServerStatistics.StatisticsType type)
	{
		this.status.incrementCount(type);
	}//updateServerStatistics

	public void exceptionCaught(String message, Exception exception)
	{
		writeLogMessage("An exception was caught in: "+message);
		writeLogMessage("Exception message is: "+exception.getMessage());
	}//exceptionCaught



private class ConnectionHandler implements Runnable
{
	/*
		A runnable object that is used to make threads to
		handle connections to the server and process their
		requests.

		Class Variables:
			socket
				A socket that is the socket that the server
				is currently using to communicate with the
				client. This is initialized by being passed
				a reference to that socket.

		Constructors:
			ConnectionHandler(Socket)
				initializes socket by putting the passed Socket
				object into socket as a reference to the Socket
				being used to communicate between the server
				and the client.

		Methods:
			public synchronized void run()
				Implementation of the runnable interface that allows
				this to be threaded. This implements the request/response
				communcation of the server as it reads in an object and
				tries to cast it as different object types to determine
				what to do with it. Based on how that goes it will send
				back a response to the client that it has a connection with.
	*/

	private Socket socket;

	public ConnectionHandler(Socket socket)
	{
		this.socket=socket;
	}//connectionHandler constructor


	public synchronized void run()
	{
	    CommandMessage       commandMessage;
	    ObjectInputStream    input;
	    Message              message;
	    ObjectOutputStream   output;
	    ResponseMessage      response;
	    Server.ServerCommand serverCommand;

	    input    = null;  // required by compiler
	    output   = null;  // required by compiler
	    response = null;  // required by compiler and used as a flag
	    try
	    {
	     input   = new ObjectInputStream ( this.socket.getInputStream() );
	     message = ( Message ) input.readObject();

	     try
	      {
	       response = ((RequestMessage) message).runRequest(this.socket);
		   Server.this.updateServerStatistics(ServerStatistics.StatisticsType.ValidMessageCount);
	      }
	     catch ( ClassCastException cce )
	     {
	      try
	      {
		   ServerStatistics stats;

	       commandMessage = (CommandMessage) message;
		   Server.this.updateServerStatistics(ServerStatistics.StatisticsType.CommandMessageCount);


	       System.out.println("Received this command: " + commandMessage.getCommand());

		   stats    = commandMessage.getCommand().execute(Server.this);
	       response = new ServerStatisticsResponseMessage(stats,0, "this is the command response message", false );

	      } // try
	      catch ( ClassCastException cce1 )
	      {
			Server.this.updateServerStatistics(ServerStatistics.StatisticsType.InvalidMessageCount);
	      } // catch
	     }// catch

	    }  // try
	    catch ( ClassCastException cce ) { Server.this.exceptionCaught("ConnectionHandler", cce); } // object read could not be cast as Message
	    catch ( Exception e )            { Server.this.exceptionCaught("ConnectionHandler", e); }  // something else happened. maybe they closed the socket
	    finally
	    {
	     try
	     {
	      output = new ObjectOutputStream(this.socket.getOutputStream());
	      output.writeObject(response);
	     } // try
	     catch ( Exception e ) {Server.this.exceptionCaught("ConnectionHandler finally method response processing", e); }
	     //Cleaning up IO streams
	     try { input.close();                         } catch(Exception e) {}
	     try { output.close();                        } catch(Exception e) {}
	     try { this.socket.getInputStream().close();  } catch(Exception e) {}
	     try { this.socket.getOutputStream().close(); } catch(Exception e) {}
	     try { this.socket.close();                   } catch(Exception e) {}
	    }
   }  // run

}//connectionHandler Class

}//server Class