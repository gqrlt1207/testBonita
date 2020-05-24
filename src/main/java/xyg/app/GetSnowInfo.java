package xyg.app;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



public class GetSnowInfo {
	private String incid;
	
	
	public GetSnowInfo(String incid)
	{
		this.incid = incid;
		
	}
	public Map<String,String> getSnowInfo() throws IOException{
		  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		  Date date = new Date();
		  String curtime=dateFormat.format(date);
		  String credential="mule:mule123";
		  String httpurl="https://mule-internal.muleca.compucom.com/api/incident/incidentnumber?sender=HIRO&client=HIRO&transactionId=";
		  String tranid="";
		  incid=incid.trim();
		  //Stream.of(1,2,3,4).forEach(p->System.out.println(p));
		  
		  String iid = String.valueOf(Math.random()*999999+1);
		  
		  tranid=tranid.concat(incid).concat(curtime).concat(iid);
		  System.out.println("\n tranid: "+tranid+"\n");
		  httpurl=httpurl.concat(tranid).concat("&caseNumber=").concat(incid);
		  System.out.println("\n httpurl "+httpurl+"\n");
		  muleRequest mrequest = new muleRequest(httpurl,credential);
		  String result = mrequest.sendRequest();
		  String assignedgroup="nodata";
		  String snowstatus="nodata";
		  String eventid="nodata";
		  String eventdescription="nodata";
		  String opentime="nodata";
		 
		  try{
		  PrintWriter pw = new PrintWriter("/tmp/snowv1.out");
		  pw.println(result);
		  pw.close();
	      JSONParser parser = new JSONParser();
	      Object obj1 = parser.parse(new FileReader("/tmp/snowv1.out"));
		  JSONObject jo = (JSONObject) obj1;
		  JSONObject ja = (JSONObject) jo.get("incident");
		  assignedgroup = (String) ja.get("supportGroup");
		  snowstatus = (String) ja.get("status");
		  eventid=(String) ja.get("shortDescription");
		  eventdescription=(String) ja.get("problemDescription");
		  opentime=(String) ja.get("openedDateStamp");

		  }catch(Exception e){
		  System.out.println("\n exception :"+e+"\n");
		  }
		  Map<String,String> snowinfo = new HashMap<String,String>();
		  String servername="nodata";
          String ipaddress="nodata";
          String ostype="nodata";
          GetCIName getciname = new GetCIName(eventid);
          servername = getciname.getciname();
          GetServerInfo getserverinfo = new GetServerInfo(servername);
          ipaddress = getserverinfo.getServerInfo()[0];
          ostype = getserverinfo.getServerInfo()[1];
		  snowinfo.put("incident",incid);
		  snowinfo.put("AssignedGroup",assignedgroup);
		  snowinfo.put("SnowStatus",snowstatus);
		  snowinfo.put("ShortDescription",eventid);
		  snowinfo.put("ProblemDescription",eventdescription);
		  snowinfo.put("openedDateStamp",opentime);
		  snowinfo.put("servername",servername);
		  snowinfo.put("ipaddress",ipaddress);
		  snowinfo.put("ostype",ostype);
		  return snowinfo;
		  }

}
