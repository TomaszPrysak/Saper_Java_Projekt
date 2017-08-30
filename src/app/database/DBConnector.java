package app.database;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnector {
	
	public Connection Connection() throws ClassNotFoundException, SQLException{
	
		Class.forName("com.mysql.jdbc.Driver");
		
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/saper_database","root","Pitek1962mySQLF700Gs");
		
	}
}