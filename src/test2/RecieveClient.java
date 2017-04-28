package test2;

/**
 * Created by Shibhu on 11-Apr-17.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.*;

public class RecieveClient {
	static Socket s = null;
	static String channelName;
	static double lat, lon;
	public static void main(String args[])throws IOException {
		InputStreamReader read=new InputStreamReader(System.in);
		BufferedReader in=new BufferedReader(read);
		System.out.println("channel name");
		channelName=in.readLine();
		try {
			if (s == null) {
				s = new Socket("localhost", 3072);
			}
			while(true) {
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(channelName);

				//System.out.println("HELLO "+channelName+" wuddup");
				ObjectInputStream fl=new ObjectInputStream(s.getInputStream());
				//System.out.println("100");
				int flag=Integer.parseInt(fl.readObject().toString());
				//System.out.println("101");
				//System.out.println(flag);
				//System.out.println("102");
				if(flag==1){
					//System.out.println("103");
					//System.out.println(flag+" flag1");
					System.out.println(channelName);
					
					ObjectInputStream oisLat=new ObjectInputStream(s.getInputStream());
					String strLat=oisLat.readObject().toString(); 
					ObjectInputStream oisLon=new ObjectInputStream(s.getInputStream());
					String strLon=oisLat.readObject().toString();
					
					lon=Double.parseDouble(strLat);
					lat=Double.parseDouble(strLon);
					
					System.out.println(channelName + " Channel Name " + lat + " latitude " + lon + " longitude ");
					System.out.println(lon+"longitude");
					System.out.println(lat+"latitude");
				}
				else if (flag==0) {
					System.out.println(flag+"flag0");
					System.out.println(channelName);
					ObjectInputStream msg=new ObjectInputStream(s.getInputStream());
					String message=msg.readObject().toString();
					System.out.println(message);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}