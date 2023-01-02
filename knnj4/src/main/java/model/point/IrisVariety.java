package model.point;


public enum IrisVariety implements IClassificationValue{
    UNKNOWN("Unknown"),
	SETOSA("Setosa"),
	VERSICOLOR("Versicolor"),
	VIRGINICA("Virginica");

	private String variety;

	IrisVariety(String variety) {
		this.variety=variety;
	}

	public String getVariety() {
		return variety;
	}

}
