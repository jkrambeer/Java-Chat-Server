import java.io.*;
import java.util.*;
public class ServerStatistics implements Cloneable,Serializable
{
	/*
		Joseph Krambeer
		12/13/14

		A class to keep track of message counts and
		tell if the server is currently running.

		Class Variables:
			serverIsRunning
				A boolean that represents whether the server
				is running or not. Used to control when the
				server will run its main functions in the while
				loop contained in the Server class.

			count
				A int[] that has a length equal to the StatisticsType
				enums. Each value in this array is refered with a certain
				enum and is representative of a value of something like
				validMessageCount.

			serialVersionUID
				State to associate the serialized object with
				a certain version to make sure the sender and
				recipient have the same versions of the class
				to enusure that they are compatible. The
				serialUID for this object is always 1.

		Constructors:
			ServerStatistics()
				This contructor sets the server running as false
				as first as when this object will be made for
				the server the server will need to be set to
				be running to actually start. The count array
				is set to be the length of the enums in this
				class's enum.

			ServerStatistics(ServerStatistics)
				A constructor used by ServerStatisticResponseMessage
				to put the passed ServerStatistics into the
				ServerStatisticResponseMessage own state.

		Methods:
			public ServerStatistics clone()
				Implements the clonable interface on ServerStatics by
				trying to class super.clone() to create a shallow copy
				of the object that called the clone method. Throws a
				RunTimeException if cloning is not supported. Although
				not as interesting as sheep cloning, this method gets
				the job done.

			public boolean serverIsRunning()
				returns the boolean value that represents whether the
				server is running or not.

			public void setServerIsRunning(boolean serverIsRunning)
				sets the instance variable representing whether or not
				the server is running to the passed value. The method
				is called in process to start up/shut down the server.

			public int getCount(StatisticsType type)
				returns the value in count[] for the passed statistics type
				by calling the enum class's getCount method.

			public int incrementCount(StatisticsType type)
				adds 1 to a value in count[] which is acessed based
				on the passed StatisticsType and then returns the
				value of the incremented value.

			private int[] getCounts()
				returns a reference to count[]

		Modification History:
			December 10, 2014
				Original Version
	*/

private boolean           serverIsRunning;
private int[]             count;
private static final long serialVersionUID = 1;

public ServerStatistics()
{
	this.serverIsRunning = false;
	this.count           = new int[ServerStatistics.StatisticsType.values().length];
}//default constructor

public ServerStatistics(ServerStatistics statistics)
{
	this.serverIsRunning = statistics.serverIsRunning();
	this.count           = statistics.getCounts();
}//constructor for subclass

public ServerStatistics clone()
{
	try
	{
	 ServerStatistics clone;
	 clone = (ServerStatistics)super.clone();
	 return clone;
 	}
 	catch(CloneNotSupportedException cnse)
 	{
		throw new RuntimeException(cnse.getMessage());
	}
}//clone

public boolean serverIsRunning()
{
	return this.serverIsRunning;
}//serverIsRunning

public void setServerIsRunning(boolean serverIsRunning)
{
	this.serverIsRunning=serverIsRunning;
}//setServerIsRunning

public int getCount(StatisticsType type)
{
	return type.getCount(this);
}//getCount

public int incrementCount(StatisticsType type)
{
	return type.incrementCount(this);
}//incrementCount

private int[] getCounts()
{
	return this.count;
}//getCounts

public enum StatisticsType
{
	/*
		An enum class that associates certain types
		of counts to the count array in ServerStatistics.

		Class Variables:
			arrayIndex
				This associates a certain enum with a
				index to access count[] in ServerStatistics.
				Example: ExceptionCount is the fourth element
				in count[] and thus its index is 3 to access
				its place in count[].

		Constructors:
			StatisticsType(int)
				Creates the enums with values associated with
				them to use as indexs to associated certain
				places in count[] with specific count types.
				This has no visibility as the compiler does not
				like this having visibility so therefore I left
				it out of the constructor.

		Methods:
			private int getIndex()
				returns the integer index a certain enum is
				associated with.

			public int getCount(ServerStatistics serverStatistics)
				returns the value held in a specific value of
				count[] based on the enum type that is used to
				reference this method.

			public int incrementCount(ServerStatistics serverStatistics)
				adds one to a certain count within count[] based
				on the enum type that called this method and returns
				that incremented value.
	*/

	CommandMessageCount(0),
 	ExceptionCount     (3),
 	InvalidMessageCount(2),
 	ValidMessageCount  (1);

    private int arrayIndex;

    StatisticsType(int arrayIndex)
    {
		this.arrayIndex = arrayIndex;
	}//constructor

	private int getIndex()
	{
		return this.arrayIndex;
	}//getIndex

	public int getCount(ServerStatistics serverStatistics)
	{
		return serverStatistics.count[getIndex()];
	}//getCount

	public int incrementCount(ServerStatistics serverStatistics)
	{
		serverStatistics.count[getIndex()]=serverStatistics.count[getIndex()]+1;
		return serverStatistics.count[getIndex()];
	}//incrementCount


}//StatisticsType


}//class