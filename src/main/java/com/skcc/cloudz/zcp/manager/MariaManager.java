package com.skcc.cloudz.zcp.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MariaManager {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MariaManager.class);

	@Value("${props.mariaDB.url}")
	private String url;
	@Value("${props.mariaDB.id}")
	private String id;
	@Value("${props.mariaDB.password}")
	private String password;

	@SuppressWarnings("unchecked")
	public JSONArray getAlertHistoryList() {
		JSONArray jsonArr = new JSONArray();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection(url, id, password);

			if (con != null) {
				String sql = "SELECT seq, alert, datetime FROM history ORDER BY seq DESC LIMIT 10";
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();

				JSONObject jsonObj = new JSONObject();

				while (rs.next()) {
					JSONParser jsonParser = new JSONParser();
					jsonObj = (JSONObject) jsonParser.parse(rs.getString("alert"));

					if (jsonObj != null) {
						jsonArr.add(jsonObj.get("alerts"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return jsonArr;
	}

}
