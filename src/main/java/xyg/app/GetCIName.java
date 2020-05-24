package xyg.app;

public class GetCIName {
	private String eventid;
	
	GetCIName(String eventid){
		this.eventid = eventid;
	}
	
	public String getciname() {
		System.out.println(eventid);
		String eventid3 = eventid.split("_20")[0];
		if(eventid3.contains("/")){
			eventid3=eventid3.split("/")[0];
		}
		if(eventid3.contains("NOTIFICATION-SNMPTrap_")){
			eventid3=eventid3.split("NOTIFICATION-SNMPTrap_")[1];
		}
		if(eventid3.contains("NOTIFICATION-NETIQ__Trap_")){
			System.out.println(eventid3);
			eventid3=eventid3.split("NOTIFICATION-NETIQ__Trap_")[1];
			System.out.println(eventid3);
		}
		if(eventid3.contains("NOTIFICATION-Interface_IF-")){
			eventid3=eventid3.split("NOTIFICATION-Interface_IF-")[1];
		}
		if(eventid3.contains("NOTIFICATION-FileSystem_FS-")){
			eventid3=eventid3.split("NOTIFICATION-FileSystem_FS-")[1];
		}
		
		if(eventid3.contains("NOTIFICATION-ProcessorGroup_PSRGROUP-")){
			eventid3=eventid3.split("NOTIFICATION-ProcessorGroup_PSRGROUP-")[1];
		}
		
		if(eventid3.contains("NOTIFICATION-SNMPAgent_SNMPAgent-")) {
			eventid3=eventid3.split("NOTIFICATION-SNMPAgent_SNMPAgent-")[1];
		}
		if(eventid3.contains("NOTIFICATION-Host_")) {
			eventid3=eventid3.split("NOTIFICATION-Host_")[1];
		}
		if(eventid3.contains("NOTIFICATION-Memory_MEM-")) {
			eventid3=eventid3.split("NOTIFICATION-Memory_MEM-")[1];
		}
		
		if(eventid3.contains("NOTIFICATION-MemoryGroup_MEMGROUP-")){
			eventid3=eventid3.split("NOTIFICATION-MemoryGroup_MEMGROUP-")[1];
		}
		if(eventid3.contains(".isv.local")) {
			eventid3=eventid3.split(".isv.local")[0];
		}
		if(eventid3.contains(".ISV.LOCAL")) {
			eventid3=eventid3.split(".ISV.LOCAL")[0];
		}
		System.out.println(eventid3);
		return eventid3.toLowerCase();
	}
	

}
