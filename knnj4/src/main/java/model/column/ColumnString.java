package model.column;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import model.dataset.IDataset;
import model.point.IPoint;

public class ColumnString implements IColumn{
	protected ColumnStringAttribute type;
    protected String name;
    protected IDataset trainingDataset;
	protected IDataset suspectDataset;
	

	public ColumnString(ColumnStringAttribute type, String name, IDataset dataset, IDataset suspectDataset) {
		this.type = type;
		this.name = name;
		this.trainingDataset = dataset;
		this.suspectDataset = suspectDataset;
	}

	public Map<IPoint,Object> getColumnValues(){
		Map<IPoint,Object> res = new HashMap<>();
		Iterator<IPoint> iterator = trainingDataset.iterator();
		while(iterator.hasNext()) {
			IPoint currentPoint = iterator.next();
			res.put(currentPoint, type.columnTypeToValue(currentPoint));
		}
		return res;
    }
	
	
	
	/** 
	 * @param point
	 * @return -1 because we can't normalize this column
	 */
	@Override
	public double getNormalizedValue(IPoint point) {
		return -1;
	}

	
	/** 
	 * @param value
	 * @return null because we can't normalize this column
	 */
	@Override
	public Object getDenormalizedValue(double value) {
		return null;
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
		return false;
	}

	public ColumnStringAttribute getType(){
		return this.type;
	}
}
