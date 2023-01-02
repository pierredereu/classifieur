package model.normalizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.column.ColumnNumber;
import model.dataset.IDataset;
import model.point.IPoint;

public class NumberNormalizer implements IValueNormalizer{
    protected ColumnNumber column;
    protected double minValue;
    protected double maxValue;

    public NumberNormalizer(ColumnNumber column) {
        this.column = column;
        setMinValue();
        setMaxValue();
    }


    @Override
    public double normalize(Object value) {
        double val = ((Number) value).doubleValue();
        if(isSmallerThanMinColumn(val)){
            return ((val -  val) / (maxValue - val));
        }else if(isGreaterThanMaxColumn(val)){
            return ((val -  minValue) / (val - minValue));
        }else{
            return ((val -  minValue) / (maxValue - minValue));
        }
    }

    @Override
    public Object denormalize(double value) {
        if(isSmallerThanMinColumn(value)){
            return (Number) ( (value * (minValue - value)) + value);
        }else if(isGreaterThanMaxColumn(value)){
            return (Number) ( (value * value - (minValue)) + minValue);
        }else{
            return (Number) (value * (maxValue - (minValue)) + minValue);
        }
    }

    protected List<Number> getColumnTrainingValues(){
        IDataset dataset = column.getTrainingDataset();
        Iterator<IPoint> iterator = dataset.iterator();
        List<Number> values = new ArrayList<>();
        while(iterator.hasNext()){
            Number value = (Number) iterator.next().getValue(column);
            values.add(value);
        }
        return values;
    }

    public void setMaxValue(){
        List<Number> values = getColumnTrainingValues();
        Number max = Double.NEGATIVE_INFINITY;
        for(Number nb : values){
            if(nb.doubleValue() > max.doubleValue()) max = nb;
        }
        maxValue = max.doubleValue();
    }

    public void setMinValue(){
        List<Number> values = getColumnTrainingValues();
        Number min = Double.POSITIVE_INFINITY;
        for(Number nb : values){
            if(nb.doubleValue() < min.doubleValue()) min = nb;
        }
        minValue = min.doubleValue();
    }

    public boolean isSmallerThanMinColumn(double value){
        return value < minValue;
    }

    public boolean isGreaterThanMaxColumn(double value){
        return value > maxValue;
    }


    public ColumnNumber getColumn() {
        return this.column;
    }

    public void setColumn(ColumnNumber column) {
        this.column = column;
    }

    public double getMinValue() {
        return this.minValue;
    }


    public double getMaxValue() {
        return this.maxValue;
    }


    
}
