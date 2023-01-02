package model.column;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.dataset.Dataset;
import model.dataset.IDataset;
import model.normalizer.NumberNormalizer;
import model.point.IPoint;

public class ColumnNumber implements IColumn {
    protected final ColumnNumberAttribute type;
    protected final String name;
    protected IDataset trainingDataset;
    protected IDataset suspectDataset;
    protected NumberNormalizer normalizer;

    public ColumnNumber(ColumnNumberAttribute type, String name, IDataset trainingDataset, IDataset suspectDataset) {
        super();
        this.type = type;
        this.name = name;
        this.trainingDataset = trainingDataset;
        this.suspectDataset = suspectDataset;
        this.normalizer = new NumberNormalizer(this);
    }

    @Override
    public Map<IPoint, Object> getColumnValues() {
        Map<IPoint, Object> res = new HashMap<>();
        Iterator<IPoint> iterator = trainingDataset.iterator();
        while (iterator.hasNext()) {
            IPoint currentPoint = iterator.next();
            res.put(currentPoint, type.columnTypeToValue(currentPoint));
        }

        iterator = suspectDataset.iterator();
        while(iterator.hasNext()){
            IPoint currentPoint = iterator.next();
            res.put(currentPoint, type.columnTypeToValue(currentPoint));
        }
        return res;
    }

    @Override
    public double getNormalizedValue(IPoint point) {
        Number value = type.columnTypeToValue(point);
        if(Double.NEGATIVE_INFINITY == normalizer.getMaxValue()){
            updateNormalizer();
          }   
        return normalizer.normalize(value);
    }

    public void updateNormalizer() {
        normalizer.setMaxValue();
        normalizer.setMinValue();
    }

    @Override
    public Object getDenormalizedValue(double value) {
        if(Double.NEGATIVE_INFINITY == normalizer.getMaxValue()){
            updateNormalizer();
          }   
        return normalizer.denormalize(value);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public IDataset getTrainingDataset() {
        return this.trainingDataset;
    }

    @Override
    public boolean isNormalizable() {
        return true;
    }

    public ColumnNumberAttribute getType() {
        return this.type;
    }

    public NumberNormalizer getNormalizer() {
        return this.normalizer;
    }

    public void setTrainingDataset(Dataset dataset) {
        this.trainingDataset = dataset;
    }

}
