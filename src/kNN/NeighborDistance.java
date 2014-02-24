package kNN;

import dataCreator.Tuple;

public class NeighborDistance {
	private Tuple neighbor;
	private double distance;
	
	public NeighborDistance(Tuple p, double dist) {
		neighbor = p;
		distance = dist;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public boolean getClassP() {
		return neighbor.isClassP();
	}

}
