package top.ruandb.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcUtils {

	//Greenplum
	private static String greenplum_driver="com.pivotal.jdbc.GreenplumDriver";
	private static String greenplum_url="jdbc:pivotal:greenplum://192.168.43.20:5432;DatabaseName=WYDB";
	private static String greenplum_user="sa";
	private static String greenplum_password="123456";
	
	
	public static Connection getGreenplumConnection() throws SQLException, ClassNotFoundException {
		Class.forName(greenplum_driver);
		Connection con = DriverManager.getConnection(greenplum_url, greenplum_user, greenplum_password);
		return con;
	}
	
	public static void main(String[] args) {
		try {
			Connection con = JdbcUtils.getGreenplumConnection();
			PreparedStatement ptsm = con.prepareStatement("select usp_fact_log_test()");
			ptsm.execute();
			
			System.out.println("核心数据区连接成功.....");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
