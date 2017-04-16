package test2;

import java.io.*;

public class tr1 {
	static double lat1,lon1;
	static String channelname1;
	static int flag1;
	
	public static void main(String[] args)throws IOException {
		RecieveClient bl=new RecieveClient();
		//bl.stuff();
		/*flag1=bl.getflag();
		lat1=bl.getlat();
		lon1=bl.getlongi();
		channelname1=bl.getchnm();*/
		
		System.out.println(channelname1);
		System.out.println(flag1);
		System.out.println(lon1);
		System.out.println(lat1);

	}
}