import java.awt.*; 
import java.awt.event.*; 
import java.net.*; 
import java.sql.*;
import java.io.*; 

import javax.swing.JOptionPane;

/**
 *
 * @author Shubh Ketan
 */

class AddLocation
{
	public static void main(String args[])throws IOException
	{
		int tep;
		Socket sock;
		ServerSocket ServerSock;
		try {
			ServerSock=new ServerSocket(3070); //Creates a server socket, bound to the specified port
			while (true) {
				sock=ServerSock.accept(); //Listens for a connection to be made to this socket and accepts it
				System.out.println("Connection created");
				while(true) 
				{ 				
					ObjectInputStream oisChannel=new ObjectInputStream(sock.getInputStream());
					String strChannel=oisChannel.readObject().toString();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		try
		{
			ServerSock=new ServerSocket(3070);
			//code to read data from database database and to transfer data to 'ReadContent'
			Class.forName("com.mysql.jdbc.Driver");//loads driver
			String url="jdbc:mysql://localhost/mydb?user=root&password=qwerty";
			Connection cn=DriverManager.getConnection(url); //connection established

			while(true)
			{
				sock=ServerSock.accept();
				ObjectOutputStream oos=new ObjectOutputStream(sock.getOutputStream());
				String query="select * from GlobetrotDB where channel_name='oisChannel' ";
				// create the java statement
				java.sql.Statement st = cn.createStatement();
				// execute the query, and get a java resultset
				ResultSet rs = st.executeQuery(query);
				// iterate through the java resultset
				while (rs.next())
				{
					String strChannel =rs.getString("channel_name");
					//System.out.println("In Sender "+strChannel);
					oos.writeObject(strChannel);//send data to 'ReadContent'
					String strLatitude = rs.getString("latitude");
					//System.out.println("In Sender "+strLatitude);
					oos.writeObject(strLatitude);
					String strLongitude = rs.getString("longitude");
					//System.out.println("In Sender "+strLongitude);
					oos.writeObject(strLongitude);
					System.out.println("In Sender SENT");//send data to 'ReadContent'
				}
				oos.close();
				st.close();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			//sock.close();
		}
	}
}