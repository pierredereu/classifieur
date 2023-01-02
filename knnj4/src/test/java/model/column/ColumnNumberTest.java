package model.column;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dataset.Dataset;
import model.dataset.IDataset;
import model.point.IPoint;
import model.point.Iris;
import model.point.Passenger;

public class ColumnNumberTest {
    protected ColumnNumberAttribute type;
    protected String irisDatasetName;
    protected Dataset irisDataset;
    protected List<IPoint> irisList = new ArrayList<>();
    protected String passengerDatasetName;
    protected IDataset passengerDataset;
    protected List<IPoint> passengerList = new ArrayList<>();
    protected Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");

    @BeforeEach
    public void setup() {
        EasyRandom generator = new EasyRandom();

        irisDatasetName = "IRIS";
        for (int i = 0; i < 3; i++) {
            Iris iris = generator.nextObject(Iris.class);
            irisList.add(iris);
        }
        irisDataset = new Dataset(irisList, irisDatasetName);

        passengerDatasetName = "PASSENGER";
        for (int i = 0; i < 3; i++) {
            Passenger passenger = generator.nextObject(Passenger.class);
            passengerList.add(passenger);
        }
        passengerDataset = new Dataset(passengerList, passengerDatasetName);
    }

    @Test
    public void test() {
        type = ColumnNumberAttribute.PASSENGER_AGE;
        ColumnNumber cn = new ColumnNumber(type, passengerDatasetName, passengerDataset, suspectDataset);

        assertEquals(((Passenger) passengerList.get(0)).getPassengerAge(),
                cn.getColumnValues().get(passengerList.get(0)));
    }

    @Test
    public void should_get_iris_value_with_column() {
        type = ColumnNumberAttribute.PETAL_LENGTH;
        ColumnNumber cn = new ColumnNumber(type, irisDatasetName, irisDataset, suspectDataset);
        for (int i = 0; i < irisList.size(); i++) {
            Iris irisToTest = (Iris) irisList.get(i);
            assertEquals(irisToTest.getPetalLength(), irisToTest.getValue(cn));
        }
    }

    @Test
    public void should_get_iris_normalized_value_with_column() {
        type = ColumnNumberAttribute.PETAL_LENGTH;
        ColumnNumber cn = new ColumnNumber(type, irisDatasetName, irisDataset, suspectDataset);
        List<Double> values = new ArrayList<>();
        for (IPoint point : irisList) {
            values.add((Double) point.getValue(cn));
        }

        Double maxValueTraingDataset = Collections.max(values);
        Double minValueTrainingDataset = Collections.min(values);
        for (int i = 0; i < irisList.size(); i++) {
            double max = maxValueTraingDataset;
            double min = minValueTrainingDataset;
            Iris irisToTest = (Iris) irisList.get(i);
            if (irisToTest.getPetalLength() < min)
                min = irisToTest.getPetalLength();
            if (irisToTest.getPetalLength() > max)
                max = irisToTest.getPetalLength();
            Double irisValueNormalized = (irisToTest.getPetalLength() - min) / (max - min);
            assertEquals(irisValueNormalized, irisToTest.getNormalizedValue(cn));
        }

    }

    @Test
    public void should_return_Column_name() {
        type = ColumnNumberAttribute.PASSENGER_AGE;
        String setName = "testName";
        ColumnNumber cn = new ColumnNumber(type, setName, passengerDataset, suspectDataset);
        assertEquals("testName", cn.getName());
    }

    @Test
    public void should_return_Column_dataSet() {
        type = ColumnNumberAttribute.PASSENGER_AGE;
        ColumnNumber cn = new ColumnNumber(type, irisDatasetName, passengerDataset, suspectDataset);
        assertEquals(passengerDataset, cn.getTrainingDataset());
    }

}
