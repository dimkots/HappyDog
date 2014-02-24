package dataCreator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import attributeManipulation.Attribute;
import attributeManipulation.AttributeReader;

public class Tuple {
	private int[] attrs = new int[utils.Constants.NO_OF_ATTRIBUTES];
	private boolean classP;

	public Tuple(String strLine) {
		String[] tokens = strLine.split(" ");

		int i;
		for ( i = 0; i < utils.Constants.NO_OF_ATTRIBUTES; i++ ) {
			attrs[i] = Integer.parseInt(tokens[i]);
		}
		
		if ( tokens.length == utils.Constants.NO_OF_ATTRIBUTES + 1 ) {
			int classP = Integer.parseInt(tokens[i]);
			if ( classP == 0 )
				this.classP = false;
			else this.classP = true;
		}
	}
	
	public Tuple(Random generator, ArrayList<Attribute> attributes) {

		for ( int i = 0; i < utils.Constants.NO_OF_ATTRIBUTES; i++ ) {
			Attribute attr = attributes.get(i);
			attrs[i] = generator.nextInt((int)attr.getMaxValue() + 1 - (int)attr.getMinValue()) + (int)attr.getMinValue();
		}
		
		/*******************************/
		/* CODE DATA CORRELATIONS HERE */
		/*******************************/
		if ( attrs[0] >= 75000 )
			attrs[1] = 0;
		attrs[6] = attrs[6] * attrs[5];
	}
	
	public int getValue(int pos) {
		return attrs[pos];
	}
	
	public boolean isClassP() {
		return classP;
	}
	
	public void write(BufferedWriter out) {
		// TODO Auto-generated method stub
		try {
			for ( int i = 0; i < attrs.length; i++ ) 
				out.write(attrs[i] + " " );
			out.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeWithClass(BufferedWriter out, boolean classP) {
		// TODO Auto-generated method stub
		try {
			for ( int i = 0; i < attrs.length; i++ ) 
				out.write(attrs[i] + " " );
			if ( classP )
				out.write("1");
			else out.write("0");
			out.write("\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double compare (Tuple p, AttributeReader reader) {
		double dist = 0;
		
		for ( int i = 0; i < attrs.length; i++ ) {
			if ( reader.isArithmetic(i))
				dist += Math.abs(attrs[i] - p.attrs[i])/(double)(reader.getMaxValue(i) - reader.getMinValue(i));
			else {
				//TODO: maybe if different dist += 0.5 to mitigate impact of categorical attributes?
				if ( attrs[i] != p.attrs[i] )
					dist += 1;
			}
		}		
		return dist;
	}
}
