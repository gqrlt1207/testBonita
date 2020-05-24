package xyg.app;

import java.io.FileReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetTicketInfo {
	private int chkInterval;
	
	GetTicketInfo(int chkInterval){
		this.chkInterval = chkInterval;
	}
	public void getTicketInfo() {
	  long unixTime = Instant.now().getEpochSecond();
	  long interval = chkInterval*60;
	  long unixTime2 = unixTime - interval;
	  Date date1 = new Date(unixTime * 1000L);
	  Date date2 = new Date(unixTime2 * 1000L);
	  SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	  String endtime = jdf.format(date1);
	  String startime = jdf.format(date2);
	  
	  String credential="mule:mule123";
	  String httpurl="https://mule-internal.muleca.compucom.com/api/incident/incident?createdDateRange=%5B";
	  	   
	  httpurl=httpurl.concat(startime).concat("%20TO%20").concat(endtime).concat("%5D");
	  System.out.println("\n httpurl "+httpurl+"\n");
	  muleRequest mrequest = new muleRequest(httpurl,credential);
	  String result = mrequest.sendRequest();
	  String snowstatus="nodata";
	  String opentime="nodata";
	  String incident="nodata";
	  String eventid="nodata";
	 
	  try{
	  PrintWriter pw = new PrintWriter("/tmp/ticketinfo.out");
	  pw.println(result);
	  pw.close();
      JSONParser parser = new JSONParser();
      Object obj1 = parser.parse(new FileReader("/tmp/ticketinfo.out"));
	  JSONObject jo = (JSONObject) obj1;
	  JSONArray ja = (JSONArray) jo.get("incidents");
	  @SuppressWarnings("unchecked")
	  Iterator<JSONObject> iterator1 = ja.iterator();
	  while(iterator1.hasNext()) {
		  JSONObject jaa = iterator1.next();
		  incident = (String) jaa.get("incidentNumber");
	  	  snowstatus = (String) jaa.get("statusCode");
	  	  opentime=(String) jaa.get("createdDate");
	  	  eventid=(String) jaa.get("shortDescription");
	  	  //System.out.println(incident+","+eventid+","+snowstatus+","+opentime);
	  	  GetSnowInfo snowinfo = new GetSnowInfo(incident);
	  	  Map<String,String> ticketinfo = snowinfo.getSnowInfo();
	  	  String assignedgroup = ticketinfo.get("AssignedGroup");
	  	  String eventdescription = ticketinfo.get("ProblemDescription");
	  	  System.out.println(incident+","+eventid+","+
	  	  snowstatus+","+opentime+","+assignedgroup+","+eventdescription);
	  	  try {
	  		  if(eventid.contains("NOTIFICATION")) {
	  			  InsertData input = new InsertData(ticketinfo);
	  			  input.connect();
	  			  
	  		  }else {
	  			  System.out.println("not SMART or NETIQ ticket "+eventid);
	  		  }
	  	  }catch(Exception e) {
	  		System.out.println(e);
	  		System.out.println(incident+","+eventid+","+
	  		  	  snowstatus+","+opentime+","+assignedgroup+","+eventdescription);  
	  	  }
	  	
	  	  
	  }

	  }catch(Exception e){
		  e.printStackTrace();
	  }
	  
	}
	
	
	public static void main(String[] args) {
		int interval = Integer.valueOf(args[0]);
		new GetTicketInfo(interval).getTicketInfo();
	}

}
