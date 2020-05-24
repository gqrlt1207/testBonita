package xyg.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connect to a sample database
 */

public class queryData { 
	private String sqlcmd;
	
	queryData(String sqlcmd)  
	{
		this.sqlcmd = sqlcmd;
	}
	public void connect() {
	        Connection conn = null;
	        Statement stmt = null;
	        try {
	            // db parameters
	            String url = "jdbc:sqlite:/home/bgao/sqlite/testDB.db";
	            // create a connection to the database
	            conn = DriverManager.getConnection(url);
	            stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sqlcmd);
	            //System.out.println(rs);
	            while(rs.next()) {
	            	String incident=rs.getString("incidentnumber");
	            	String eventid=rs.getString("eventid");
	            	String eventdescription=rs.getString("eventdescription");
	            	String servername=rs.getString("servername");
	            	String ipaddress=rs.getString("ipaddress");
	            	String ostype=rs.getString("ostype");
	            	String createtime=rs.getString("createtime");
	            	String assignedgroup=rs.getString("assignedgroup");
	            	String status=rs.getString("status");
	            	String processedbybonita=rs.getString("processedbybonita");
	            	System.out.println(incident+","+eventid+","+eventdescription+","+servername+
	            			","+ipaddress+","+ostype+","+createtime+","+assignedgroup+","+status+","+processedbybonita);
	            }
	            rs.close();
	            stmt.close();
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

