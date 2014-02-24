package dbFunctionality;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TempClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String ans = null;
//		System.out.print("Do you want to create locus database from scratch? [y/n]: ");
//		String ans = input.next();
//		if( ans.compareTo("y") == 0 ) {
//			DbCreator dbcreator = new DbCreator();
//			dbcreator.createDB();
//			System.out.println("Created db");
//		}
		
		System.out.print("Do you want to populate locus database? [y/n]: ");
		ans = input.next();
		if( ans.compareTo("y") == 0 ) {
			DbInserter dbinserter = new DbInserter();
			dbinserter.populateDB();
			System.out.println("Populated db");
		}
		input.close();

		
		
	} // end main
} // end class TempClass
