package date_a_base;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class RecieveServer {
	public static void main(String args[]){


		Socket s=null;
		ServerSocket ss2=null;
		System.out.println("Server Listening......");
		try{
			ss2 = new ServerSocket(3072); // can also use static final PORT_NUM , when defined
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Server error");
		}
		while(true){
			try{
				s= ss2.accept();
				System.out.println("connection Established");
				RecieveServerThread st=new RecieveServerThread(s);
				st.start();
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Connection Error");
			}
		}
	}
}
class RecieveServerThread extends Thread {  
	String strChannel=null;
	Socket s=null;
	String strLon, strLat;
	
	public RecieveServerThread(Socket s){
		this.s=s;
	}

	public void run() {
		try{
			ObjectInputStream oisChannel=new ObjectInputStream(s.getInputStream());
			strChannel=oisChannel.readObject().toString();
		}catch(Exception e){
			System.out.println("IO error in server thread");
		}

		try {
			while(strChannel.compareTo("QUIT")!=0){
				//System.out.println(strChannel);
				//System.out.println("Response to Client  :  "+strChannel);
				
				Class.forName("com.mysql.jdbc.Driver");//loads driver
				String url="jdbc:mysql://localhost/mydb?user=root&password=qwerty";
				Connection cn=DriverManager.getConnection(url); //connection established
				String query="select * from GlobetrotDB where channel_name='"+strChannel+"'";
				java.sql.Statement st = cn.createStatement();
				ResultSet rs = st.executeQuery(query);
				String ChannelName;
				int flag=0;
				while (rs.next())
				{
					ChannelName = rs.getString("channel_name");
					if (strChannel.equals(ChannelName))
					{
						flag=1;
						break;
					}
				}
				if (flag==1)
				{
					strLon=rs.getString("longitude");
					strLat=rs.getString("latitude");

					ObjectOutputStream fl=new ObjectOutputStream(s.getOutputStream()); //sends message to the client
					fl.writeObject("1");
					ObjectOutputStream oosLat=new ObjectOutputStream(s.getOutputStream());
					oosLat.writeObject(strLat);
					ObjectOutputStream oosLon=new ObjectOutputStream(s.getOutputStream());
					oosLon.writeObject(strLon);
					//System.out.println(ChannelName+" "+strChannel);
					System.out.println(strLon);
					System.out.println(strLat);
				}
				else
				{
					ObjectOutputStream fl=new ObjectOutputStream(s.getOutputStream()); //sends message to the client
					fl.writeObject("0");
					ObjectOutputStream msg=new ObjectOutputStream(s.getOutputStream()); //sends message to the client
					msg.writeObject("Channel Name not found");
				}
				//System.out.println("LOL");

				cn.close();
				//System.out.println("HELLO!");
			}   
		} catch (Exception e) {

			strChannel=this.getName(); //reused String strChannel for getting thread name
			//System.out.println("IO Error/ Client "+strChannel+" terminated abruptly");
			e.printStackTrace();
		}

		finally{    
			try{
				System.out.println("Connection Closing..");
				if (s!=null){
					s.close();
					System.out.println("Socket Closed");
				}

			}
			catch(IOException ie){
				System.out.println("Socket Close Error");
			}
		}//end finally
	}
}