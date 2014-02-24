package attributeManipulation;

import java.io.BufferedReader;  
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import utils.Constants;

public class AttributeReader {
	public ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
	public Attribute getAttribute(String attrName) {
		for ( int i = 0; i < attributes.size(); i++ ) {
			if ( attributes.get(i).getAttrName().compareTo(attrName) == 0 )
				return attributes.get(i);
		}
		//TODO: throw exception
		return null;
	}
	
	public int getAttributesSize() {
		return attributes.size();
	}

	public ArrayList<Attribute> getAllAttributes() {
		return attributes;
	}

	public double getMinValue(int pos) {
		return attributes.get(pos).getMinValue();
	}

	public double getMaxValue(int pos) {
		return attributes.get(pos).getMaxValue();
	}

	public boolean isArithmetic(int pos) {
		return attributes.get(pos).isArithmetic();
	}
	
	public String getAttrName(int pos) {
		return attributes.get(pos).getAttrName();
	}

	public AttributeReader(String fileName) {
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				Scanner scanner = new Scanner(strLine);
				Attribute attribute = new Attribute(scanner.next(), scanner.nextInt(), 
						scanner.nextBoolean(), scanner.nextBoolean(), scanner.nextBoolean(), 
						scanner.nextDouble(), scanner.nextDouble());
				attributes.add(attribute);
				scanner.close();
			}
			Constants.NO_OF_ATTRIBUTES = attributes.size();
			in.close();
			//			System.out.println("Attributes read.");
		} // end try
		catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		} // end catch
	}
	/*
	public static void main(String[] args) {
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream("attributes.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				Scanner scanner = new Scanner(strLine);
				Attribute attribute = new Attribute(scanner.next(), scanner.nextInt(), 
						scanner.nextBoolean(), scanner.nextBoolean(), scanner.nextBoolean(), 
						scanner.nextInt(), scanner.nextInt());
				attributes.add(attribute);
			}
			//Close the input stream
			in.close();
			System.out.println("Attributes read.");
//			for ( int i = 0; i < attributes.size(); i++ ) {
//				attributes.get(i).print();
//			}
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	 */
}
