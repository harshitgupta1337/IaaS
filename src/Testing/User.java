package Testing;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark; 

public class User 
{
	public static void main(String args[])
	{
		Spark.get(new Route("god/:name/home")
		{

			@Override
			public Object handle(Request arg0, Response arg1) {
				// TODO Auto-generated method stub
				return arg0.params(":name");
			}
			
		});
		
	}
}
