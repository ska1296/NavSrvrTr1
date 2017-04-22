package test2;
/**
 *
 * @author shubh ketan
 */

import java.awt.*; 
import java.awt.event.*; 
import java.net.*; 
import java.sql.*;
import java.io.*;

class Server implements ActionListener 
{
	Frame f; 
	TextField t; 
	List l; 
	Button b; 
	ServerSocket serverSock; 
	Socket sock;
	int tep;
	String strLon, strLat, strChannel;
	Server() 
	{ 
		f=new Frame("Server"); 
		t=new TextField(); 
		l=new List(); 
		b=new Button("Send"); 
		b.addActionListener(this); 
		f.add(t,BorderLayout.NORTH); 
		f.add(l); 
		f.add(b,BorderLayout.SOUTH); 
		f.setVisible(true); 
		f.setSize(300,300); 		
		try 
		{ 
			serverSock=new ServerSocket(3072); //Creates a server socket, bound to the specified port
			while (true) {
				sock=serverSock.accept(); //Listens for a connection to be made to this socket and accepts it
				System.out.println("Connection created");
				while(true) 
				{ 				
					ObjectInputStream oisChannel=new ObjectInputStream(sock.getInputStream());
					strChannel=oisChannel.readObject().toString();

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

						ObjectOutputStream fl=new ObjectOutputStream(sock.getOutputStream()); //sends message to the client
						fl.writeObject("1");
						ObjectOutputStream oosLat=new ObjectOutputStream(sock.getOutputStream());
						oosLat.writeObject(strLat);
						ObjectOutputStream oosLon=new ObjectOutputStream(sock.getOutputStream());
						oosLon.writeObject(strLon);
						//System.out.println(ChannelName+" "+strChannel);
						System.out.println(strLon);
						System.out.println(strLat);
					}
					else
					{
						ObjectOutputStream fl=new ObjectOutputStream(sock.getOutputStream()); //sends message to the client
						fl.writeObject("0");
						ObjectOutputStream msg=new ObjectOutputStream(sock.getOutputStream()); //sends message to the client
						msg.writeObject("Channel Name not found");
					}
					//System.out.println("LOL");

					cn.close();
					//System.out.println("HELLO!");
				} 
			}
		} 
		catch(Exception e) 
		{ 
			System.out.println(e.getMessage());
		} 
	} 
	public static void main(String ar[]) 
	{ 
		Server s=new Server(); 
	} 
	public void actionPerformed(ActionEvent e) 
	{ 
		try //for server's message to be put on screen and sent to the client
		{
			ObjectOutputStream oos=new ObjectOutputStream(sock.getOutputStream()); //sends message to the client
			oos.writeObject(t.getText()); //ACTUALLY send message to the client
			l.add("Me:-"+t.getText()); 
		} 
		catch(Exception e1) 
		{ 
			System.out.println(e1.getMessage()); 
		} 
	} 
}