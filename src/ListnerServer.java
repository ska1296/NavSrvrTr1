/** The Server of single client program**/

/**
 *
 * @author shubh ketan
 */

import java.awt.*; 
import java.awt.event.*; 
import java.net.*; 
import java.sql.*;
import java.io.*;

class ListnerServer implements ActionListener 
{
	Frame f; 
	TextField t; 
	List l; 
	Button b; 
	ServerSocket serverSock; 
	Socket sock;
	int tep;
	String strLon, strLat, strChannel;
	ListnerServer() 
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
			serverSock=new ServerSocket(3070); //Creates a server socket, bound to the specified port
			while (true) {
				sock=serverSock.accept(); //Listens for a connection to be made to this socket and accepts it
				System.out.println("Connection created");
				while(true) 
				{ 				
					ObjectInputStream oisLon=new ObjectInputStream(sock.getInputStream());
					ObjectInputStream oisLat=new ObjectInputStream(sock.getInputStream()); //read's client's message on that socket
					ObjectInputStream oisChannel=new ObjectInputStream(sock.getInputStream());
					strLon=oisLon.readObject().toString(); //converts to string
					strLat=oisLat.readObject().toString();
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
					if (flag==0)
					{
						String q="insert into GlobetrotDB (channel_name, latitude, longitude) "+"values (? ,?, ?)";
						PreparedStatement preparedst=cn.prepareStatement(q);
						preparedst.setString (1, strChannel);
						preparedst.setString (2, strLat);
						preparedst.setString (3, strLon);
						tep=preparedst.executeUpdate();
						flag=1;
						System.out.println(strChannel);
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
					System.out.println(strLon);
					System.out.println(strLat);
					//System.out.println(strChannel);

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
		ListnerServer s=new ListnerServer(); 
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