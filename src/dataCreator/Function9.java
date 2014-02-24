package dataCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import attributeManipulation.AttributeReader;

public class Function9 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream ifstream;
		FileWriter ofstream;
		try {
			AttributeReader reader = new AttributeReader(utils.Constants.ATTRIBUTES_FILE);
			ifstream = new FileInputStream(utils.Constants.DATA_FILE);
			ofstream = new FileWriter("function9.txt");
			DataInputStream in = new DataInputStream(ifstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			BufferedWriter out = new BufferedWriter(ofstream);
			String strLine;
			// Read file line by line
			while ((strLine = br.readLine()) != null) {
				Tuple p = new Tuple(strLine);
				if (utils.Functions.function9(p))
					p.writeWithClass(out, true);
				else
					p.writeWithClass(out, false);
			}
			in.close();
			out.close();
			System.out.println("function9.txt has been successfully created");
		}
		catch (FileNotFoundException e) {
			System.err.println("An error occured while creating function9.txt. Please try again");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
