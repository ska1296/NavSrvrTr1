package Test1;

import java.io.*;
import java.net.Socket;

public class RecClient {
	static Socket s = null;
	static double lat;
	static double lon;
	static String chname;
	static int flag1;

	public void stuff() throws IOException {
		
		InputStreamReader read=new InputStreamReader(System.in);
		BufferedReader in=new BufferedReader(read);
		System.out.println("channel name");
		chname=in.readLine();
		RecClient bl=new RecClient();
		try {
			if (s == null) {
				s = new Socket("localhost", 3072);
			}
			while(true) {
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(chname);
				//System.out.println("HELLO");
				ObjectInputStream fl=new ObjectInputStream(s.getInputStream());
				String flag=fl.readObject().toString();
				//System.out.println(flag);
				if(flag.equals(1)){
					//System.out.println(flag+"flag1");
					ObjectInputStream oisLon=new ObjectInputStream(s.getInputStream());
					ObjectInputStream oisLat=new ObjectInputStream(s.getInputStream()); //read's client's message on that socket
					//System.out.println(channelName + " Channel Name " + lat + " latitude " + lon + " longitude ");
					String strLon=oisLon.readObject().toString(); //converts to string
					String strLat=oisLat.readObject().toString();
					lon=Double.parseDouble(strLon);
					lat=Double.parseDouble(strLat);
					
					System.out.println(lon);
					System.out.println(lat);
					bl.setflag(Integer.parseInt(flag));
					bl.setlat(lat);
					bl.setlongi(lon);
					bl.setchnm(chname);
				}
				else if (flag.equals(0)) {
					bl.setflag(Integer.parseInt(flag));
					flag1=bl.getflag();
					System.out.println(flag1+"flag0");
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
	
	int flag;
	double latt,lonn;
	String ChannelName;
	public int getflag(){
		return flag;
	}
	public void setflag(int flag2){
		this.flag=flag2;
	}
	public double getlat(){
		return latt;
	}
	public void setlat(double lat2){
		this.latt=lat2;
	}
	public double getlongi(){
		return lonn;
	}
	public void setlongi(double longi){
		this.lonn=longi;
	}
	public String getchnm(){
		return ChannelName;
	}
	public void setchnm(String chnme){
		this.ChannelName=chnme;
	}
}