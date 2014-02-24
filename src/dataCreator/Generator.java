package dataCreator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import attributeManipulation.AttributeReader;

public class Generator {
	//	static ArrayList<Person> myList = new ArrayList<Person>();
	static Random generator;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO: use random seed
		generator = new Random(19580427);

		FileWriter fstream;
		try {
			fstream = new FileWriter("data.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			AttributeReader reader = new AttributeReader(Constants.ATTRIBUTES_FILE);
			for ( int i = 0; i < Constants.total; i++ ) {
				Tuple p = new Tuple(generator, reader.attributes);
				p.write(out);
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
