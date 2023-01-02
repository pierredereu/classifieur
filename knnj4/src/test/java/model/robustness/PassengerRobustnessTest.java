package model.robustness;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.classifier.PassengerClassifier;
import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.column.ColumnString;
import model.column.ColumnStringAttribute;
import model.dataset.Dataset;
import model.distance.DistanceAlgorithm;
import model.distance.DistancePassenger;
import model.factory.PassengerFactory;
import model.input.PassengerBrut;
import model.input.PassengerCSVReader;
import model.point.IPoint;
import model.point.Passenger;

public class PassengerRobustnessTest {
    protected List<Passenger> trainingDatasetList;
    protected Dataset dataset;
    protected DistancePassenger distance;
    protected PassengerClassifier classifier;
    protected PassengerRobustness robustness;

    protected ColumnNumber classColumn;
    protected ColumnNumber ageColumn;
    protected ColumnNumber sibspColumn;
    protected ColumnNumber parchColumn;
    protected ColumnNumber ticketPriceColumn;
    protected ColumnString sexColumn;
    protected ColumnString cabinColumn;
    protected ColumnString portEmbarkationColumn;
    protected ColumnNumberAttribute numberAttribute;
    protected ColumnStringAttribute stringAttribute;
    Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");

    @BeforeEach
    public void setup() throws IllegalStateException, IOException {
        PassengerCSVReader passengerCSVReader = PassengerCSVReader.getInstance();
        List<Passenger> list = new ArrayList<>();
        List<PassengerBrut> dataBrut = passengerCSVReader.readCSV("data/titanic/titanic.csv");
        PassengerFactory factory = PassengerFactory.getInstance();
        for (PassengerBrut data : dataBrut) {
            list.add(factory.createPoint(data));
        }
        List<Passenger> passengers = list.subList(0, 450);
        dataset = new Dataset(PassengersToPoints(passengers), "TRAINING_DATASET");

        numberAttribute = ColumnNumberAttribute.PASSENGER_CLASS;
        classColumn = new ColumnNumber(numberAttribute, numberAttribute.toString(), dataset, suspectDataset);

        numberAttribute = ColumnNumberAttribute.PASSENGER_AGE;
        ageColumn = new ColumnNumber(numberAttribute, numberAttribute.toString(), dataset, suspectDataset);

        numberAttribute = ColumnNumberAttribute.SIBSP;
        sibspColumn = new ColumnNumber(numberAttribute, numberAttribute.toString(), dataset, suspectDataset);

        numberAttribute = ColumnNumberAttribute.PARCH;
        parchColumn = new ColumnNumber(numberAttribute, numberAttribute.toString(), dataset, suspectDataset);

        numberAttribute = ColumnNumberAttribute.PASSENGER_TICKET_PRICE;
        ticketPriceColumn = new ColumnNumber(numberAttribute, numberAttribute.toString(), dataset, suspectDataset);

        stringAttribute = ColumnStringAttribute.PASSENGER_SEX;
        sexColumn = new ColumnString(stringAttribute, numberAttribute.toString(), dataset, suspectDataset);

        stringAttribute = ColumnStringAttribute.PASSENGER_CABIN;
        cabinColumn = new ColumnString(stringAttribute, numberAttribute.toString(), dataset, suspectDataset);

        stringAttribute = ColumnStringAttribute.PASSENGER_PORT_OF_EMBARKATION;
        portEmbarkationColumn = new ColumnString(stringAttribute, numberAttribute.toString(), dataset, suspectDataset);

        distance = new DistancePassenger(true, DistanceAlgorithm.MANHATTAN, classColumn, ageColumn, sibspColumn,
                parchColumn, ticketPriceColumn, sexColumn, cabinColumn, portEmbarkationColumn);
        classifier = new PassengerClassifier(distance, dataset);
        robustness = new PassengerRobustness(distance, dataset);
    }

    public List<IPoint> PassengersToPoints(List<Passenger> passengers) {
        List<IPoint> list = new ArrayList<>();
        for (Passenger p : passengers) {
            list.add((IPoint) p);
        }
        return list;
    }

    @Test
    public void should_get_robustness_superior_65percents() {
        int relevantLimitK = 30;
        for (int i = 0; i < relevantLimitK; i++) {
            assertEquals(100, robustness.getRobustnessValue(i), 35);
        }
    }

    @Test
    public void should_get_max_robustness_superior_75percents() {
        assertEquals(100, robustness.getMaxRobustnessValue(), 25);
    }

    @Test
    public void should_get_best_k() {
        assertEquals(24, robustness.computeBestK());
        assertEquals(77, robustness.getRobustnessValue(24), 1);
    }

}
