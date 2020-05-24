package xyg.app;


import java.util.Map;

public class CallBonitaProcess {
	private Map<String,String> alertinfo;
	
	CallBonitaProcess(Map<String,String> alertinfo){
		this.alertinfo = alertinfo;
	}
	
	public void startBonitaProcess() {
		String eventid = alertinfo.get("ShortDescription");
		String ostype = alertinfo.get("ostype");
		String incident = alertinfo.get("incident");
		String eventdescription = alertinfo.get("ProblemDescription");
		String servername = alertinfo.get("servername");
		String ipaddress = alertinfo.get("ipaddress");
		String alerttype = "nodata";
		if(eventid.indexOf("AIXErrptLog")!=-1) {
			alerttype = "AIXErrptLog";
		}
		
		QueryMatchTable matchinfo = new QueryMatchTable(alerttype,ostype);
		String[] bonitainfo = matchinfo.getMatchInfo();
		String process = bonitainfo[0];
		String version = bonitainfo[1];
		if(process.contains("nodata")) {
			System.out.println("no Bonita Process found!!");
			return;
		}
		String processinfo = process.concat(":").concat(version);
		String contractinfo = "ciname:";
		contractinfo=contractinfo.concat(servername).concat(";").concat("eventid:").concat(eventid)
				.concat(";").concat("ipaddress:").concat(ipaddress).concat(";")
				.concat("ostype:").concat(ostype).concat(";").concat("eventdescription:")
				.concat(eventdescription).concat(";").concat("incident:")
				.concat(incident);
		
		startworkflow sp = new startworkflow(processinfo,contractinfo);
		try {
			sp.startprocess();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
