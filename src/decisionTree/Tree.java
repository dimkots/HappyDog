package decisionTree;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dataCreator.Tuple;

import attributeManipulation.Attribute;
import attributeManipulation.AttributeReader;

public class Tree {
	private Node root = null;
	
	public Tree(ArrayList<Tuple> D, ArrayList<Attribute> attributes) {
		root = new Node(D, attributes, null);
	}
	
	public void prune() {
//		root.considerToPrune();
		root.considerToPruneWithoutSet();
		
	}
	
	public boolean classify(Tuple testTuple) {
		return root.classify(testTuple);
	}
	
	public static void main(String[] args) {
		AttributeReader reader = new AttributeReader(utils.Constants.ATTRIBUTES_FILE);
		
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(Constants.TRAINING_SET);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read file line by line
			while ((strLine = br.readLine()) != null ) {
				tuples.add(new Tuple(strLine));
			}
			in.close();
			
			System.out.println("Start construction");
			Tree id3 = new Tree(tuples, reader.attributes);
			System.out.println("Done constructing");
			
			id3.printDFS();
			
//			id3.printBFS();
			
			System.out.println("Start prune");
			id3.prune();
			System.out.println("Done pruning");
			
//			id3.printDFS();
			
//			id3.printBFS();

			
/*			System.out.print("Enter the tuple that you want classified: ");

			br = new BufferedReader(new InputStreamReader(System.in));
			try {
				while ( (strLine = br.readLine()) != null ) {
					Tuple tuple = new Tuple(strLine);
					System.out.println(id3.classify(tuple));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printDFS() {
		// TODO Auto-generated method stub
		root.printDFS();
	}
	
	private void printBFS() {
		for ( int i = 0; i <= root.getHeight(); i++ ) {
			System.out.print(i);
			root.printBFS(i);
			System.out.println();
		}
	}
}
