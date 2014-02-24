package kNN;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import attributeManipulation.AttributeReader;
import dataCreator.Tuple;

public class KNNClassifier {

	private ArrayList<NeighborDistance> neighbors = null;
	private AttributeReader reader = null;

	public KNNClassifier() {
		neighbors = new ArrayList<NeighborDistance>(Constants.k);
		reader = new AttributeReader(utils.Constants.ATTRIBUTES_FILE);
	}

	public boolean classify(Tuple p) {
		try {

			double qualifyingDist = Integer.MAX_VALUE;
			neighbors.clear();
			// Open the training set file
			FileInputStream fstream = new FileInputStream(Constants.TRAINING_FILE);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read file line by line
			if ((strLine = br.readLine()) != null ) {
				Tuple testP = new Tuple(strLine);
				double dist = p.compare(testP, reader);
				neighbors.add(new NeighborDistance(testP, dist));
				qualifyingDist = dist;
			}
			while ((strLine = br.readLine()) != null) {
				Tuple testP = new Tuple(strLine);
				double dist = p.compare(testP, reader);

				// if we need to remove a neighbor and add current tuple
				if ( dist < qualifyingDist && neighbors.size() == Constants.k ) {
					for ( int i = 0; i < neighbors.size(); i++ ) {
						if ( qualifyingDist == neighbors.get(i).getDistance() ) {
							neighbors.remove(i);
							neighbors.add(new NeighborDistance(testP, dist));
							qualifyingDist = dist;
							break;
						}
					}
					// update qualifying distance with new largest value 
					for ( int i = 0; i < neighbors.size(); i++ ) {
						if ( neighbors.get(i).getDistance() > qualifyingDist )
							qualifyingDist = neighbors.get(i).getDistance();
					}
				}
				// if the tuple needs to be inserted but not all neighbors have been found and there is no need to update qd
				else if (dist < qualifyingDist ) {
					neighbors.add(new NeighborDistance(testP, dist));
				}
				// if the tuple needs to be inserted but not all neighbors have been found and qd must be updated
				else if (dist > qualifyingDist && neighbors.size() < Constants.k ) {
					neighbors.add(new NeighborDistance(testP, dist));
					qualifyingDist = dist;
				}
			}
			in.close();

			//			System.out.println("Neighbor structure has been created");
			// TODO: mporw na enswmatwsw to metrima twn pos kai neg sto pio panw loop. axizei? 
			int counter = 0;
			for ( int i = 0; i < neighbors.size(); i++ ) {
				if ( neighbors.get(i).getClassP() )
					counter++;
				else
					counter--;
				//				System.out.println(neighbors.get(i).getDistance() + "    " + neighbors.get(i).getClassP());
			}
			if ( counter >= 0 )
			{
				System.out.println("Einai sto + me counter = " + counter);
				return true;
			}
			else
			{
				System.out.println("Einai sto - me counter = " + counter);
				return false;
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
 		KNNClassifier classifier = new KNNClassifier();

		FileInputStream fstream;
		int TP_yes = 0;
		int FP_yes = 0;
		int TN_yes = 0;
		int FN_yes = 0;
		int TP_no = 0;
		int FP_no = 0;
		int TN_no = 0;
		int FN_no = 0;
		double precision_yes = 0;  
		double recall_yes = 0;
		double accuracy_yes = 0;
		double precision_no = 0;
		double recall_no = 0;
		double accuracy_no = 0;
		try {
			fstream = new FileInputStream(Constants.TESTING_FILE);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i = 0;
			int j = 1;
			while ((strLine = br.readLine()) != null) {
				Tuple newTuple = new Tuple(strLine);
				System.out.println(j);
				boolean res = classifier.classify(newTuple);

				if (res) {
					if (newTuple.isClassP()) {
						TP_yes++;
						TN_no++;
						System.out.println("Eipa nai, einai nai");
					}
					else {
						FP_yes++;
						FN_no++;
						System.out.println("Eipa nai, einai oxi");
					}
				}
				else {
					if (newTuple.isClassP()) {
						FN_yes++;
						FP_no++;
						System.out.println("Eipa oxi, einai nai");
					}
					else {
						TN_yes++;
						TP_no++;
						System.out.println("Eipa oxi, einai oxi");
					}
				}
				//				if (newTuple.isClassP() == res)
				//				{
				//					i++;
				////					System.out.println("Tuple was classified correctly");
				//				}
				j++;
			}
			br.close();
			in.close();
			fstream.close();
			precision_yes = TP_yes/(double)(TP_yes + FP_yes);
			recall_yes = TP_yes/(double)(TP_yes + FN_yes);
			accuracy_yes = (TP_yes + TN_yes) / (double)(TP_yes + TN_yes + FP_yes + FN_yes);
			precision_no = TP_no/(double)(TP_no + FP_no);
			recall_no = TP_no/(double)(TP_no + FN_no);
			accuracy_no = (TP_no + TN_no) / (double)(TP_no + TN_no + FP_no + FN_no);

			
			
			System.out.println(i);
			//			System.out.println("Classified correctly " + i + " out of " + Constants.TESTING_SIZE + "(" + 100*(double)i/Constants.TESTING_SIZE + "% accuracy)");
			System.out.println("precision_yes: " + precision_yes);
			System.out.println("recall_yes: " + recall_yes);
			System.out.println("accuracy_yes: " + accuracy_yes);

			System.out.println("precision_no: " + precision_no);
			System.out.println("recall_no: " + recall_no);
			System.out.println("accuracy_no: " + accuracy_no);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
