package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CreateConnection {
	private static Connection conn = null;
	private static FileInputStream file;
	private static String url;
	private static String username;
	private static String password;
	
	public static Connection databaseConnection(String databaseConfiguration) throws IOException {
		
		Properties properties = new Properties();
		file = new FileInputStream(databaseConfiguration);
		properties.load(file);
		
		try {
			Class.forName(properties.getProperty("db.classname"));
			url = properties.getProperty("db.url");
			username = properties.getProperty("db.username");
			password = properties.getProperty("db.password");
			conn = DriverManager.getConnection(url, username, password);
			
		}catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} 
		return conn;
	}
}
