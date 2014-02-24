package decisionTree;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import dataCreator.Tuple;

import utils.MathFunctions;

import attributeManipulation.Attribute;

public class Node {
	private static int globalIndex = 0;

	private int index;
	private ArrayList<Tuple> tuples = null;
	private Attribute splitAttr = null;
	private boolean belongingClass;
	private boolean isLeaf;
	private ArrayList<Node> children = null;
	private ArrayList<Pair> minMaxVals = null;
	private Node parent = null;
	private double expectedError = Double.MAX_VALUE;
	private double backedUpError = Double.MAX_VALUE;
	private int level; /* for printBFS */
	private static int glevel = 0; /* for printBFS */
	private static int height;
	
	public Node() {
		isLeaf = false;
	}

	public Node(ArrayList<Tuple> D, ArrayList<Attribute> attributes, Node par) {
		index = globalIndex++;
		if ( height < glevel )
			height = glevel;
		level = glevel;
		parent = par;
		tuples = D;
		if ( D.isEmpty() ) {
			isLeaf = true;
			return;
		}
		if ( isPure(D) ) {
			isLeaf = true;
			belongingClass = D.get(0).isClassP();
			System.out.println(belongingClass + " (Pure) " + tuples.size());
			return;
		}
		if ( attributes.isEmpty() ) {
			isLeaf = true;
			belongingClass = findMajorityClass(D);
			System.out.println(belongingClass + " (Majority) " + tuples.size());
			return;
		}

		ArrayList<ArrayList<Tuple>> partitions = new ArrayList<ArrayList<Tuple>>();
		int pos = findBestAttribute(D, attributes, partitions, "info");
		splitAttr = attributes.get(pos);
		System.out.println(splitAttr.getAttrName());
		ArrayList<Attribute> attrsToPass = new ArrayList<Attribute>(attributes);
		attrsToPass.remove(splitAttr);
		if ( splitAttr.isDiscrete() ) {
			children = new ArrayList<Node>((int)splitAttr.getMaxValue() - (int)splitAttr.getMinValue() + 1);
			for ( int i = 0; i <= splitAttr.getMaxValue() - splitAttr.getMinValue(); i++ ) {
				glevel++;
				children.add(new Node(partitions.get(i), attrsToPass, this));
				glevel--;
				if ( partitions.get(i).isEmpty() ) {
					children.get(i).belongingClass = findMajorityClass(D);
					System.out.println(children.get(i).belongingClass + " (Empty)");
				}
			}
			System.out.println("^");
		}
		else {
			children = new ArrayList<Node>(2);
			glevel++;
			children.add(new Node(partitions.get(0), attrsToPass, this));
			glevel--;
			if ( partitions.get(0).isEmpty() ) {
				children.get(0).belongingClass = findMajorityClass(D);
				System.out.println(children.get(0).belongingClass + " (Empty)");
			}
			glevel++;
			children.add(new Node(partitions.get(1), attrsToPass, this));
			glevel--;
			if ( partitions.get(1).isEmpty() ) {
				children.get(1).belongingClass = findMajorityClass(D);
				System.out.println(children.get(1).belongingClass + " (Empty)");
			}
			System.out.println("^");
		}
		fillMinMaxVals();
	}

	private void fillMinMaxVals() {
		if ( splitAttr.isDiscrete() ) {
			minMaxVals = new ArrayList<Pair>((int)splitAttr.getMaxValue() - (int)splitAttr.getMinValue() + 1);
			int min = (int)splitAttr.getMinValue();
			int max = (int)splitAttr.getMaxValue();
			for (int i = min; i <= max; i++ ) {
				minMaxVals.add(new Pair(i, i));
			}
		}
		else {
			minMaxVals = new ArrayList<Pair>(2);
			minMaxVals.add(new Pair((int)Math.floor(splitAttr.getMinValue()), (int)Math.floor((splitAttr.getMinValue() + splitAttr.getMaxValue())/2.0)));
			minMaxVals.add(new Pair((int)Math.ceil((splitAttr.getMinValue() + splitAttr.getMaxValue())/2.0), (int)Math.ceil(splitAttr.getMaxValue())));
		}
	}

	public int getHeight() {
		return height;
	}

	private boolean isPure(ArrayList<Tuple> tuples) {
		boolean tClass = tuples.get(0).isClassP();
		for(int i = 1; i < tuples.size(); i++)
			if(tuples.get(i).isClassP() != tClass)
				return false;
		return true;
	}

	private boolean findMajorityClass(ArrayList<Tuple> tuples) {
		int count = 0;
		for(int i = 0; i < tuples.size(); i++) {
			if(tuples.get(i).isClassP())
				count++;
			else count--;
		}
		if(count >= 0)
			return true;
		return false;
	}

	public int findBestAttribute(ArrayList<Tuple> D, ArrayList<Attribute> attributes, ArrayList<ArrayList<Tuple>> partitions, String method) {
		int bestMatch = -1;
		double metric = 0, maxMetric = 0;
		ArrayList<ArrayList<Tuple>> tempPartitions = new ArrayList<ArrayList<Tuple>>();

		for(int i = 0; i < attributes.size(); i++) {
			if ( method == "info" ) {
				metric = informationGain(D, attributes.get(i), tempPartitions);
			}
			else if ( method == "gain" ) {
				metric = gainRatio(D, attributes.get(i), tempPartitions);
			}
			if( metric >= maxMetric || attributes.size() == 1 ) {
				maxMetric = metric;
				bestMatch = i;
				partitions.clear();
				partitions.addAll(tempPartitions);
			}
			tempPartitions.clear();
		}
		return bestMatch;
	}

	private float informationGain(ArrayList<Tuple> D, Attribute attr, ArrayList<ArrayList<Tuple>> partitions) {
		float info, infoA;
		float gain;
		info = info(D);
		infoA = infoA(D, attr, partitions);
		gain = info - infoA;
		if (gain < 0 && gain < -1E-10)
		{
			gain = 0;
		}
		else if (gain < 0)
			System.out.println("malakia");

		return gain;
	}

	private double gainRatio(ArrayList<Tuple> D, Attribute attr, ArrayList<ArrayList<Tuple>> partitions) {
		double splitA = splitInfoA(D, attr, partitions);
		double gainA = infoA(D, attr, partitions);
		double gainRatio = gainA/splitA;
		return gainRatio;
	}

	private double splitInfoA(ArrayList<Tuple> D, Attribute attr, ArrayList<ArrayList<Tuple>> partitions) {
		double splitA = 0;
		if(attr.isDiscrete()) {
			HashMap<Integer, ArrayList<Tuple>> map = new HashMap<Integer, ArrayList<Tuple>>((int)attr.getMaxValue() - (int)attr.getMinValue() + 1 );
			int min = (int)attr.getMinValue();
			int max = (int)attr.getMaxValue();
			for(int i = min; i <= max; i++) {
				ArrayList<Tuple> Dj = calcValuePartitionDiscrete(D, attr, i);
				map.put(i, Dj);
				partitions.add(Dj);
			}
			for ( int i = min; i <= max; i++ ) {
				splitA -= (map.get(i).size()/(double)D.size())*MathFunctions.log2(map.get(i).size()/(double)D.size());
			}
		}
		else {
			partitions.add(calcValuePartitionContinuous(D, attr, attr.getMinValue(), (attr.getMinValue() + attr.getMaxValue())/2.0));
			partitions.add(calcValuePartitionContinuous(D, attr, (attr.getMinValue() + attr.getMaxValue())/2.0, attr.getMaxValue() + 1));
			splitA = (partitions.get(0).size()/D.size())*MathFunctions.log2(partitions.get(0).size()/D.size()) + 
					(partitions.get(1).size()/D.size())*MathFunctions.log2(partitions.get(1).size()/D.size());  
		}

		return splitA;
	}

	private float info(ArrayList<Tuple> D) {
		float info;
		int yes = 0;
		int no = 0;
		if ( D.isEmpty() )
			return 0;
		for ( int i = 0; i < D.size(); i++ ) {
			if ( D.get(i).isClassP() ) 
				yes++;
			else no++;
		}
		info = (float) (-(float)yes*MathFunctions.log2((float)yes/(yes+no))/(yes+no) -
				(float)no*MathFunctions.log2((float)no/(yes+no))/(yes+no));

		if (info < 0)
		{
			System.out.println("skata");
		}
		return info;
	}

	private float infoA(ArrayList<Tuple> D, Attribute attr, ArrayList<ArrayList<Tuple>> partitions) {
		float infoA = 0;
		if(attr.isDiscrete()) {
			int min = (int)attr.getMinValue();
			int max = (int)attr.getMaxValue();
			HashMap<Integer, ArrayList<Tuple>> map = new HashMap<Integer, ArrayList<Tuple>>(max - min + 1 );
			for(int i = min; i <= max; i++) {
				ArrayList<Tuple> Dj = calcValuePartitionDiscrete(D, attr, i);
				map.put(i, Dj);
				partitions.add(Dj);
			}
			for ( int i = min; i <= max; i++ ) {
				infoA += info(map.get(i))*map.get(i).size()/D.size();
			}
		}
		else {
			ArrayList<Tuple> firstPartition = new ArrayList<Tuple>();
			ArrayList<Tuple> secondPartition = new ArrayList<Tuple>();

			partitionTuples(D, firstPartition, secondPartition, attr);
			infoA = info(firstPartition)*firstPartition.size()/D.size() + info(secondPartition)*secondPartition.size()/D.size();
			partitions.add(firstPartition);
			partitions.add(secondPartition);
		}

		return infoA;
	}

	private void partitionTuples(ArrayList<Tuple> D,
			ArrayList<Tuple> firstPartition, ArrayList<Tuple> secondPartition,
			Attribute attr) {

		int split = (int)Math.floor((attr.getMinValue() + attr.getMaxValue()) / 2.0);

		for (int i = 0; i < D.size(); i++) {
			if (D.get(i).getValue(attr.getIndex()) <= split)
				firstPartition.add(D.get(i));
			else
				secondPartition.add(D.get(i));
		}
	}

	private ArrayList<Tuple> calcValuePartitionContinuous(ArrayList<Tuple> D, Attribute attr, double minValue, double maxValue) {
		ArrayList<Tuple> Dj = new ArrayList<Tuple>();
		for(int i = 0; i < D.size(); i++) {
			if ( D.get(i).getValue(attr.getIndex()) >= minValue  && D.get(i).getValue(attr.getIndex()) < maxValue )
				Dj.add(D.get(i));
		}
		return Dj;
	}

	private ArrayList<Tuple> calcValuePartitionDiscrete(ArrayList<Tuple> D, Attribute attr, int value) {
		ArrayList<Tuple> Dj = new ArrayList<Tuple>();
		for(int i = 0; i < D.size(); i++) {
			if ( D.get(i).getValue(attr.getIndex()) == value )
				Dj.add(D.get(i));
		}
		return Dj;
	}

	public Node getChild(int pos) {
		return children.get(pos);
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public Node getParent() {
		return parent;
	}

	public double calculateError() {
		if ( children != null ) {
			for ( int i = 0; i < children.size(); i++ ) {
				children.get(i).calculateError();
			}
		}
		if ( isLeaf && expectedError < 0 ) {
			int errors = 0;
			int N = tuples.size();
			double f, z = 0.69, e;
			for ( int i = 0; i < tuples.size(); i++ ) {
				if ( tuples.get(i).isClassP() != belongingClass )
					errors++;
			}
			//			errors = 1; N = 2;
			f = 1 - errors/(double)N;
			//			f = (double)5/14;
			e = (f + z*z/(double)(2*N) + z*Math.sqrt(f/(double)N - f*f/(double)N + z*z/(double)(2*N*N)))/(double)(1 + z*z/(double)N);
			System.out.println(e);
		}
		System.out.println(index);
		return 0;
	}

	public boolean classify(Tuple testTuple) {
		while ( !isLeaf ) {
			int v = testTuple.getValue(splitAttr.getIndex());
			for ( int i = 0; i < minMaxVals.size(); i++ ) {
				if ( v >= minMaxVals.get(i).getFirst() && v <= minMaxVals.get(i).getSecond() ) {
					if ( tuples != null && isPure(tuples) )
						System.out.println("Pure");
					return children.get(i).classify(testTuple);
				}
			}
		}
		return belongingClass;
	}

	public void considerToPrune() {
		// TODO Auto-generated method stub
		if ( isLeaf )
			return;

		for ( int i = 0; i < children.size(); i++ ) {
			children.get(i).considerToPrune();
		}

		int classError = 0;
		int pruneError = 0;
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(Constants.PRUNING_SET))));
			String strLine;
			while ( (strLine = br.readLine()) != null ) {

				// First we classify the tuple using the already-built subtree
				Tuple tuple = new Tuple(strLine);
				boolean res = classify(tuple);
				if ( res != tuple.isClassP() )
					classError++;

				// Then we classify the tuple as if we had stopped the pruning at this level, i.e. using the majority class
				res = findMajorityClass(tuples);
				if ( res != tuple.isClassP() )
					pruneError++;
			}
			if ( classError >= pruneError )
				prune();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void considerToPruneWithoutSet() {
		if ( isLeaf ) {
			calculateExpectedError();
			return;
		}

		for ( int i = 0; i < children.size(); i++ ) {
			children.get(i).considerToPruneWithoutSet();
		}

		calculateBackedUpError();
		calculateExpectedError();

		if ( backedUpError >= expectedError )
			prune();
	}

	private void calculateBackedUpError() {
		if ( isLeaf )
			return;
		double error = 0;
		for (Node n : children) {
			error = error + n.tuples.size()*Math.min(n.expectedError, n.backedUpError)/(double)tuples.size() ;
		}
		backedUpError = error;
	}

	private void calculateExpectedError() {
		int n = countMajorityTuples();
		expectedError = (this.tuples.size() - n + 1)/(double)(this.tuples.size() + n);
	}

	private int countMajorityTuples() {
		int i = 0;
		for (Tuple t : tuples )
			if (t.isClassP() == belongingClass)
				i++;
		return i;
	}

	private void prune() {
		isLeaf = true;
		belongingClass = findMajorityClass(tuples);
		children = null;
		minMaxVals = null;
		splitAttr = null;
		backedUpError = Double.MAX_VALUE;
	}

	public void printDFS() {
		if ( !isLeaf ) {
			System.out.println(splitAttr.getAttrName() + "(" + tuples.size() + ")");
			for ( int i = 0; i < children.size(); i++ ) {
				children.get(i).printDFS();
			}
			System.out.println("^");
		}
		else {
			System.out.println(belongingClass + "(" + tuples.size() + ")");
		}
	}

	public void printBFS(int lev) {
		if ( lev == level ) {
			if ( isLeaf )
				System.out.print(belongingClass + "(" + tuples.size() + ") ");
			else
				System.out.print(splitAttr.getAttrName() + "(" + tuples.size() + ") ");
			return;
		}
		if ( isLeaf )
			return;
		for ( int i = 0; i < children.size(); i++ ) {
			children.get(i).printBFS(lev);
		}
		System.out.print("		");
	}
}
