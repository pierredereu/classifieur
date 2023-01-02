package model.classifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.column.ColumnString;
import model.column.ColumnStringAttribute;
import model.dataset.Dataset;
import model.distance.Distance;
import model.distance.DistanceAlgorithm;
import model.distance.DistancePassenger;
import model.point.IPoint;
import model.point.Passenger;
import model.point.PassengerSex;

public class PassengerClassifierTest {
    protected List<IPoint> passengerList;
    protected Passenger passenger1;
    protected Passenger passenger2;
    protected Passenger passenger3;
    protected Passenger passenger4;
    protected Passenger passenger5;
    protected Dataset dataset;
    protected Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");
    protected DistancePassenger distance;
    protected ColumnNumber idColumn;
    protected ColumnNumber classColumn;
    protected ColumnNumber sibspColumn;
    protected ColumnNumber parchColumn;
    protected ColumnNumber ageColumn;
    protected ColumnNumber ticketPriceColumn;
    protected ColumnNumberAttribute columnNumberAttribute;
    protected ColumnString sexColumn;
    protected ColumnString cabineColumn;
    protected ColumnString portOfEmbarkationColumn;
    protected ColumnStringAttribute columnStringAttribute;
    protected PassengerClassifier classifier;

    @BeforeEach
    public void setup(){
        passengerList = new ArrayList<>();
        passenger1 = new Passenger(1, 1, "P1", "male", 20.0, 1, 0, "", 10, "C1", "C", 0);

        passenger2 = new Passenger(2, 1, "P2", "male", 21, 0, 1, "", 8, "C6", "C", 0);

        passenger3 = new Passenger(3, 2, "P3", "female", 30, 2, 0, "", 12, "C1", "C", 0);

        passenger4 = new Passenger(4, 2, "P4", "female", 40, 0, 0, "", 13, "C9", "S", 1);

        passenger5 = new Passenger(5, 3, "P5", "female", 50, 0, 3, "", 10, "C2", "C", 1);

        passengerList.add(passenger1);
        passengerList.add(passenger2);
        passengerList.add(passenger3);
        passengerList.add(passenger4);
        passengerList.add(passenger5);

        dataset = new Dataset(passengerList,"PASSENGER");

        columnNumberAttribute = ColumnNumberAttribute.PASSENGER_ID;
        idColumn = new ColumnNumber(columnNumberAttribute, columnNumberAttribute.toString(), dataset, suspectDataset);

        columnNumberAttribute = ColumnNumberAttribute.PASSENGER_AGE;
        ageColumn = new ColumnNumber(columnNumberAttribute, columnNumberAttribute.toString(), dataset, suspectDataset);
        
        columnNumberAttribute = ColumnNumberAttribute.PASSENGER_CLASS;
        classColumn = new ColumnNumber(columnNumberAttribute, columnNumberAttribute.toString(), dataset, suspectDataset);
       
        columnNumberAttribute = ColumnNumberAttribute.SIBSP;
        sibspColumn = new ColumnNumber(columnNumberAttribute, columnNumberAttribute.toString(), dataset, suspectDataset);
        
        columnNumberAttribute = ColumnNumberAttribute.PARCH;
        parchColumn = new ColumnNumber(columnNumberAttribute, columnNumberAttribute.toString(), dataset, suspectDataset);

        columnNumberAttribute = ColumnNumberAttribute.PASSENGER_TICKET_PRICE;
        ticketPriceColumn = new ColumnNumber(columnNumberAttribute, columnNumberAttribute.toString(), dataset, suspectDataset);

        columnStringAttribute = ColumnStringAttribute.PASSENGER_SEX;
        sexColumn = new ColumnString(columnStringAttribute, columnStringAttribute.toString(), dataset, suspectDataset);

        columnStringAttribute = ColumnStringAttribute.PASSENGER_CABIN;
        cabineColumn = new ColumnString(columnStringAttribute, columnStringAttribute.toString(), dataset, suspectDataset);

        columnStringAttribute = ColumnStringAttribute.PASSENGER_PORT_OF_EMBARKATION;
        portOfEmbarkationColumn = new ColumnString(columnStringAttribute, columnStringAttribute.toString(), dataset, suspectDataset);


        distance = new DistancePassenger(true,DistanceAlgorithm.MANHATTAN, classColumn, ageColumn, sibspColumn, parchColumn, ticketPriceColumn, sexColumn, portOfEmbarkationColumn, portOfEmbarkationColumn);

        classifier = new PassengerClassifier(distance, dataset);
    }

    @Test
    public void should_get_dataset_points(){
        assertEquals(passengerList, classifier.getDatasetPoints());
    }

    @Test
    public void should_get_all_distance_between_passenger_and_training_set_passenger(){
        Passenger passengerSuspect = new Passenger(10, 2, "P10", "male", 0, 18, 1, "", 12.5, "C5", "S", 0);

        Map<Passenger,Double> map = classifier.getDistanceBetweenSuspectAndDatasetTraining(passengerSuspect);
        assertEquals(5, map.size());
        assertTrue(map.containsKey(passenger2));
        assertFalse(map.containsKey(passengerSuspect));
    }

    
}
