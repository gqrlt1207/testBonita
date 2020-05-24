package xyg.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor;
import org.bonitasoft.engine.bpm.process.ActivationState;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;

/** List deployed process **/
public class GetAllProcess {
	private APISession apiSession;
	
	public GetAllProcess(APISession apiSession) {
		this.apiSession = apiSession;
	}
	
	public HashMap<String,String>  getDeployedProcess() throws Exception{
	final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
	final SearchOptions searchOptions = new SearchOptionsBuilder(0, 100).sort(ProcessDeploymentInfoSearchDescriptor.DEPLOYMENT_DATE, Order.DESC).done();
	final SearchResult<ProcessDeploymentInfo> deploymentInfoResults = processAPI.searchProcessDeploymentInfos(searchOptions);
	HashMap<String,String> hmap = new HashMap<String,String>();
	for(ProcessDeploymentInfo pdi: deploymentInfoResults.getResult()) {
		String pname = pdi.getDisplayName();
		long pid = pdi.getProcessId();
		String version = pdi.getVersion();
		ActivationState status = pdi.getActivationState();
		String details = String.valueOf(pid)+" "+version+" "+String.valueOf(status);
		hmap.put(pname,details);
	}
	return hmap;
	}
	
	public List<HashMap<String,String>> getallprocessinfo() throws Exception{
		List<HashMap<String,String>> lst1 = new ArrayList<HashMap<String,String>>();
		final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
		final SearchOptions searchOptions = new SearchOptionsBuilder(0, 100).sort(ProcessDeploymentInfoSearchDescriptor.DEPLOYMENT_DATE, Order.DESC).done();
		final SearchResult<ProcessDeploymentInfo> deploymentInfoResults = processAPI.searchProcessDeploymentInfos(searchOptions);
		
		for(ProcessDeploymentInfo pdi: deploymentInfoResults.getResult()) {
			HashMap<String,String> hmap = new HashMap<String,String>();
			String pname = pdi.getDisplayName();
			long pid = pdi.getProcessId();
			String version = pdi.getVersion();
			ActivationState status = pdi.getActivationState();
			//String details = String.valueOf(pid)+" "+version+" "+String.valueOf(status);
			hmap.put("processName",pname);
			hmap.put("processId",String.valueOf(pid));
			hmap.put("version",version);
			hmap.put("status", status.toString());
			String[] str1 = new String[2];
			str1=new getContract(pid,processAPI).getcontractinfo();
			hmap.put("constraintInfo", str1[0]);
			hmap.put("contractInfo", str1[1]);
			lst1.add(hmap);
					
		}
		return lst1;
	}
}
