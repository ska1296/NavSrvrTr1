/** The server of multiple client program**/

/**
 *
 * @author shubh ketan
 */

package chat_tr1;

import java.io.*;
import java.net.*;
import java.util.*;

public class VerySimpleChatServer 
{
	ArrayList clientOutputStreams;

	public class ClientHandler implements Runnable 
	{
		BufferedReader reader; 
		Socket sock;
		public ClientHandler (Socket clientSocket) 
		{ 
			try 
			{
				sock = clientSocket; 
				InputStreamReader isReader = new InputStreamReader (sock.getInputStream());
				reader= new BufferedReader (isReader) ;
			}
			catch(Exception ex) 
			{
				ex.printStackTrace();
			}
		}//close the constructor

		public void run () 
		{ 
			String message; 
			try 
			{
				while((message = reader.readLine())!= null)
				{
					
					System.out.println("read " + message);
					tellEveryone(message);
				}//end while
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}//close run
	}//end inner class

	public static void main (String[] args)
	{
		new VerySimpleChatServer().go();
	}

	public void go()
	{
		clientOutputStreams = new ArrayList();
		try 
		{
			ServerSocket serverSock=new ServerSocket(3070);
			while (true) 
			{
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream()); 
				clientOutputStreams.add(writer) ;		
				Thread t=new Thread(new ClientHandler(clientSocket));			
				t.start(); 
				System.out.println("get a connection!" );
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace(); 
		}
	}//close go

	public void tellEveryone(String message)
	{
		Iterator it = clientOutputStreams.iterator();
		while(it.hasNext())
		{
			try 
			{
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			}
			catch (Exception ex) 
			{ 
				ex.printStackTrace();
			}
		}//end while
	}//close tell Everyone

}//end class