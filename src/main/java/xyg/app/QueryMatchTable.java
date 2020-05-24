package xyg.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Connect to a sample database
 */

public class QueryMatchTable { 
	private String alerttype;
	private String ostype;
	
	QueryMatchTable(String alerttype,String ostype){
		this.alerttype = alerttype;
		this.ostype = ostype;
	}
	
	public String[] getMatchInfo() {
	        Connection conn = null;
	        Statement stmt = null;
	        String bonitaprocess = "nodata";
	        String bonitaprocessversion = "nodata";
	        String[] processinfo = new String[2];
	        processinfo[0] = "nodata";
	        processinfo[1] = "nodata";
	        try {
	            // db parameters
	            String url = "jdbc:sqlite:/home/bgao/sqlite/testDB.db";
	            String sql = "SELECT * FROM alertProcessMatch where ";
	            sql = sql.concat("alerttype=\'").concat(alerttype).concat("\' and ostype=\'")
	            		.concat(ostype).concat("\'");
	            // create a connection to the database
	            System.out.println("\n\nBonitaProcess search: "+sql+"\n\n");
	            conn = DriverManager.getConnection(url);
	            stmt = conn.createStatement();
	            ResultSet rs = stmt.executeQuery(sql);
	            //System.out.println(rs);
	            while(rs.next()) {
	            	bonitaprocess=rs.getString("bonitaprocess");
	            	bonitaprocessversion=rs.getString("bonitaprocessversion");
	            	System.out.println("\nBonita process search result: "+bonitaprocess+","+bonitaprocessversion);	
	            	break;
	            }
	            rs.close();
	            stmt.close();
	            System.out.println("Connection to SQLite has been established.");
	            
	            processinfo[0] = bonitaprocess;
	            processinfo[1] = bonitaprocessversion;
	            return processinfo;
	            
	            
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
	        return processinfo;
	}
	/**
	* @param args the command line arguments
	 
	public static void main(String[] args) {
	        connect();
	}*/
}


