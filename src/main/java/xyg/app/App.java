package xyg.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.time.LocalDate;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.*;
import org.bonitasoft.engine.bpm.process.*;
import org.bonitasoft.engine.util.APITypeManager;

import dnl.utils.text.table.TextTable;


/**
 * Hello world!
 *
 */
public class App 
{
	
		
	
    public static void main( String[] args ) throws IOException
    {
	Map<String, String> settings = new HashMap<String, String>();
	BufferedReader br = new BufferedReader(new FileReader("/etc/bonita/config/config.txt"));
	String line;
	String server="local";
	String port="8080";
	String username="walter.bates";
	String password="bpm";
	String item1;
	String processname="Pool";
	String version="1.0";
	String argument="";
	String procinfo="";
	boolean flag = false;
	for(int i=0;i<args.length;i++) {
		if(flag) {
			argument=argument.concat(" ").concat(args[i]);
			continue;
		}
		if(args[i].contains(":")&&!args[i].contains("Version")&&!args[i].contains("version")) {
			System.out.println(args[i]);
			flag=true;
			argument=argument.concat(args[i]);
			continue;
		}
		procinfo=procinfo.concat(args[i]).concat(" ");
		
	}
	procinfo=procinfo.trim();
	if(procinfo.contains("version")) {
			
    String[] processinfo=procinfo.split("version:");
	processname=processinfo[0].trim();
	if(processinfo.length>1) {
		version=processinfo[1].trim();
	}
	
	}else if (procinfo.contains("Version")) {
		String[] processinfo=procinfo.split("Version:");
		processname=processinfo[0].trim();
		if(processinfo.length>1) {
			version=processinfo[1].trim();
		}
	}else {
		String[] processinfo=procinfo.split(":");
		processname=processinfo[0].trim();
		if(processinfo.length>1) {
			version=processinfo[1].trim();
		}
	}
	
	/**
	Map<String,String> contract2 = new HashMap<String,String>();
	String contract0 = args[1];
	String[] con2 = contract0.split(";");
	for(String cons: con2) {
		String key= cons.split(":")[0];
		String value=cons.split(":")[1];
		System.out.println(key+" "+value);
		contract2.put(key,value);
	}
	**/
	
	//String json2 = new JSONObject(contract2).toString();
	//Map<String,Serializable> contract = new HashMap<String,Serializable>();
	//contract.put("alertInput",(Serializable) contract2);
	
	System.out.println(processname+":"+version);
	
	while((line=br.readLine())!=null) {
		item1 = line.split(":")[0];
		//System.out.println(item1);
		if(item1.contains("server"))
			server=line.split(":")[1];
		else if(item1.contains("port"))
			port=line.split(":")[1];
		else if(item1.contains("username"))
			username=line.split(":")[1];
		else if(item1.contains("password"))
			password=line.split(":")[1];
		else
			System.out.println("complete reading the config file...");		
	}
	br.close();
	String prefix = "http://";
	String url = prefix.concat(server).concat(":").concat(port);
	System.out.println(url);
	String link = url.concat("/bonita/portal/homepage#?_p=monitoringpm&_pf=3&monitoringpm_id=");
	
	settings.put("server.url", url);
	settings.put("application.name", "bonita");
	APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, settings);
	//String username="walter.bates";
	//String password="bpm";

	org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
	try {
	apiClient.login(username, password);
	final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
	
	final APISession session = loginAPI.login(username, password);
	System.out.println(" ");
	final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(session);
	if(processname.contains("help")) {
		/*GetAllProcess deployedprocessinfo = new GetAllProcess(session);
		HashMap<String,String> hmap = deployedprocessinfo.getDeployedProcess();
		//hmap.entrySet().forEach(entry->System.out.println(entry.getKey()+":"+entry.getValue()));
		System.out.println("ProcessName"+"  "+"ProcessID"+" "+"ProcessVersion"+" "+"Status");
		for(Map.Entry<String,String> record: hmap.entrySet()) {
			//System.out.println("\n");
			System.out.println(record.getKey()+" : "+record.getValue());
			long processid = Long.parseLong(record.getValue().split(" ")[0].trim());
			new getContract(processid,processAPI).getInput();
			//Map<String,String> pload = gcontract.getInput();
			//for(Map.Entry<String,String> record1:pload.entrySet()) {
				//System.out.println(record1.getKey()+" "+record1.getValue());
			//}
		}
		System.out.println("\n");
		System.exit(1);
		*/
		GetAllProcess deployedprocessinfo = new GetAllProcess(session);
		List<HashMap<String,String>> lst1 = deployedprocessinfo.getallprocessinfo();
		String[] columnNames = {"processName","processId","version","status","constraintInfo","contractInfo"};
		int n = lst1.size();
		Object[][] data = new Object[n][6];
		for(int i=0;i<lst1.size();i++) {
			data[i][0]=lst1.get(i).get("processName");
			data[i][1]=lst1.get(i).get("processId");
			data[i][2]=lst1.get(i).get("version");
			data[i][3]=lst1.get(i).get("status");
			data[i][4]=lst1.get(i).get("constraintInfo");
			data[i][5]=lst1.get(i).get("contractInfo");
		}
				
		
		TextTable tt = new TextTable(columnNames,data);                                                         
		tt.printTable(); 
		System.out.println("\n\n");
		String link2=url.concat("/bonita/portal/homepage#?_p=monitoringpm&_pf=3");
		System.out.println(link2);
		System.out.println();
		System.exit(1);
		
	}
	
	long processid = processAPI.getProcessDefinitionId(processname,version);
	link=link.concat(String.valueOf(processid));
	
	getContract gcontract = new getContract(processid,processAPI);
	Map<String,Serializable> pload = gcontract.getInput();
	String tName = gcontract.getTname();
	
	if(!argument.isEmpty()) {
	
	String[] arg = argument.split(" ");
	String contract0 = arg[0];
	if(arg.length>2) {
		for(int i=1;i<arg.length;i++) {
			contract0=contract0.concat(" ").concat(arg[i]);
		}
	}
	//System.out.println(contract0);
	//String contract0 = args[1];
	String[] con2 = contract0.split(";");
	//System.out.println("\n\n");
	for(String cons: con2) {
		String[] tinfo = cons.split(":");
		String key1= cons.split(":")[0];
		String value1 =cons.split(":")[1];
		for(int i=2;i<tinfo.length;i++) {
			value1=value1.concat(":").concat(tinfo[i]);
		}
		
		//String value1=cons.split(":")[1];
		System.out.print(key1+":"+value1+" ");
		if(key1.contains("Date")||key1.contains("date")) {
			//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
			LocalDate value2 = LocalDate.parse(value1);
			pload.replace(key1, value2);
			continue;
		}
		if(key1.contains("num")||key1.contains("Num")) {
			int value2 = Integer.parseInt(value1);
			pload.replace(key1, value2);
			continue;
		}
		pload.replace(key1, value1);
	}
	System.out.println();
	}else {
		
		System.out.println("No contract found!!");
	}
		
	System.out.println("\n\n"+Arrays.asList(pload));
	
	Map<String,Serializable> contract = new HashMap<String,Serializable>();
	contract.put(tName,(Serializable)pload);
	
	final ProcessInstance processInstance = processAPI.startProcessWithInputs(processid,contract);
	long caseid = processInstance.getId();
	link=link.concat("-").concat(String.valueOf(caseid)).concat("&");
	System.out.println("\n\nA new process instance was started with id: " + processInstance.getId()+"\n");
	System.out.println(link);
	//System.out.println("A new process was enabled: " + "1");
	loginAPI.logout(session);
	System.out.println("\nlog out of the Bonita Portal..."+"\n");
	}catch(Exception e) {
		e.printStackTrace();
	}
	
	//System.out.println("process initiated ..."+"\n");

    }
}
