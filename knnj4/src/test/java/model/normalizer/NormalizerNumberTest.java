package model.normalizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.dataset.Dataset;
import model.dataset.IDataset;
import model.point.IPoint;
import model.point.Iris;
import model.point.Passenger;

public class NormalizerNumberTest {
    protected ColumnNumberAttribute attribute;
    protected ColumnNumber columnNumber;
    protected NumberNormalizer numberNormalizer;
    protected String irisDatasetName;
    protected IDataset irisDataset;
    protected List<IPoint> irisList = new ArrayList<>();
    protected String passengerDatasetName;
    protected IDataset passengerDataset;
    protected List<IPoint> passengerList = new ArrayList<>();
    Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");

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
    public void should_get_column_values() {
        attribute = ColumnNumberAttribute.SEPAL_LENGTH;
        columnNumber = new ColumnNumber(attribute, attribute.toString(), irisDataset, suspectDataset);
        numberNormalizer = new NumberNormalizer(columnNumber);
        List<Number> numbers = numberNormalizer.getColumnTrainingValues();
        System.out.println(numbers);
        assertEquals(List.of(0.7231742029971469, 0.8058695140834087, 0.7962609718390335), numbers);
    }

    @Test
    public void should_return_normalize_value_petal_length_iris() {
        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        columnNumber = new ColumnNumber(attribute, attribute.toString(), irisDataset, suspectDataset);
        numberNormalizer = new NumberNormalizer(columnNumber);

        for (int i = 0; i < irisList.size(); i++) {
            Iris currentIris = (Iris) irisList.get(i);
            Double irisNormalizedValue = numberNormalizer.normalize(currentIris.getValue(columnNumber));
            assertEquals(currentIris.getNormalizedValue(columnNumber), irisNormalizedValue);
        }
    }

    @Test
    public void should_return_normalize_value_id_passenger() {
        attribute = ColumnNumberAttribute.PASSENGER_ID;
        columnNumber = new ColumnNumber(attribute, attribute.toString(), passengerDataset, suspectDataset);
        numberNormalizer = new NumberNormalizer(columnNumber);

        assertNotNull(numberNormalizer.getColumnTrainingValues());
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < passengerList.size(); i++) {
            list.add((int) passengerList.get(i).getValue(columnNumber));
        }
        assertEquals(list, numberNormalizer.getColumnTrainingValues());

        for (int i = 0; i < passengerList.size(); i++) {
            Passenger currentPassenger = (Passenger) passengerList.get(i);
            Number value = attribute.columnTypeToValue(currentPassenger);

            double passengerNormalizedValue = numberNormalizer.normalize(value.doubleValue());
            assertEquals(currentPassenger.getNormalizedValue(columnNumber), passengerNormalizedValue);
        }

    }

    @Test
    public void is_smaller_than_min_column_value_test() {
        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        columnNumber = new ColumnNumber(attribute, irisDatasetName, irisDataset, suspectDataset);
        numberNormalizer = new NumberNormalizer(columnNumber);
        double minColumn = numberNormalizer.getMaxValue();
        assertTrue(numberNormalizer.isSmallerThanMinColumn(minColumn - 1));
        assertFalse(numberNormalizer.isSmallerThanMinColumn(minColumn + 1));
    }

    @Test
    public void is_greater_than_max_column_value_test() {
        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        columnNumber = new ColumnNumber(attribute, irisDatasetName, irisDataset, suspectDataset);
        numberNormalizer = new NumberNormalizer(columnNumber);
        double maxColumn = numberNormalizer.getMinValue();
        assertTrue(numberNormalizer.isGreaterThanMaxColumn(maxColumn + 1));
        assertFalse(numberNormalizer.isGreaterThanMaxColumn(maxColumn - 1));
    }

    @Test
    public void should_return_normalize_value_when_value_is_smaller() {
        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        columnNumber = new ColumnNumber(attribute, irisDatasetName, irisDataset, suspectDataset);
        numberNormalizer = new NumberNormalizer(columnNumber);
        double minColumn = numberNormalizer.getMinValue();
        Iris irisPetalLengthSmaller = new Iris(4.0, 3.0, minColumn - 0.1, 3.0, "");
        assertEquals(0.0, numberNormalizer.normalize(irisPetalLengthSmaller.getPetalLength()));
    }

    @Test
    public void should_return_normalize_value_when_value_is_bigger() {
        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        columnNumber = new ColumnNumber(attribute, irisDatasetName, irisDataset, suspectDataset);
        numberNormalizer = new NumberNormalizer(columnNumber);
        double maxColumn = numberNormalizer.getMaxValue();
        Iris irisPetalLengthBigger = new Iris(4.0, 3.0, maxColumn + 1, 3.0, irisDatasetName);
        assertTrue(numberNormalizer.isGreaterThanMaxColumn(maxColumn + 1));
        assertEquals(1.0, numberNormalizer.normalize(irisPetalLengthBigger.getPetalLength()));
    }
}
