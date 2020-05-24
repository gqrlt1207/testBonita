package xyg.app;

import java.util.Arrays;
import java.util.HashMap;
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

/**
 * Hello world!
 *
 */
public class startworkflow
{
	private String processinfo1 ;
	private String contractinfo;
	
	startworkflow(String processinfo,String contractinfo){
		this.processinfo1 = processinfo;
		this.contractinfo = contractinfo;
	}
	
	//@SuppressWarnings("unchecked")
    public void startprocess( ) throws IOException
    {
	Map<String, String> settings = new HashMap<String, String>();
	BufferedReader br = new BufferedReader(new FileReader("/home/bgao/bonita/etc/config/config.txt"));
	String line;
	String server="local";
	String port="8080";
	String username="walter.bates";
	String password="bpm";
	String item1;
	String processname="Pool";
	String version="1.0";
	String[] processinfo=processinfo1.split(":");
	processname=processinfo[0];
	if(processinfo.length>1) {
		version=processinfo[1];
	}
	
		
	
	System.out.println(processname+" "+version+"\n\n");
	
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
	System.out.println(url+"\n\n");
	
	settings.put("server.url", url);
	settings.put("application.name", "bonita");
	APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, settings);
	

	org.bonitasoft.engine.api.APIClient apiClient = new APIClient();
	try {
	apiClient.login(username, password);
	final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
	
	APISession session = loginAPI.login(username, password);
	System.out.println("\n\n");
	final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(session);
	if(processinfo1.contains("help")) {
		GetAllProcess deployedprocessinfo = new GetAllProcess(session);
		HashMap<String,String> hmap = deployedprocessinfo.getDeployedProcess();
		//hmap.entrySet().forEach(entry->System.out.println(entry.getKey()+":"+entry.getValue()));
		System.out.println("ProcessName"+"  "+"ProcessID"+" "+"ProcessVersion"+" "+"Status");
		for(Map.Entry<String,String> record: hmap.entrySet()) {
			System.out.println("\n");
			System.out.println(record.getKey()+" : "+record.getValue());
			long processid = Long.parseLong(record.getValue().split(" ")[0].trim());
			new getContract(processid,processAPI).getInput();
			
		}
		System.out.println("\n\n");
		System.exit(1);
	}
	
	long processid = processAPI.getProcessDefinitionId(processname,version);
	
	getContract gcontract = new getContract(processid,processAPI);
	Map<String,Serializable> pload = gcontract.getInput();
	String tName = gcontract.getTname();
	
	String[] coninfo = contractinfo.split(" ");
	String contract0 = coninfo[0];
	if(coninfo.length>2) {
		for(int i=1;i<coninfo.length;i++) {
			contract0=contract0.concat(" ").concat(coninfo[i]);
		}
	}
	System.out.println("\n\n"+contract0+"\n");
	//String contract0 = args[1];
	String[] con2 = contract0.split(";");
	System.out.println("\n\n");
	for(String cons: con2) {
		String[] tinfo = cons.split(":");
		String key1= cons.split(":")[0];
		String value1 =cons.split(":")[1];
		for(int i=2;i<tinfo.length;i++) {
			value1=value1.concat(":").concat(tinfo[i]);
		}
		
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
		
		//String value1=cons.split(":")[1];
		System.out.println(key1+" "+value1);
		pload.replace(key1, value1);
	}
	
		
	System.out.println("\n\n"+Arrays.asList(pload));
	
	Map<String,Serializable> contract = new HashMap<String,Serializable>();
	contract.put(tName,(Serializable)pload);
	
	final ProcessInstance processInstance = processAPI.startProcessWithInputs(processid,contract);
	System.out.println("\n\nA new process instance was started with id: " + processInstance.getId()+"\n");
	//System.out.println("A new process was enabled: " + "1");
	loginAPI.logout(session);
	System.out.println("\nlog out of the Bonita Portal..."+"\n");
	}catch(Exception e) {
		e.printStackTrace();
	}
	
	System.out.println("complete testing ..."+"\n");

    }
}

