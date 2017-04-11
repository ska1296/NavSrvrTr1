import java.sql.*;
import java.io.*;
public class Date_a_Base {
	public static void main(String ars[])throws IOException
	{
		InputStreamReader read=new InputStreamReader(System.in);
		BufferedReader in=new BufferedReader(read);

		System.out.println("chanel name");
		String channelName=in.readLine();

		try
		{
			Class.forName("com.mysql.jdbc.Driver");//loads driver
			String url="jdbc:mysql://localhost/mydb?user=root&password=qwerty";
			Connection cn=DriverManager.getConnection(url); //connection established
			
			String query="select * from GlobetrorDB where channel_name='"+channelName+"'";
			java.sql.Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next())
			{
				String strChannel =rs.getString("channel_name");
				System.out.println("In channelName "+strChannel);
				String strLatitude = rs.getString("latitude");
				System.out.println("In Latitude "+strLatitude);
				String strLongitude = rs.getString("longitude");
				System.out.println("In Longitude "+strLongitude);
			}
			st.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}