package xyg.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class GetServerInfo {
	private String ciname;
	
	GetServerInfo(String ciname){
		this.ciname = ciname;
	}
	
    public String[] getServerInfo() throws IOException{
    	BufferedReader br = new BufferedReader(new FileReader("/home/bgao/bonita/projects/host.list"));
    	String sr;
    	String ipaddress="nodata";
    	String ostype="nodata";
    	while((sr=br.readLine())!=null) {
    		if(sr.contains(ciname)) {
    			ipaddress=sr.split(",")[1];
    			ostype=sr.split(",")[2];
    			break;
    		}
    	}
    	String[] sinfo = new String[2];
    	sinfo[0]=ipaddress;
    	sinfo[1]=ostype;
    	br.close();
    	return sinfo;
    	
    }
		

}
