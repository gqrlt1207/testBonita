package xyg.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ConstraintDefinition;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.InputDefinition;


public class getContract {
	private long pid ;
	private ProcessAPI processapi ;
	public getContract(long pid,ProcessAPI processapi) {
		this.pid = pid;
		this.processapi = processapi;
	}
	
	public String getTname() throws Exception{
		ContractDefinition condef = processapi.getProcessContract(pid);
		List<InputDefinition> indef = condef.getInputs();
		String inputname = "";
		for(InputDefinition de: indef) {
			//System.out.println(de.getName());
			inputname = de.getName();
		}
		return inputname;
		
	}
	
	
	public String[] getcontractinfo() throws Exception{
		String[] str1 = new String[2];
		String constraintinfo="";
		String contractinfo="";
		
		ContractDefinition condef = processapi.getProcessContract(pid);
		List<ConstraintDefinition> cdef = condef.getConstraints();
		for(ConstraintDefinition id : cdef) {
			String[] str2 = id.getExpression().split("[.]");
			
			constraintinfo=constraintinfo.concat(str2[1]).concat(" ");
			//System.out.println(id.getExpression());
		}
		str1[0]=constraintinfo.trim();
		List<InputDefinition> indef = condef.getInputs();
		
		String inputname = "";
		for(InputDefinition de: indef) {
			//System.out.println(de.getName());
			inputname = de.getName();
			//contractinfo=contractinfo.concat(inputname).concat("@");
			List<InputDefinition> dde = de.getInputs();
			for(InputDefinition dd : dde) {
				//System.out.print(dd.getName()+":"+dd.getType()+" ");
				String str = dd.getName()+":"+dd.getType()+" ";
				contractinfo=contractinfo.concat(str).concat(" ");
			}
			contractinfo=contractinfo.trim();
		}
		str1[1]=contractinfo.trim();
		return str1;
		
	}
	
	
	
	public Map<String,Serializable> getInput() throws Exception {
		
		ContractDefinition condef = processapi.getProcessContract(pid);
		List<ConstraintDefinition> cdef = condef.getConstraints();
		//System.out.println("\n\n");
		for(ConstraintDefinition id : cdef) {
			List<String> names = id.getInputNames();
			for(String i : names) {
				System.out.println(i);
			}
			System.out.println(id.getExpression());
		}
		
		//System.out.println(condef.toString());
		List<InputDefinition> indef = condef.getInputs();
		Map<String,Serializable> payload = new HashMap<String,Serializable>();
		Map<String,Serializable> payload2 = new HashMap<String,Serializable>();
		GetCurrentTime curtime = new GetCurrentTime();
		String curtime1 = curtime.getCurrentTime();
		String inputname = "";
		for(InputDefinition de: indef) {
			//System.out.println(de.getName());
			inputname = de.getName();
			List<InputDefinition> dde = de.getInputs();
			for(InputDefinition dd : dde) {
				System.out.print(dd.getName()+":"+dd.getType()+" ");
				String inputname2 = dd.getName();
				
				if(inputname2.contains("time")) {
					payload2.put(inputname2,curtime1);
				}else {
					payload2.put(inputname2,"");
				}
			}
			System.out.println();
			payload.put(inputname,(Serializable)payload2);
		}
		return payload2;
	}

}
