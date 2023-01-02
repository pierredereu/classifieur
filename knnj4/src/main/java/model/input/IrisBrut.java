package model.input;

import com.opencsv.bean.CsvBindByName;

public class IrisBrut implements IPointBrut{
    @CsvBindByName(column = "sepal.length")
    protected double sepalLength;
    @CsvBindByName(column = "sepal.width")
    protected double sepalWidth;
    @CsvBindByName(column = "petal.length")
    protected double petalLength;
    @CsvBindByName(column = "petal.width")
    protected double petalWidth;
    @CsvBindByName(column = "variety")
    protected String variety;

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

    public String getVariety() {
        return this.variety;
    }

}
