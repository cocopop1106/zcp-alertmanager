package com.skcc.cloudz.zcp.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MariaManager {
	private static Logger logger = Logger.getLogger(MariaManager.class);
	private String str, receiveMsg;
	
	@Value("${props.mariaDB.url}") private String url;
	@Value("${props.mariaDB.id}") private String id;
	@Value("${props.mariaDB.password}") private String password;
	
	public JSONObject getAlertHistoryList() {
		JSONObject jsonObj = new JSONObject();
		
		Connection con = null;
        PreparedStatement pstmt = null;   
        ResultSet rs = null;
		
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            
            con = DriverManager.getConnection(url, id, password);
            if( con != null ){ System.out.println("database connected!"); }
            
            String sql = "select seq, alert, datetime from history";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
            	System.out.println(rs.getString("seq"));
            	System.out.println(rs.getString("alert"));
            	System.out.println(rs.getString("datetime"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(con != null) con.close(); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return jsonObj;
	}

}
