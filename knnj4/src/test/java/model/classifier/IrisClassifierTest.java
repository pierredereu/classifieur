package model.classifier;

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
import model.input.IrisCSVReader;
import model.point.IPoint;
import model.point.Iris;
import model.point.IrisVariety;

public class IrisClassifierTest {
    protected List<IPoint> irisList;
    protected Iris iris1;
    protected Iris iris2;
    protected Iris iris3;
    protected Iris iris4;
    protected Iris iris5;
    protected Iris iris6;
    protected Dataset trainingDataset;
    protected Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");
    protected DistanceIris distance;
    protected ColumnNumber sepalLengthColumn;
    protected ColumnNumber sepalWidthColumn;
    protected ColumnNumber petalLengthColumn;
    protected ColumnNumber petalWidthColumn;
    protected ColumnNumberAttribute attribute;
    protected IrisClassifier classifier;

    @BeforeEach
    public void setup(){
        irisList = new ArrayList<>();
        iris1 = new Iris(4.4, 3.0, 1.0, 0.5, "Setosa");

        iris2 = new Iris(4.9, 2.4, 1.4, 0.3, "Setosa");

        iris3 = new Iris(5.0, 3.0, 3.4, 1.5, "Versicolor");

        iris4 = new Iris(5.8, 2.9, 4.0, 1.5, "Versicolor");

        iris5 = new Iris(6.7, 3.0, 6.0, 2.3, "Virginica");

        iris6 = new Iris(6.8, 3.2, 5.9, 2.5, "Virginica");

        irisList.add(iris1);
        irisList.add(iris2);
        irisList.add(iris3);
        irisList.add(iris4);
        irisList.add(iris5);
        irisList.add(iris6);


        trainingDataset = new Dataset(irisList,"IRIS");

        attribute = ColumnNumberAttribute.SEPAL_LENGTH;
        sepalLengthColumn = new ColumnNumber(attribute, attribute.toString(), trainingDataset, suspectDataset);
        
        attribute = ColumnNumberAttribute.SEPAL_WIDTH;
        sepalWidthColumn = new ColumnNumber(attribute, attribute.toString(), trainingDataset, suspectDataset);
       
        attribute = ColumnNumberAttribute.PETAL_LENGTH;
        petalLengthColumn = new ColumnNumber(attribute, attribute.toString(), trainingDataset, suspectDataset);
        
        attribute = ColumnNumberAttribute.PETAL_WIDTH;
        petalWidthColumn = new ColumnNumber(attribute, attribute.toString(), trainingDataset, suspectDataset);

        distance = new DistanceIris(true,DistanceAlgorithm.MANHATTAN, sepalLengthColumn, sepalWidthColumn, petalLengthColumn, petalWidthColumn);
        classifier = new IrisClassifier(distance, trainingDataset);

    }

    

    @Test
    public void should_get_dataset_points(){
        assertEquals(irisList, classifier.getDataset().getPoints());
    }

    @Test
    public void should_get_all_distance_between_iris_and_training_set_iris(){
        Iris irisSuspect = new Iris(4.0, 2.6, 1.3, 0.6, "");
        Map<Iris,Double> map = classifier.getDistanceBetweenSuspectAndDatasetTraining(irisSuspect);
        assertEquals(6, map.size());
        assertTrue(map.containsKey(iris2));
        assertFalse(map.containsKey(irisSuspect));
    }

    @Test
    public void should_get_knn_smaller_iris_with_specific_k(){
        classifier.setK(2);
        Iris smallerIris = new Iris(4.0, 2.6, 1.3, 0.6, "");
        List<Iris> expectedIrisForSmallerIrisWithK2 = new ArrayList<>();
        expectedIrisForSmallerIrisWithK2.add(iris2);
        expectedIrisForSmallerIrisWithK2.add(iris1);
        assertEquals(expectedIrisForSmallerIrisWithK2, classifier.getKnnPoint(smallerIris));
    }

    @Test
    public void should_get_knn_larger_iris_with_specific_k(){
        classifier.setK(2);
        List<Iris> expectedIrisForLargerIrisWithK2 = new ArrayList<>();
        expectedIrisForLargerIrisWithK2.add(iris5);
        expectedIrisForLargerIrisWithK2.add(iris6);
        Iris largerIris = new Iris(5, 2.5, 6.2, 2.5, "");
        assertEquals(expectedIrisForLargerIrisWithK2, classifier.getKnnPoint(largerIris));
    }

    @Test
    public void should_get_knn_standart_iris_with_specific_k(){
        classifier.setK(2);
        List<Iris> expectedIrisForStandardIrisWithK2 = new ArrayList<>();
        expectedIrisForStandardIrisWithK2.add(iris3);
        expectedIrisForStandardIrisWithK2.add(iris4);
        Iris standardIris = new Iris(5.2, 4.8, 3, 1.9, "");
        assertEquals(expectedIrisForStandardIrisWithK2, classifier.getKnnPoint(standardIris));
    }

    @Test
    public void should_get_variety_classification(){
        classifier.setK(2);

        Iris smallerIris = new Iris(0.3, 1, 0.5, 0.2, "");
        assertEquals(IrisVariety.SETOSA,classifier.getClassification(smallerIris));

        Iris largerIris = new Iris(5, 6.5, 4, 5.8, "");
        assertEquals(IrisVariety.VERSICOLOR, classifier.getClassification(largerIris));

        Iris standardIris = new Iris(2.2, 2.8, 2, 2.9, "");
        assertEquals(IrisVariety.VERSICOLOR,classifier.getClassification(standardIris));
    }
}
