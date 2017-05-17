package date_a_base;

import java.io.*;
import java.net.*;
import java.sql.*;

//a multithreading server to handle multiple clients.

public class BroadcastServer {
	public static void main(String args[]){


		Socket s=null;
		ServerSocket ss2=null;
		System.out.println("Server Listening......");
		try{
			ss2 = new ServerSocket(3070); // can also use static final PORT_NUM , when defined
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Server error");
		}
		while(true){
			try{
				s= ss2.accept();
				System.out.println("connection Established");
				BroadcastServerThread st=new BroadcastServerThread(s);
				st.start();
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Connection Error");
			}
		}
	}
}
class BroadcastServerThread extends Thread {  
	String strChannel=null;
	Socket sock=null;
	String strLon, strLat;
	int tep;

	public BroadcastServerThread(Socket s){
		this.sock=s;
	}

	public void run() {
		try{
			ObjectInputStream oisLon=new ObjectInputStream(sock.getInputStream());//read's client's message on that socket
			ObjectInputStream oisLat=new ObjectInputStream(sock.getInputStream()); //read's client's message on that socket
			ObjectInputStream oisChannel=new ObjectInputStream(sock.getInputStream()); //read's client's message on that socket

			strLon=oisLon.readObject().toString(); //converts to string
			strLat=oisLat.readObject().toString(); //converts to string
			strChannel=oisChannel.readObject().toString(); //converts to string
		}catch(Exception e){
			System.out.println("IO error in server thread");
		}

		try {
			while(true){
				//System.out.println(strChannel);
				//System.out.println("Response to Client  :  "+strChannel);

				Class.forName("com.mysql.jdbc.Driver");//loads driver
				String url="jdbc:mysql://localhost/mydb?user=root&password=qwerty";
				Connection cn=DriverManager.getConnection(url); //connection established

				String query="select * from GlobetrotDB where channel_name='"+strChannel+"'"; //query pass to table
				java.sql.Statement st = cn.createStatement();
				ResultSet rs = st.executeQuery(query);
				String ChannelName;
				int flag=0;
				while (rs.next()) //to traverese to the end of the table
				{
					ChannelName = rs.getString("channel_name"); //read from column and put into a variable
					if (strChannel.equals(ChannelName)) //logical stuff for my code.
					{
						flag=1;
						break;
					}
				}
				if (flag==0)
				{
					String q="insert into GlobetrotDB (channel_name, latitude, longitude) "+"values (? ,?, ?)";
					PreparedStatement preparedst=cn.prepareStatement(q);
					preparedst.setString (1, strChannel);
					preparedst.setString (2, strLat);
					preparedst.setString (3, strLon);
					tep=preparedst.executeUpdate();
					flag=1;
					//System.out.println(ChannelName+" "+strChannel);
				}
				else
				{
					String q="Update GlobetrotDB set latitude = ?, longitude= ? where channel_name = ?";
					PreparedStatement preparedst=cn.prepareStatement(q);
					preparedst.setString (1, strLat);
					preparedst.setString (2, strLon);
					preparedst.setString (3, strChannel);
					tep=preparedst.executeUpdate();
				}
				//System.out.println("data inserted");
				//System.out.println(strLon);
				//System.out.println(strLat);
				//System.out.println(strChannel);

				//System.out.println("LOL");

				cn.close();
				//System.out.println("HELLO!");			}   
			} 
		}catch (Exception e) {

			strChannel=this.getName(); //reused String strChannel for getting thread name
			//System.out.println("IO Error/ Client "+strChannel+" terminated abruptly");
			e.printStackTrace();
		}

		finally{    
			try{
				System.out.println("Connection Closing..");
				if (sock!=null){
					sock.close();
					System.out.println("Socket Closed");
				}

			}
			catch(IOException ie){
				System.out.println("Socket Close Error");
			}
		}//end finally
	}
}