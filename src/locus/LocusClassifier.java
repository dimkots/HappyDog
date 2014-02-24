package locus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JdbcRowSet;
import javax.swing.text.AbstractDocument.BranchElement;

import sun.misc.IOUtils;
import attributeManipulation.Attribute;
import attributeManipulation.AttributeReader;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.JdbcRowSetImpl;
//import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;








import dataCreator.Tuple;


public class LocusClassifier {

	private AttributeReader reader;
	ArrayList<String> trainingSet = null;

	public LocusClassifier() {
		reader = new AttributeReader(utils.Constants.ATTRIBUTES_FILE);
	}

	private ArrayList<Attribute> chooseBestAttrs() {
		ArrayList<Attribute> bestAttrs = new ArrayList<Attribute>();
		if ( Constants.DATAFILE.compareTo("function1.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("age"));
		}
		else if ( Constants.DATAFILE.compareTo("function2.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("age"));
			bestAttrs.add(reader.getAttribute("salary"));
		}
		else if ( Constants.DATAFILE.compareTo("function3.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("age"));
			bestAttrs.add(reader.getAttribute("elevel"));
		}
		else if ( Constants.DATAFILE.compareTo("function4.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("age"));
			bestAttrs.add(reader.getAttribute("elevel"));
			bestAttrs.add(reader.getAttribute("salary"));
		}
		else if ( Constants.DATAFILE.compareTo("function5.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("age"));
			bestAttrs.add(reader.getAttribute("salary"));
			bestAttrs.add(reader.getAttribute("loan"));
		}
		else if ( Constants.DATAFILE.compareTo("function6.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("age"));
			bestAttrs.add(reader.getAttribute("salary"));
			bestAttrs.add(reader.getAttribute("commission"));
		}
		else if ( Constants.DATAFILE.compareTo("function7.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("salary"));
			bestAttrs.add(reader.getAttribute("commission"));
			bestAttrs.add(reader.getAttribute("loan"));
		}
		else if ( Constants.DATAFILE.compareTo("function8.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("salary"));
			bestAttrs.add(reader.getAttribute("commission"));
			bestAttrs.add(reader.getAttribute("elevel"));
		}
		else if ( Constants.DATAFILE.compareTo("function9.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("salary"));
			bestAttrs.add(reader.getAttribute("commission"));
			bestAttrs.add(reader.getAttribute("elevel"));
			bestAttrs.add(reader.getAttribute("loan"));
		}
		else if ( Constants.DATAFILE.compareTo("function10.txt") == 0 ) {
			bestAttrs.add(reader.getAttribute("hyears"));
			bestAttrs.add(reader.getAttribute("hvalue"));
			bestAttrs.add(reader.getAttribute("salary"));
			bestAttrs.add(reader.getAttribute("commission"));
			bestAttrs.add(reader.getAttribute("elevel"));
		}
		return bestAttrs;
	}
	/*	
	private void classify(Tuple tuple) {
		ArrayList<Attribute> bestAttrs = chooseBestAttrs();
		CachedRowSet crs = null;
		try {
			crs = new CachedRowSetImpl();
			crs.setUsername(Constants.USERNAME);
			crs.setPassword(Constants.PASSWORD);
			crs.setUrl(Constants.DATABASE_URL);

			String cmd = "SELECT classP, count(*) FROM " + Constants.TABLE + " WHERE ";
			for ( int i = 0; i < bestAttrs.size(); i++ ) {
				if ( i > 0 )
					cmd += " AND ";
				Attribute attr = bestAttrs.get(i);
				int x = tuple.getValue(bestAttrs.get(i).getIndex());
				String f = bestAttrs.get(i).getAttrName();
				if ( attr.isArithmetic() ) {
					double d = (attr.getMinValue() + attr.getMaxValue())/200.0;		// neighbourhood volume
					cmd += f + " >= " + (x - d) + " AND " + f + " <= " + (x + d);
				}
				else {
					cmd += f + " = " + x;
				}
			}
			cmd += " GROUP BY classP";

			crs.setCommand(cmd);
			crs.execute();
			int res = 0;
			while(crs.next()) {
				System.out.println(crs.getInt("classP") + " " + crs.getInt("count(*)"));
				if ( crs.getInt("classP") == 0 )
					res -= crs.getInt("count(*)");
				else
					res += crs.getInt("count(*)");
			}
			if ( res >= 0 )
				System.out.println("Positive");
			else
				System.out.println("Negative");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				crs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	 */

	private LocusEval classify(Tuple tuple, ArrayList<Attribute> attrs) {
		CachedRowSet crs = null;
		LocusEval eval = new LocusEval();
		try {
			crs = new CachedRowSetImpl();
			crs.setUsername(Constants.USERNAME);
			crs.setPassword(Constants.PASSWORD);
			crs.setUrl(Constants.DATABASE_URL);

			String cmd;
			if ( attrs.size() == 0) 
				cmd = "SELECT classP, count(*) FROM " + Constants.TABLE;
			else {
				cmd = "SELECT classP, count(*) FROM " + Constants.TABLE + " WHERE ";
				for ( int i = 0; i < attrs.size(); i++ ) {
					if ( i > 0 )
						cmd += " AND ";
					Attribute attr = attrs.get(i);
					int x = tuple.getValue(attrs.get(i).getIndex());
					String f = attrs.get(i).getAttrName();
					if ( attr.isArithmetic() ) {
						double d = (attr.getMinValue() + attr.getMaxValue())/200.0;
						cmd += f + " >= " + (x - d) + " AND " + f + " <= " + (x + d);
					}
					else {
						cmd += f + " = " + x;
					}
				}
			}
			cmd += " GROUP BY classP";

			crs.setCommand(cmd);
			crs.execute();
			int resPos = 0;
			int resNeg = 0;
			while(crs.next()) {
				//				System.out.println(crs.getInt("classP") + " " + crs.getInt("count(*)"));
				if ( crs.getInt("classP") == 0 )
					resNeg = crs.getInt("count(*)");
				else
					resPos = crs.getInt("count(*)");
			}
			if ( resPos >= resNeg ) {
				//				System.out.println("Positive");
				eval.setClassP(true);
				if ( resPos/((double)(resPos + resNeg)) >= Constants.THRESHOLD )
					eval.setPure(true);
				else 
					eval.setPure(false);
			}
			else {
				//				System.out.println("Negative");
				eval.setClassP(false);
				if ( resNeg/((double)(resPos + resNeg)) >= Constants.THRESHOLD )
					eval.setPure(true);
				else 
					eval.setPure(false);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				crs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return eval;
	}

	private static ArrayList<ArrayList<Attribute>> findAllSubsets(ArrayList<Attribute> attrs)
	{
		ArrayList<ArrayList<Attribute>> allSubsets = new ArrayList<ArrayList<Attribute>>((int)Math.pow(2, attrs.size()));
		int subsetCount = (int)Math.pow(2, attrs.size());

		for (int i = 0; i < subsetCount; i++)
		{
			ArrayList<Attribute> subset = new ArrayList<Attribute>();
			for (int bitIndex = 0; bitIndex < attrs.size(); bitIndex++)
			{
				if (GetBit(i, bitIndex) == 1)
				{
					subset.add(attrs.get(bitIndex));
				}
			}

			allSubsets.add(subset);
		}

		return allSubsets;
	}

	private static int GetBit(int value, int position)
	{
		int bit = value & (int)Math.pow(2, position);
		return (bit > 0 ? 1 : 0);
	}

	private ArrayList<Attribute> selectFeatures() {
		ArrayList<ArrayList<Attribute>> allSubsets = findAllSubsets(reader.getAllAttributes());
		int start = 0;
		int end = Constants.TRAINING_SIZE;
		int size = 0;
		int[] locmaxnode = new int[Constants.MAXCYCLES];
		createTrainingSet("function1.txt", start, end);
		for ( int cycles = 0; cycles < Constants.MAXCYCLES; cycles++ ) {
			int[] p = new int[allSubsets.size()];	// purity of FS
			int[] a = new int[allSubsets.size()];	// accuracy of FS
			double[] s = new double[allSubsets.size()];	// support of FS
			double[] c = new double[allSubsets.size()];	// confidence of FS
			double[] r = new double[allSubsets.size()];	// reliability of FS
			for ( int i = 0; i < Constants.TRAINING_SIZE; i++ ) {
				Tuple t = new Tuple(trainingSet.get(i));
				for (int j = 0; j < allSubsets.size(); j++) {
					LocusEval eval = classify(t, allSubsets.get(j));
					if (eval.isPure())
						p[j]++;
					if (t.isClassP() == eval.getClassP())
						a[j]++;
					System.out.println("p[" + j + "] = " + p[j] + ", a[" + j + "] = " + a[j]);
				}
				System.out.println("Tuple " + i);
			}
			System.out.println("Finished tuples");
			for (int j = 1; j < allSubsets.size(); j++) {
				s[j] = (double)p[j]/Constants.TRAINING_SIZE;
				c[j] = (double)a[j]/p[j];
				if (s[j] > Constants.T)
					r[j] = c[j];
				else
					r[j] = 0;
			}
			locmaxnode[size] = 0;
			double locmax = r[0];
			for ( int j = 1; j < allSubsets.size(); j++) {
				if ( r[j] > locmax ) {
					locmax = r[j];
					locmaxnode[size] = j;
				}
			}
			boolean foundGlobal = true;
			if (size >= Constants.CYCLES - 1) {
				for (int k = 1; k < Constants.CYCLES; k++ ) {
					if ( locmaxnode[size-k] != locmaxnode[size-k+1]) {
						foundGlobal = false;
						break;
					}
				}
				if (foundGlobal)
					break;
			}
			size++;
			start = end;
			end = start + Constants.TRAINING_SIZE;
			System.out.println("Starting new cycle");
		}
		return allSubsets.get(locmaxnode[size]);
	}

	private void createTrainingSet(String fileName, int start, int end) {
		trainingSet = new ArrayList<String>(Constants.TRAINING_SIZE);

		FileInputStream fs;
		BufferedReader br = null;
		try {
			fs = new FileInputStream(fileName);
			br = new BufferedReader(new InputStreamReader(fs));
			for(int i = 0; i < start; ++i)
				br.readLine();
			for (int i = start; i <end; i++)
			{
				trainingSet.add(br.readLine());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	public static void main(String[] args) {
		LocusClassifier locus = new LocusClassifier();
		locus.selectFeatures();

		/*
		System.out.print("Enter the tuple that you want classified: ");

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String strLine;
		try {
			while ( (strLine = br.readLine()) != null ) {
				Tuple tuple = new Tuple(strLine);
				locus.classify(tuple);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
