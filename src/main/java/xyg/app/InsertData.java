package xyg.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Connect to a sample database
 */

public class InsertData { 
	private Map<String,String> data;
	
	InsertData(Map<String,String> data)  
	{
		this.data = data;
	}
	public void connect() throws IOException{
	        Connection conn = null;
	        Statement stmt = null;
	        try {
	            // db parameters
	            String url = "jdbc:sqlite:/home/bgao/sqlite/testDB.db";
	            // create a connection to the database
	            conn = DriverManager.getConnection(url);
	            conn.setAutoCommit(false);
	            stmt = conn.createStatement();
	            
	            String incident=data.get("incident");
	            String eventid=data.get("ShortDescription");
	            String eventdescription=data.get("ProblemDescription");
	            String servername=data.get("servername");
	            String ipaddress=data.get("ipaddress");
	            String ostype=data.get("ostype");
	                                 
	            String createtime=data.get("openedDateStamp");
	            String assignedgroup=data.get("AssignedGroup");
	            String status=data.get("SnowStatus");
	            String processedbybonita="no";
	            String sqlcmd = "INSERT INTO ticketinfo values (";
	            sqlcmd=sqlcmd.concat("\"").concat(incident).concat("\",\"").concat(eventid).concat("\",\"")
	            		.concat(eventdescription).concat("\",\"").concat(servername).concat("\",\"")
	            		.concat(ipaddress).concat("\",\"").concat(ostype).concat("\",\"").concat(createtime)
	            		.concat("\",\"").concat(assignedgroup).concat("\",\"").concat(status).concat("\",\"")
	            		.concat(processedbybonita).concat("\");");
	            System.out.println(sqlcmd);
	            stmt.executeUpdate(sqlcmd);
	            //if(status.indexOf("Resolved")==-1 && status.indexOf("Closed")==-1 && assignedgroup.indexOf("ITS OCC")==-1) {
	            	CallBonitaProcess callbonita = new CallBonitaProcess(data);
	            	callbonita.startBonitaProcess();
	            
	            //}
	            stmt.close();
	            conn.commit();
	            conn.close();
	            System.out.println("Connection to SQLite has been established.");
	            
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            try {
	                if (conn != null) {
	                    conn.close();
	                }
	            } catch (SQLException ex) {
	                System.out.println(ex.getMessage());
	            }
	        }
	}
	/**
	* @param args the command line arguments
	 
	public static void main(String[] args) {
	        connect();
	}*/
}


