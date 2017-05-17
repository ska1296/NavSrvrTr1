/** The client of multiple client program**/

/**
 *
 * @author shubh ketan
 */

package chat_tr1;

import java.io.*; 
import java.net.*; 
import javax. swing.*; 
import java.awt.*; 
import java.awt.event.*;
public class SimpleChatClientA
{
	JTextField outgoing;
	JTextArea incoming;
	PrintWriter writer; 
	Socket sock;
	BufferedReader reader;

	public void go () 
	{
		JFrame frame = new JFrame("Ludicrously SimpleChatClientA"); 
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15,50); 
		incoming.setLineWrap(true); 
		incoming.setWrapStyleWord(true); 
		incoming.setEditable(false); 
		JScrollPane qScroller = new JScrollPane(incoming); 
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); 
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
		outgoing = new JTextField(20); 
		JButton sendButton =new JButton ("send");
		sendButton.addActionListener(new SendButtonLiBtener());
		mainPanel.add(outgoing); 
		mainPanel.add(sendButton);
		Thread readerThread=new Thread(new IncomingReader());
		frame.getContentPane().add (BorderLayout.CENTER, mainPanel); 
		setUpNetworkinq(); 
		frame.setSize(400,500);
		frame.setVisible(true); 
	}// close go 
	private void setUpNetworkinq () 
	{ 
		try
		{
			sock=new Socket("localhost",3070);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream()); 
			reader =new BufferedReader(streamReader); //get messages from the server
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		}
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
	}//close setUpNetworking
	public class SendButtonLiBtener implements ActionListener //send contents of textfield to the server
	{
		public void actionPerformed(ActionEvent ev) 
		{ 
			try 
			{ 
				writer.println(outgoing.getText());
				writer.flush();
			}

			catch(Exception ex)
			{ 
				ex.printStackTrace();
			}
			outgoing.setText ("");
			outgoing.requestFocus();
		}
	}// close SendButtonListener inner class

	public class IncomingReader implements Runnable //use to put messages on the console as long as the messages are coming in
	{
		public void run()
		{
			String message;
			try
			{
				while ((message=reader.readLine())!=null)
				{
					System.out.println("read"+message);
					System.out.println("read already!!");
					incoming.append(message+"\n");
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void main (String args[]) 
	{
		SimpleChatClientA client=new SimpleChatClientA();
		client.go();
	}
}