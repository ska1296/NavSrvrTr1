package chat_tr1;

/** The Server of single client program**/

/**
 *
 * @author shubh ketan
 */

import java.awt.*; 
import java.awt.event.*; 
import java.net.*; 
import java.io.*; 
class server implements ActionListener 
{
	Frame f; 
	TextField t; 
	List l; 
	Button b; 
	ServerSocket ss; 
	Socket s; 
	server() 
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
			ss=new ServerSocket(3079); //Creates a server socket, bound to the specified port
			while (true) {
				s=ss.accept(); //Listens for a connection to be made to this socket and accepts it
				System.out.println("Connection created");
				while(true) 
				{ 				
					ObjectInputStream ois=new ObjectInputStream(s.getInputStream()); //read's client's message on that socket
					String str=ois.readObject().toString(); //converts to string
					System.out.println(str);
					l.add("Friend:-"+str); //puts on the server's window
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
		server s=new server(); 
	} 
	public void actionPerformed(ActionEvent e) 
	{ 
		try //for server's message to be put on screen and sent to the client
		{
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream()); //sends message to the client
			oos.writeObject(t.getText()); //ACTUALLY send message to the client
			l.add("Me:-"+t.getText()); 
		} 
		catch(Exception e1) 
		{ 
			System.out.println(e1.getMessage()); 
		} 
	} 
}