package dbFunctionality;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import javax.sql.rowset.JdbcRowSet;

import locus.Constants;

import attributeManipulation.AttributeReader;

import com.sun.rowset.JdbcRowSetImpl;

import dataCreator.Tuple;



public class DbInserter{
	private Connection connection = null;
	
	public DbInserter() {			
		try{
//			Class.forName(DbConstants.DRIVER).newInstance();
//			connection = DriverManager.getConnection(DbConstants.DATABASE_URL,DbConstants.USERNAME,DbConstants.PASSWORD);
			Class.forName(DbConstants.DRIVER).newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://localhost/locus", DbConstants.USERNAME, DbConstants.PASSWORD);
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void populateDB() {
		Statement statement = null; // query statement
		AttributeReader reader = null;
		Scanner scanner = null;
		JdbcRowSet insertRowSet = null;
		try {
			reader = new AttributeReader(utils.Constants.ATTRIBUTES_FILE);
			statement = connection.createStatement();

			statement.executeUpdate("USE " + DbConstants.DBNAME);
			statement.executeUpdate("DROP TABLE IF EXISTS " + DbConstants.TABLE);
			String query = "CREATE TABLE IF NOT EXISTS " + DbConstants.TABLE + "(id INT NOT NULL AUTO_INCREMENT";
			for ( int i = 0; i < utils.Constants.NO_OF_ATTRIBUTES; i++ ) {
				query += ", " + reader.getAttrName(i) + " INT";
			}
			query += ", classP TINYINT, PRIMARY KEY(id))";
			statement.executeUpdate(query);

			insertRowSet = new JdbcRowSetImpl();
			insertRowSet.setUrl(DbConstants.DATABASE_URL);
			insertRowSet.setUsername(DbConstants.USERNAME);
			insertRowSet.setPassword(DbConstants.PASSWORD);
			insertRowSet.setCommand("SELECT * FROM " + DbConstants.TABLE);
			insertRowSet.execute();

			// Read file line by line
			
			File file = new File(DbConstants.DATAFILE);			
			scanner = new Scanner(file);
			while((scanner.hasNextLine()) && scanner.hasNextInt()) {
//				strLine = scanner.nextLine();
				insertRowSet.moveToInsertRow();
				for ( int i = 0; i < utils.Constants.NO_OF_ATTRIBUTES; i++ ) {
					insertRowSet.updateInt(reader.getAttrName(i), scanner.nextInt());
				}
				insertRowSet.updateInt("classP", scanner.nextInt());
				insertRowSet.insertRow();
			}
			System.out.println("Populated " + DbConstants.TABLE);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (insertRowSet != null ) {
				try {
					insertRowSet.close();
				} catch (SQLException e) {
				} // nothing we can do
			}
			if (scanner != null) {
				try {
					scanner.close();
				} catch (IllegalStateException e) {
				} // nothing we can do
			}
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