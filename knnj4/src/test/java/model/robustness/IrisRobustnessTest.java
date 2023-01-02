package model.robustness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.classifier.IrisClassifier;
import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.dataset.Dataset;
import model.distance.Distance;
import model.distance.DistanceAlgorithm;
import model.distance.DistanceIris;
import model.factory.IrisFactory;
import model.input.IrisBrut;
import model.input.IrisCSVReader;
import model.point.IPoint;
import model.point.Iris;
import model.robustness.IrisRobustness;

public class IrisRobustnessTest {
    protected List<Iris> trainingDatasetList = new ArrayList<>();
    protected Dataset dataset;
    protected DistanceIris distance;
    protected ColumnNumber sepalLengthColumn;
    protected ColumnNumber sepalWidthColumn;
    protected ColumnNumber petalLengthColumn;
    protected ColumnNumber petalWidthColumn;
    protected ColumnNumberAttribute attribute;
    protected IrisClassifier classifier;
    IrisRobustness robustness;
    Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");

    @BeforeEach
    public void setup() throws IllegalStateException, IOException {
        IrisCSVReader reader = IrisCSVReader.getInstance();
        List<IrisBrut> dataBrut = reader.readCSV("data/iris/iris.csv");
        IrisFactory factory = IrisFactory.getInstance();
        for (IrisBrut data : dataBrut) {
            trainingDatasetList.add(factory.createPoint(data));
        }
        List<IPoint> irisTrainingToPoints = new ArrayList<>(trainingDatasetList);
        dataset = new Dataset(irisTrainingToPoints, "IRIS");

        attribute = ColumnNumberAttribute.SEPAL_LENGTH;
        sepalLengthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

        attribute = ColumnNumberAttribute.SEPAL_WIDTH;
        sepalWidthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        petalLengthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

        attribute = ColumnNumberAttribute.PETAL_WIDTH;
        petalWidthColumn = new ColumnNumber(attribute, attribute.toString(), dataset, suspectDataset);

        distance = new DistanceIris(true, DistanceAlgorithm.MANHATTAN, sepalLengthColumn, sepalWidthColumn,
                petalLengthColumn, petalWidthColumn);
        classifier = new IrisClassifier(distance, dataset);
        robustness = new IrisRobustness(distance, dataset);
    }

    @Test
    public void should_get_robustness_superior_75percents() {
        int relevantLimitK = 30;
        for (int i = 0; i < relevantLimitK; i++) {
            assertEquals(100, robustness.getRobustnessValue(i), 25);
        }
    }

    @Test
    public void should_get_max_robustness_superior_90percents() {
        assertEquals(100, robustness.getMaxRobustnessValue());
    }

    @Test
    public void should_get_best_k() {
        assertEquals(4, robustness.computeBestK());
        assertEquals(4, robustness.computeBestK());
        assertEquals(100, robustness.getRobustnessValue(4));
    }
}
