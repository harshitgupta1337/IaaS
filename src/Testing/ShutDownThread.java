package Testing;

import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.xerox.amazonws.ec2.Jec2;

public class ShutDownThread extends Thread 
{
	private String _vmName;
	private String _username;
	public ShutDownThread(String vmName, String username) 
	{
		_vmName = vmName;
		_username = username;
	}
	public void run()
	{
		String AWSAccessKeyId = "4TDKTOQ8BEOKC4KH8ZCXV";
		String SecretAccessKey = "ythx9u3XekMuZZrV5Cw7Yd4hpLF5ta8vCqVjJPfX";
		try
		{
			Jec2 connection = new Jec2(AWSAccessKeyId, SecretAccessKey, false, "10.14.79.194", 8773);
			connection.setResourcePrefix("/services/Eucalyptus"); 
			connection.setSignatureVersion(1);
		
			
			MongoClient client;
			client = new MongoClient("127.0.0.1", 27017);
			DB database = client.getDB("iaas");
			DBCollection users = database.getCollection("users");
			List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", _username)).get("instance");
			
			for(DBObject instance : instances)
			{
				if(instance.get("name").equals(_vmName))
				{
					connection.terminateInstances(Arrays.asList(instance.get("instanceID").toString()));
					
					while(true)
					{
						sleep(10000);
						if(connection.describeInstances(Arrays.asList(instance.get("instanceID").toString())).get(0).getInstances().get(0).getState().equals("terminated"))
						{
							connection.terminateInstances(Arrays.asList(instance.get("instanceID").toString()));
							break;
						}
					}
					instance.put("instanceID", "null");
					break;
				}
			}
			users.update(new BasicDBObject("username", _username), new BasicDBObject("$set", new BasicDBObject("instance", instances)));
			client.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/*
	public static void main(String args[])
	{
		ShutDownThread thread = new ShutDownThread("i-08EA402F");
		thread.start();
	}
	 */
}
