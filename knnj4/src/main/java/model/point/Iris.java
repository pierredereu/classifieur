package model.point;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.column.IColumn;

public class Iris implements IPoint {
    protected double sepalLength;
    protected double sepalWidth;
    protected double petalLength;
    protected double petalWidth;
    protected IrisVariety classification;

    public Iris(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String variety) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.classification = IrisVariety.UNKNOWN;
        for(IrisVariety irisVariety : List.of(IrisVariety.SETOSA, IrisVariety.VERSICOLOR, IrisVariety.VIRGINICA)) {
            if(variety.equals(irisVariety.getVariety())){
                this.classification = irisVariety;
            }
        }
    }

    @Override
    public Object getValue(IColumn col) {
        for(Map.Entry<IPoint,Object> values: col.getColumnValues().entrySet()){
            if(this.equals(values.getKey())) return values.getValue();
        }
        return getNormalizedValue(col);
    }

    @Override
    public double getNormalizedValue(IColumn col) {
        return col.getNormalizedValue(this);
    }

    public double getSepalLength() {
        return this.sepalLength;
    }

    public double getSepalWidth() {
        return this.sepalWidth;
    }

    public double getPetalLength() {
        return this.petalLength;
    }

    public double getPetalWidth() {
        return this.petalWidth;
    }

    @Override
    public IrisVariety getClassification() {
        return this.classification;
    }

    @Override
    public String toString() {
        return 
        "               " +getSepalLength() + "                    " +
                getSepalWidth() + "                       " +
                getPetalLength() + "                         " +
                getPetalWidth() + "             " +
                getClassification().getVariety()
                ;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Iris)) {
            return false;
        }
        Iris iris = (Iris) o;
        return sepalLength == iris.sepalLength && sepalWidth == iris.sepalWidth && petalLength == iris.petalLength && petalWidth == iris.petalWidth && Objects.equals(classification, iris.classification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sepalLength, sepalWidth, petalLength, petalWidth, classification);
    }
 

	public void setClassification(IClassificationValue classification) {
		this.classification=(IrisVariety) classification;
	}


}
