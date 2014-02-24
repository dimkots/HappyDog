package attributeManipulation;

public class Attribute {
	private String attrName;
	private int index;
	private boolean arithmetic;
	private boolean ordered;
	private boolean discrete;
	private double minValue;
	private double maxValue;
	
	public Attribute(String attrName, int index, boolean arithmetic,
			boolean ordered, boolean discrete, double minValue, double maxValue) {
		super();
		this.attrName = attrName;
		this.index = index-1;
		this.arithmetic = arithmetic;
		this.ordered = ordered;
		this.discrete = discrete;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public String getAttrName() {
		return attrName;
	}
	
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public boolean isArithmetic() {
		return arithmetic;
	}
	
	public void setArithmetic(boolean arithmetic) {
		this.arithmetic = arithmetic;
	}
	
	public boolean isOrdered() {
		return ordered;
	}
	
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}
	
	public boolean isDiscrete() {
		return discrete;
	}

	public void setDiscrete(boolean discrete) {
		this.discrete = discrete;
	}

	public double getMinValue() {
		return minValue;
	}
	
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	
	public double getMaxValue() {
		return maxValue;
	}
	
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	
	public void print() {
		System.out.println(attrName + " " + index + " " + 
				arithmetic + " " + ordered + " " + discrete + 
				" " + minValue + " " + maxValue);
	}
}
