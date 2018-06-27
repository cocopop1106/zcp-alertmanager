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
//	private String url = "jdbc:mariadb://webhook-mariadb:3306/alertmanager";
	@Value("${props.mariaDB.id}")
	private String id;
//	private String id = "root";
	@Value("${props.mariaDB.password}")
	private String password;
//	private String password = "admin";

	@SuppressWarnings("unchecked")
	public JSONArray getAlertHistoryList(String time) {
		JSONArray jsonArr = new JSONArray();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT seq, alert, datetime FROM history WHERE datetime >= date_add(now(), interval -1 hour)";

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection(url, id, password);

			if (con != null) {
				if ("1".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE datetime >= date_add(now(), interval -1 hour)";
				} else if ("2".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE date_format(datetime, '%Y-%m-%d') = current_date ";
				} else if ("3".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE date_format(datetime, '%Y-%m-%d') = CURDATE()-INTERVAL 1 DAY";
				} else if ("4".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE datetime >= date_add(now(), interval -2 day)";
				} else if ("5".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE YEARWEEK(datetime) = YEARWEEK(now())";
				} else if ("6".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE datetime >= curdate()-INTERVAL DAYOFWEEK(curdate())+6 DAY AND datetime < curdate()-INTERVAL DAYOFWEEK(curdate())-1 DAY";
				} else if ("7".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE date_format(datetime, '%Y-%m-%d') >= LAST_DAY(NOW() - INTERVAL 1 MONTH) AND date_format(datetime, '%Y-%m-%d') <= last_day(now())";
				} else if ("8".equals(time)) {
					sql = "SELECT seq, alert, datetime FROM history WHERE date_format(datetime, '%Y-%m-%d') >= last_day(now() - INTERVAL 2 MONTH) AND date_format(datetime, '%Y-%m-%d') <= last_day(now() - INTERVAL 1 MONTH)";
				}

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
