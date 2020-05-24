package xyg.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetCurrentTime {
	  public String getCurrentTime(){
		  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		  Date date = new Date();
		  String curtime=dateFormat.format(date);
		  return curtime;
	  }

}
