package dbFunctionality;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbCreator {
	private Connection connection = null; // manages connection
	
	public DbCreator()
	{
		try {
			Class.forName(DbConstants.DRIVER).newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://localhost/locus",DbConstants.USERNAME,DbConstants.PASSWORD);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createDB()
	{
		Statement statement = null; // query statement
		try {
			statement = connection.createStatement();
			statement.executeUpdate("DROP DATABASE IF EXISTS " + DbConstants.DBNAME);
			statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DbConstants.DBNAME);
			statement.executeUpdate("USE " + DbConstants.DBNAME);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null ) {
				try {
					statement.close();
				} catch (SQLException e) {
				} // nothing we can do
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
				} // nothing we can do
			}
		}
	}
}
