package chat_tr1;

import java.awt.*; 
import java.awt.event.*; import java.net.*; 
import java.io.*; 
class client implements ActionListener 
{ 
	Frame f; 
	TextField t; 
	List l; 
	Button b; 
	Socket s; 
	client() 
	{ 
		f=new Frame("Client"); 
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
			s=new Socket("localhost",3070); //Creates a stream socket and connects it to the specified port number on the named host
			while(true) 
			{ 
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());//reads from the specified InputStream
				String str=ois.readObject().toString(); //object's stuff to string
				l.add("Friend:-"+str); //add server's messages to client's window, most probably
			} 
		} 
		catch(Exception e) 
		{ 
			System.out.println(e.getMessage()); 
		}
	} 
	public static void main(String ar[]) 
	{ 
		client c=new client();
	} 
	public void actionPerformed(ActionEvent e) 
	{ 
		try
		{ 
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());//Creates an ObjectOutputStream that writes to the specified OutputStream
			oos.writeObject(t.getText());//sends message to sever read above
			l.add("Me:-"+t.getText()); //to put up my messages anywhere
		} 
		catch(Exception e1) 
		{ 
			System.out.println(e1.getMessage()); 
		} 
	} 
}