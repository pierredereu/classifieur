package model.mainModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.IrisPlatformModel;
import model.PassengerPlatformModel;
import model.column.ColumnNumberAttribute;
import model.dataset.Dataset;
import model.distance.DistanceAlgorithm;
import model.point.IPoint;
import model.point.Iris;
import model.point.IrisVariety;

public class PlatformModelTest {
    IrisPlatformModel irisPlatform;
    IrisPlatformModel irisPlatform2;
    PassengerPlatformModel passengerPlatform;
    PassengerPlatformModel passengerPlatform2;
    Iris iris;
    private Dataset suspectDataset;
    private IPoint randomPoint;

    @BeforeEach
    public void setup() throws IllegalStateException, IOException {
        irisPlatform = new IrisPlatformModel();
        passengerPlatform = new PassengerPlatformModel();
        irisPlatform2 = new IrisPlatformModel();
        irisPlatform2.importTrainingDataset("data/iris/iris.csv");
        iris = new Iris(5.1, 3, 1, 0.2, "");
        irisPlatform2.addSuspectPoint(iris);
        irisPlatform2.importSuspectDataset("data/iris/irisSuspects.csv");

    }

    @Test
    public void should_initialize_iris_platform_axis() {
        assertEquals(ColumnNumberAttribute.PETAL_LENGTH, irisPlatform.getAxisX());
        assertEquals(ColumnNumberAttribute.PETAL_WIDTH, irisPlatform.getAxisY());
    }

    @Test
    public void should_initialize_iris_platform_dataset_empty() {
        assertEquals(0, irisPlatform.getSuspectDataset().getNbLines());
        assertNotEquals(null, irisPlatform.getSuspectDataset());
        assertEquals(0, irisPlatform.getTrainingDataset().getNbLines());
        assertNotEquals(null, irisPlatform.getTrainingDataset());

    }

    @Test
    public void should_initialize_iris_platform_classifier() {
        assertEquals(irisPlatform.getTrainingDataset(), irisPlatform.getClassifier().getDataset());
        assertEquals(4, irisPlatform.getClassifier().getK().getValue());
    }

    @Test
    public void should_clear_iris_platform_suspect_dataset() {
        irisPlatform.addSuspectPoint(new Iris(1, 1, 1, 2, ""));
        assertEquals(1, irisPlatform.getSuspectDataset().getNbLines());
        irisPlatform.clearSuspectDataset();
        assertEquals(0, irisPlatform.getSuspectDataset().getNbLines());
    }

    @Test
    public void should_import_training_dataset_iris_platform() throws IllegalStateException, IOException {
        assertEquals(0, irisPlatform.getTrainingDataset().getNbLines());
        irisPlatform.importTrainingDataset("data/iris/iris.csv");
        assertEquals(150, irisPlatform.getTrainingDataset().getNbLines());
        assertEquals(150, irisPlatform.getClassifier().getDataset().getNbLines());
        assertEquals(150, irisPlatform.getRobustness().getFullTrainingDataset().getNbLines());
    }

    @Test
    public void should_import_suspect_dataset_iris_platform() throws IllegalStateException, IOException {
        assertEquals(0, irisPlatform.getSuspectDataset().getNbLines());
        irisPlatform.importSuspectDataset("data/iris/irisSuspects.csv");
        assertEquals(13, irisPlatform.getSuspectDataset().getNbLines());
        assertEquals(0, irisPlatform.getTrainingDataset().getNbLines());
        assertEquals(0, irisPlatform.getClassifier().getDataset().getNbLines());
    }

    @Test
    public void should_get_classification_iris() throws IllegalStateException, IOException {
        assertEquals(IrisVariety.SETOSA, irisPlatform2.getClassificationPoint(iris));
    }

    @Test
    public void should_classify_suspects_iris() throws IllegalStateException, IOException {
        Random r = new Random();
        suspectDataset = irisPlatform2.getSuspectDataset();
        int randomInt = r.nextInt(suspectDataset.getNbLines() - 1);
        assertEquals(IrisVariety.UNKNOWN, iris.getClassification());
        Iris randomIris = (Iris) suspectDataset.getPoints().get(randomInt);
        assertEquals(IrisVariety.UNKNOWN, randomIris.getClassification());
        irisPlatform2.classify();
        assertEquals(IrisVariety.SETOSA, iris.getClassification());
        assertNotEquals(IrisVariety.UNKNOWN, randomIris.getClassification());
    }

    @Test
    public void should_get_best_k_iris_platform() {
        irisPlatform2.findBestK();
        assertEquals(4, irisPlatform2.getBestK().getValue());
    }

    @Test
    public void should_get_robustness_for_k_iris_platform() {
        assertEquals(100, irisPlatform2.getRobustnessValue(4));
        assertEquals(100, irisPlatform2.getRobustnessValue(5));
        assertEquals(93, irisPlatform2.getRobustnessValue(6), 1);

    }

    @Test
    public void should_get_max_robustness_iris_platform() {
        assertEquals(100, irisPlatform2.getMaxRobustnessValue());
    }

    @Test
    public void should_set_distance_algorithm_iris_platform() {
        assertEquals(DistanceAlgorithm.MANHATTAN, irisPlatform2.getDistanceUsed().getAlgorithmUsed().getValue());
        irisPlatform2.setDistanceAlgorithm(DistanceAlgorithm.EUCLIDEAN);
        assertEquals(DistanceAlgorithm.EUCLIDEAN, irisPlatform2.getDistanceUsed().getAlgorithmUsed().getValue());
    }

    @Test
    public void should_initialize_passenger_platform_axis() {
        assertEquals(ColumnNumberAttribute.PASSENGER_AGE, passengerPlatform.getAxisX());
        assertEquals(ColumnNumberAttribute.PARCH, passengerPlatform.getAxisY());
    }

    @Test
    public void should_initialize_passenger_platform_dataset_empty() {
        assertEquals(0, passengerPlatform.getSuspectDataset().getNbLines());
        assertNotEquals(null, passengerPlatform.getSuspectDataset());
        assertEquals(0, passengerPlatform.getTrainingDataset().getNbLines());
        assertNotEquals(null, passengerPlatform.getTrainingDataset());
    }

    @Test
    public void should_change_distance_not_normalise() {
        irisPlatform2.setDistanceNormalized(false);
        assertEquals(false, irisPlatform2.isDistanceNormalized().getValue());
        irisPlatform2.classify();
    }

}
