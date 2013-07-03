package Testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.bson.NewBSONDecoder;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.xerox.amazonws.ec2.InstanceType;
import com.xerox.amazonws.ec2.Jec2;
import com.xerox.amazonws.ec2.LaunchConfiguration;
import com.xerox.amazonws.ec2.ReservationDescription;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Spark;
import spark.Route;
import spark.Response;
import spark.Request;

public class Login 
{
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		Spark.get(new Route("/")
		{

			@Override
			public Object handle(Request arg0, Response arg1) {
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");

				try 
				{
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("Login.html");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					template.process(helloMap, writer);		
					return writer;
				} 
				catch (Exception e) 
				{	
					e.printStackTrace();

					return e.getMessage();
				}
				
			}
			
		});
		Spark.post(new Route("/checkLogin")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				// TODO Auto-generated method stub
				if(request.queryParams("username").equals("admin") && request.queryParams("password").equals("admin"))
					response.redirect("/admin");
				try 
				{
					MongoClient client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					DBCursor cursor = users.find(new BasicDBObject("username", request.queryParams("username")).append("password", request.queryParams("password"))); 
					if(cursor.count()==1)
					{
						if(cursor.next().get("status").toString().equals("approved"))
						{
							response.redirect("/account/"+request.queryParams("username"));
							return null;
						}
						
						else
							return "Your account has not yet been approved. If it is urgent, mail the admin about it.";
					}
					else
						return "Incorrect Login. Please check Username and Password.";
					
				} 
				catch (Exception e) 
				{
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});

		Spark.get(new Route("/admin")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");
				
				try 
				{
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("AdminHome.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					template.process(helloMap, writer);		
					return writer;
				} 
				catch (Exception e) 
				{	
					e.printStackTrace();

					return e.getMessage();
				}
				
			}
			
		});

		Spark.get(new Route("/admin/accounts")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");
				
				try 
				{
					MongoClient client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					
					List<DBObject> pendingUsers = users.find(new BasicDBObject("status","pending")).toArray();
					
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("AdminRequests.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					helloMap.put("pendingUsers", pendingUsers);
					template.process(helloMap, writer);		
					client.close();
					return writer;
				} 
				catch (Exception e) 
				{	
					e.printStackTrace();

					return e.getMessage();
				}
				
			}
			
		});
		Spark.get(new Route("/signup/:bool")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");
				
				try 
				{
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("SignUp.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					
					if(request.params(":bool").equals("true"))
						helloMap.put("status", "");
					else
						helloMap.put("status", "Username already taken");
					
					template.process(helloMap, writer);		
					return writer;
				} 
				catch (Exception e) 
				{	
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		Spark.post(new Route("/signUpComplete")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				String username = request.queryParams("username");
				String password = request.queryParams("password");
				String name = request.queryParams("name");
				
				
				try 
				{
					MongoClient client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					if(users.find(new BasicDBObject("username", username)).size()>0)
					{
						response.redirect("/signup/false");
						return null;
					}
					users.insert(new BasicDBObject("username", username).append("password", password).append("name", name).append("status", "pending").append("email", request.queryParams("email")).append("instance", new ArrayList()));
					final Configuration configuration = new Configuration();
					configuration.setClassForTemplateLoading(Login.class, "/");
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
					
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("SignUpComplete.html");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					template.process(helloMap, writer);
					client.close();
					return writer;
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		Spark.post(new Route("/confirmAccount/:username")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				MongoClient client;
				try 
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					users.update(new BasicDBObject("username", request.params(":username")), new BasicDBObject("$set", new BasicDBObject("status", "approved")));
					client.close();
					response.redirect("/admin/accounts");
					return null;
				}
				catch (Exception e) 
				{
					e.printStackTrace();

					return e.getMessage();
				}
				
			}
			
		});
		Spark.post(new Route("/rejectAccount/:username")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				MongoClient client;
				try 
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					users.remove(new BasicDBObject("username", request.params(":username"))); 
					response.redirect("/admin");
					return null;
				}
				catch (Exception e) 
				{
					e.printStackTrace();

					return e.getMessage();
				}
				
			}
			
		});
		Spark.get(new Route("/account/:username")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");

				try 
				{
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("UserHome.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					helloMap.put("username", request.params("username"));
					template.process(helloMap, writer);		
					return writer;
				} 
				catch (Exception e) 
				{	
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		
		Spark.get(new Route("/account/:username/requestVM")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");

				try 
				{
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("UserRequestVM.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					helloMap.put("username", request.params("username"));
					template.process(helloMap, writer);		
					return writer;
				} 
				catch (Exception e) 
				{	
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		Spark.post(new Route("/account/:username/vmRequested")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				MongoClient client;
				try 
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					DBObject instance = new BasicDBObject();
					String name = request.queryParams("name");
					
					List<DBObject> instances = (List<DBObject>)(users.findOne(new BasicDBObject("username", request.params("username"))).get("instance"));
					if(instances != null)
					{
						for(DBObject instanceIterator : instances)
						{
							if(instanceIterator.get("name").equals(name))
								return "This machine name has already been taken. Please enter another name.";
						}
					}
					if((request.queryParams("Hardware")).toString().equals("m1.small"))
						instance = new BasicDBObject("emi", request.queryParams("OperatingSystem").substring(0, request.queryParams("OperatingSystem").indexOf('#'))).append("hardwareConfig", new BasicDBObject("CPU", 1).append("RAM", 128).append("DISK", 2)).append("status", "pendingForAdmin").append("OS", request.queryParams("OperatingSystem").substring(request.queryParams("OperatingSystem").indexOf('#')+1)).append("type", "m1.small").append("name", name).append("instanceID", "null");
					else if((request.queryParams("Hardware")).toString().equals("c1.medium"))
						instance = new BasicDBObject("emi", request.queryParams("OperatingSystem").substring(0, request.queryParams("OperatingSystem").indexOf('#'))).append("hardwareConfig", new BasicDBObject("CPU", 1).append("RAM", 256).append("DISK", 5)).append("status", "pendingForAdmin").append("OS", request.queryParams("OperatingSystem").substring(request.queryParams("OperatingSystem").indexOf('#')+1)).append("type", "c1.medium").append("name", name).append("instanceID", "null");
					else if((request.queryParams("Hardware")).toString().equals("m1.large"))
						instance = new BasicDBObject("emi", request.queryParams("OperatingSystem").substring(0, request.queryParams("OperatingSystem").indexOf('#'))).append("hardwareConfig", new BasicDBObject("CPU", 2).append("RAM", 512).append("DISK", 10)).append("status", "pendingForAdmin").append("OS", request.queryParams("OperatingSystem").substring(request.queryParams("OperatingSystem").indexOf('#')+1)).append("type", "m1.large").append("name", name).append("instanceID", "null");
					else if((request.queryParams("Hardware")).toString().equals("m1.xlarge"))
						instance = new BasicDBObject("emi", request.queryParams("OperatingSystem").substring(0, request.queryParams("OperatingSystem").indexOf('#'))).append("hardwareConfig", new BasicDBObject("CPU", 2).append("RAM", 1024).append("DISK", 20)).append("status", "pendingForAdmin").append("OS", request.queryParams("OperatingSystem").substring(request.queryParams("OperatingSystem").indexOf('#')+1)).append("type", "m1.xlarge").append("name", name).append("instanceID", "null");
					else if((request.queryParams("Hardware")).toString().equals("c1.xlarge"))
						instance = new BasicDBObject("emi", request.queryParams("OperatingSystem").substring(0, request.queryParams("OperatingSystem").indexOf('#'))).append("hardwareConfig", new BasicDBObject("CPU", 4).append("RAM", 2048).append("DISK", 20)).append("status", "pendingForAdmin").append("OS", request.queryParams("OperatingSystem").substring(request.queryParams("OperatingSystem").indexOf('#')+1)).append("type", "c1.xlarge").append("name", name).append("instanceID", "null");
					
					
					users.update(new BasicDBObject("username", request.params("username").toString()), new BasicDBObject("$addToSet", new BasicDBObject("instance", instance)));
					
					client.close();
					
					response.redirect("/account/"+request.params("username")+"/vmRequestSuccessful");
					
					return null;
				}
				catch (Exception e) 
				{
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		Spark.get(new Route("/account/:username/vmRequestSuccessful")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");

				try 
				{
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("VMRequested.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					helloMap.put("username", request.params(":username"));
					template.process(helloMap, writer);		
					return writer;
				} 
				catch (Exception e) 
				{	
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		Spark.get(new Route("/admin/vms")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				MongoClient client;
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");
				try 
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					AggregationOutput aggregate = users.aggregate(new BasicDBObject("$unwind", "$instance"), new BasicDBObject("$match", new BasicDBObject("instance.status", "pendingForAdmin")));
					List<DBObject> pendingVMs = new ArrayList<DBObject>();
					for(DBObject object : (List<DBObject>)aggregate.getCommandResult().get("result"))
					{
						String name = object.get("name").toString();
						String username = object.get("username").toString();
						String OS = ((DBObject)object.get("instance")).get("OS").toString();
						String RAM = ((DBObject)((DBObject)object.get("instance")).get("hardwareConfig")).get("RAM").toString();
						String CPU = ((DBObject)((DBObject)object.get("instance")).get("hardwareConfig")).get("CPU").toString();
						String DISK = ((DBObject)((DBObject)object.get("instance")).get("hardwareConfig")).get("DISK").toString();
						String vmName = ((DBObject)object.get("instance")).get("name").toString();
						pendingVMs.add(new BasicDBObject("name", name).append("OS", OS).append("RAM", RAM).append("CPU", CPU).append("DISK", DISK).append("vmName", vmName).append("username", username));
					}
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
				
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("AdminVMs.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					helloMap.put("vms", pendingVMs);
					template.process(helloMap, writer);
					client.close();
					return writer;
					
				}
				catch(Exception e)
				{
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		Spark.post(new Route("/confirmVM/:username/:vmName")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				String username = request.params(":username");
				String vmName = request.params(":vmName");
				MongoClient client;
				try 
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", username)).get("instance");
					int i=0, index = 0;
					//System.out.println(vmName);

					for(DBObject instance : instances)
					{
						if(instance.get("name").toString().equals(vmName) && instance.get("status").equals("pendingForAdmin"))
						{
							instance.put("status", "approved");
						}
					}
					users.update(new BasicDBObject("username", username), new BasicDBObject("$set", new BasicDBObject("instance", instances)));
					client.close();
					response.redirect("/admin/vms");
					return null;
				}
				catch(Exception e)
				{
					e.printStackTrace();

					return e.getMessage();
				}
				
			}
		});
		Spark.post(new Route("/rejectVM/:username/:vmName")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				String username = request.params(":username");
				String vmName = request.params(":vmName");
				MongoClient client;
				try 
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", username)).get("instance");
					int i = 0, index = 0;
					for(DBObject instance : instances)
					{
						if(instance.get("name").toString().equals(vmName) && instance.get("status").equals("pendingForAdmin"))
						{
							index = i;
						}
						i++;
					}
					instances.remove(index);
					users.update(new BasicDBObject("username", username), new BasicDBObject("$set", new BasicDBObject("instance", instances)));
					client.close();
					response.redirect("/admin/vms");
					return null;
				}
				catch(Exception e)
				{
					e.printStackTrace();

					return e.getMessage();
				}
				
			}
			
		});
		Spark.get(new Route("/account/:username/listVMs")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				final Configuration configuration = new Configuration();
				configuration.setClassForTemplateLoading(Login.class, "/");
				String username = request.params(":username");
				MongoClient client;
				try 
				{
					String AWSAccessKeyId = "4TDKTOQ8BEOKC4KH8ZCXV";
					String SecretAccessKey = "ythx9u3XekMuZZrV5Cw7Yd4hpLF5ta8vCqVjJPfX";
					Jec2 connection = new Jec2(AWSAccessKeyId, SecretAccessKey, false, "10.14.79.194", 8773);
					connection.setResourcePrefix("/services/Eucalyptus"); 
					connection.setSignatureVersion(1);
					
					
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", username)).get("instance");
					List<DBObject> approvedInstances = new ArrayList<DBObject>();
					//List<String> runningInstances = new ArrayList<String>();
					//List<String> statusOfRunningInstances = new ArrayList<String>();
					/*for(DBObject instance : instances)
					{
						if(!(instance.get("instanceID").toString().equals("null")))
							runningInstances.add(instance.get("instanceID").toString());
					}*/
					
					//if(runningInstances.size()>0)
					//{
						/*
						String AWSAccessKeyId = "4TDKTOQ8BEOKC4KH8ZCXV";
						String SecretAccessKey = "ythx9u3XekMuZZrV5Cw7Yd4hpLF5ta8vCqVjJPfX";
						Jec2 connection = new Jec2(AWSAccessKeyId, SecretAccessKey, false, "10.14.79.194", 8773);
						connection.setResourcePrefix("/services/Eucalyptus"); 
						connection.setSignatureVersion(1);
						*/
						/*List<ReservationDescription> reservationDescriptions = connection.describeInstances(runningInstances);
						for(ReservationDescription resDesc : reservationDescriptions)
						{
							statusOfRunningInstances.add(resDesc.getInstances().get(0).getState().toUpperCase());
						}
						*/
					//}
					
					//int i=0;
					for(DBObject instance : instances)
					{
						if(instance.get("status").toString().equals("approved"))
						{
							String name = instance.get("name").toString();
							String status = "";
							if(instance.get("instanceID").toString().equals("null"))
								status = "Ready";
							else
							{
								//status = statusOfRunningInstances.get(i);
								//CHANGED BECAUSE OF UNORDEREDNESS OF LIST OF VMS
								status = connection.describeInstances(Arrays.asList(instance.get("instanceID").toString())).get(0).getInstances().get(0).getState();
								//i++;
							}
							approvedInstances.add(new BasicDBObject("name", name).append("status", status.toLowerCase()));
						}
					}
					configuration.setDirectoryForTemplateLoading(new File("./Resources"));
					
					final StringWriter writer = new StringWriter();
					Template template = null;
					template = configuration.getTemplate("UserListVMs.ftl");
					
					Map<String, Object> helloMap = new HashMap<String, Object>();
					helloMap.put("approvedVMs", approvedInstances);
					helloMap.put("username", username);
					template.process(helloMap, writer);		
					client.close();
					return writer;
				}
				catch(Exception e)
				{
					e.printStackTrace();

					return e.getMessage();
				}
			}
			
		});
		Spark.post(new Route("/account/:username/bootVM/:vmName")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				String AWSAccessKeyId = "4TDKTOQ8BEOKC4KH8ZCXV";
				String SecretAccessKey = "ythx9u3XekMuZZrV5Cw7Yd4hpLF5ta8vCqVjJPfX";
				MongoClient client;
				String username = request.params(":username").toString();
				try
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", username)).get("instance");
					
					for(DBObject instance : instances)
					{
						if(instance.get("name").equals(request.params(":vmName")))
						{
							Jec2 connection = new Jec2(AWSAccessKeyId, SecretAccessKey, false, "10.14.79.194", 8773);
							connection.setResourcePrefix("/services/Eucalyptus"); 
							connection.setSignatureVersion(1);
							
							LaunchConfiguration lc = new LaunchConfiguration(instance.get("emi").toString());
							lc.setKeyName("mykey");
							lc.setInstanceType(InstanceType.valueOf(instance.get("type").toString()));
							if(instance.get("instanceID").toString().equals("null"))
							{
								ReservationDescription resDesc =  connection.runInstances(lc);
								instance.put("instanceID", resDesc.getInstances().get(0).getInstanceId().toString());
							}
							else
							{
								connection.startInstances(Arrays.asList(instance.get("instanceID").toString()));
							}
							break;
							
						}
					}
					users.update(new BasicDBObject("username", username), new BasicDBObject("$set", new BasicDBObject("instance", instances)));
					response.redirect("/account/"+username+"/listVMs");
					return "No Such VM Found";
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return e.getMessage();
				}

				
			}
			
		});
		
		Spark.post(new Route("/account/:username/deleteVM/:vmName")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				MongoClient client;
				String username = request.params(":username").toString();
				try
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", username)).get("instance");
					List<DBObject> newInstances =  new ArrayList<DBObject>();
					System.out.println(request.params(":vmName"));
					for(DBObject instance : instances)
					{
						if(!(instance.get("name").equals(request.params(":vmName"))))
						{
							newInstances.add(instance);
						}
					}
					users.update(new BasicDBObject("username", username), new BasicDBObject("$set", new BasicDBObject("instance", newInstances)));
					
					response.redirect("/account/"+username+"/listVMs");
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return e.getMessage();
				}
				return null;
			}
			
		});
		Spark.post(new Route("/account/:username/shutDownVM/:vmName")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				try
				{
					ShutDownThread thread = new ShutDownThread(request.params(":vmName"), request.params(":username"));
					thread.start();
					response.redirect("/account/"+request.params("username")+"/listVMs");
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return e.getMessage();
				}
				return null;
			}
			
		});
		Spark.get(new Route("/account/:username/vmDetails/:vmName")
		{

			@Override
			public Object handle(Request request, Response response) 
			{
				String username = request.params(":username").toString();
				String vmName = request.params(":vmName").toString();
				
				MongoClient client;
				
				try
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", username)).get("instance");
					System.out.println(request.params(":vmName"));
					for(DBObject instance : instances)
					{
						if(instance.get("name").equals(request.params(":vmName")))
						{
							String instanceID = instance.get("instanceID").toString();
							
							String OS = instance.get("OS").toString();
							String RAM = ((DBObject)instance.get("hardwareConfig")).get("RAM").toString();
							String CPU = ((DBObject)instance.get("hardwareConfig")).get("CPU").toString();
							String DISK = ((DBObject)instance.get("hardwareConfig")).get("DISK").toString();
							String IP = "Not Available";
							String loginUsername = "Not Available";
							String privateKey = "Not Available";
							List<String> privateKeyLines = new ArrayList<String>();
							if(!(instanceID.equalsIgnoreCase("null")))
							{
								if(instance.get("emi").toString().equalsIgnoreCase("emi-FCCA3D9E"))
								{
									loginUsername = "ubuntu";
								}
								String AWSAccessKeyId = "4TDKTOQ8BEOKC4KH8ZCXV";
								String SecretAccessKey = "ythx9u3XekMuZZrV5Cw7Yd4hpLF5ta8vCqVjJPfX";
								Jec2 connection = new Jec2(AWSAccessKeyId, SecretAccessKey, false, "10.14.79.194", 8773);
								connection.setResourcePrefix("/services/Eucalyptus"); 
								connection.setSignatureVersion(1);
								
								IP = connection.describeInstances(Arrays.asList(instanceID)).get(0).getInstances().get(0).getIpAddress();
								privateKey = "";
								BufferedReader br =  new BufferedReader(new InputStreamReader(new FileInputStream(new File("mykey.private"))));
								while(true)
								{
									String str = br.readLine();
									if(str.length()==0)
										break;
									privateKeyLines.add(str);
									System.out.println(str);
								}
								br.close();
							}
							final Configuration configuration = new Configuration();
							configuration.setClassForTemplateLoading(Login.class, "/");
							configuration.setDirectoryForTemplateLoading(new File("./Resources"));
							
							final StringWriter writer = new StringWriter();
							Template template = null;
							template = configuration.getTemplate("VMDetails.ftl");
							
							Map<String, Object> helloMap = new HashMap<String, Object>();
							helloMap.put("vmName", vmName);
							helloMap.put("os", OS);
							helloMap.put("ram", RAM);
							helloMap.put("cpu", CPU);
							helloMap.put("disk", DISK);
							helloMap.put("ip", IP);
							helloMap.put("loginName", loginUsername);
							helloMap.put("PrivateKey", privateKey);
							helloMap.put("lines", privateKeyLines);
							
							template.process(helloMap, writer);		
							client.close();
							return writer;
						}
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return e.getMessage();
				}
				return null;
			}
			
		});
		Spark.post(new Route("/account/:username/terminal/:vmName")
		{
			@Override
			public Object handle(Request request, Response response) 
			{
				String username = request.params(":username").toString();
				String vmName = request.params(":vmName").toString();
				
				MongoClient client;
				
				try
				{
					client = new MongoClient("127.0.0.1", 27017);
					DB database = client.getDB("iaas");
					DBCollection users = database.getCollection("users");
					List<DBObject> instances = (List<DBObject>) users.findOne(new BasicDBObject("username", username)).get("instance");
					System.out.println(request.params(":vmName"));
					for(DBObject instance : instances)
					{
						if(instance.get("name").equals(request.params(":vmName")))
						{
							String instanceID = instance.get("instanceID").toString();
							String AWSAccessKeyId = "4TDKTOQ8BEOKC4KH8ZCXV";
							String SecretAccessKey = "ythx9u3XekMuZZrV5Cw7Yd4hpLF5ta8vCqVjJPfX";
							Jec2 connection = new Jec2(AWSAccessKeyId, SecretAccessKey, false, "10.14.79.194", 8773);
							connection.setResourcePrefix("/services/Eucalyptus"); 
							connection.setSignatureVersion(1);
							
							String IP = connection.describeInstances(Arrays.asList(instanceID)).get(0).getInstances().get(0).getIpAddress();
							
							response.redirect("http://"+IP+":4200/");
						}
					}
					return null;
				}
				catch(Exception e)
				{
					return e.getMessage();
				}
			}
		});
		
	}

}
