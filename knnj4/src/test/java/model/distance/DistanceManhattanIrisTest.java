package model.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.column.IColumn;
import model.dataset.Dataset;
import model.point.IPoint;
import model.point.Iris;

public class DistanceManhattanIrisTest {
    List<IPoint> listIris = new ArrayList<>();
    Iris iris1;
    Iris iris2;
    Iris iris3;
    DistanceIris distance;
    ColumnNumber sepalLengthColumn;
    ColumnNumber sepalWidthColumn;
    ColumnNumber petalLengthColumn;
    ColumnNumber petalWidthColumn;
    ColumnNumberAttribute attribute;
    Dataset dataset;
    Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");


    @BeforeEach
    public void setup() {
        EasyRandom generator = new EasyRandom();
        for (int i = 0; i < 3; i++) {
            Iris iris = generator.nextObject(Iris.class);
            listIris.add(iris);
        }

        iris1 = (Iris) listIris.get(0);
        iris2 = (Iris) listIris.get(1);
        iris3 = (Iris) listIris.get(2);

        dataset = new Dataset(listIris, "IRIS");

        attribute = ColumnNumberAttribute.SEPAL_LENGTH;
        sepalLengthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

        attribute = ColumnNumberAttribute.SEPAL_WIDTH;
        sepalWidthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        petalLengthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

        attribute = ColumnNumberAttribute.PETAL_WIDTH;
        petalWidthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

    }

    @Test
    public void should_get_distance_iris_not_normalised() {
        distance = new DistanceIris(false,DistanceAlgorithm.MANHATTAN, sepalLengthColumn, sepalWidthColumn, petalLengthColumn,
                petalWidthColumn);

        double distanceExpected = calculDistanceNotNormalized(iris1, iris2);
        assertEquals(distanceExpected, distance.distanceManhattanNotNormalized(iris1, iris2), 1E-5 * distanceExpected);
    }

    @Test
    public void should_get_iris_distance_normalised() {
        distance = new DistanceIris(true,DistanceAlgorithm.MANHATTAN, sepalLengthColumn, sepalWidthColumn, petalLengthColumn,
                petalWidthColumn);

        double distanceExpected = calculDistanceNormalized(iris1, iris2);
        assertEquals(distanceExpected, distance.distanceManhattanNormalized(iris1, iris2), 1E-5 * distanceExpected);
    }

    @Test
    public void should_get_iris_distance_normalized_or_not_thanks_to_parameter() {
        distance = new DistanceIris(false, DistanceAlgorithm.MANHATTAN,sepalLengthColumn, sepalWidthColumn, petalLengthColumn,
                petalWidthColumn);

        double distanceExpected = calculDistanceNotNormalized(iris1, iris2);
        assertEquals(distanceExpected, distance.distance(iris1, iris2), 1E-5 * distanceExpected);

        distance = new DistanceIris(true, DistanceAlgorithm.MANHATTAN,sepalLengthColumn, sepalWidthColumn, petalLengthColumn,
                petalWidthColumn);

        distanceExpected = calculDistanceNormalized(iris1, iris2);
        assertEquals(distanceExpected, distance.distanceManhattanNormalized(iris1, iris2), 1E-5 * distanceExpected);

    }

    protected double calculDistanceNotNormalized(Iris iris1, Iris iris2) {
        List<IColumn> columns = List.of(sepalLengthColumn, sepalWidthColumn, petalLengthColumn, petalWidthColumn);
        double res = 0;
        for (IColumn column : columns) {
            res += Math.pow((Math.abs((double) iris1.getValue(column) - (double) iris2.getValue(column))),
                    getTypeCoefficient((ColumnNumber) column));
        }
        return res;
    }

    protected double calculDistanceNormalized(Iris iris1, Iris iris2) {
        List<IColumn> columns = List.of(sepalLengthColumn, sepalWidthColumn, petalLengthColumn, petalWidthColumn);
        double res = 0;
        for (IColumn column : columns) {
            res += Math.pow((Math.abs(iris1.getNormalizedValue(column) - iris2.getNormalizedValue(column))),
                    getTypeCoefficient((ColumnNumber) column));
        }
        return res;
    }

    protected double getTypeCoefficient(ColumnNumber column) {
        if(distance.isNormalized.getValue()) return column.getType().getCoefficientForDistanceNormalized();
        return column.getType().getCoefficientForDistanceNotNormalized();
    }
}
