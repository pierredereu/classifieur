package model.distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.column.ColumnNumber;
import model.column.ColumnNumberAttribute;
import model.column.ColumnString;
import model.column.ColumnStringAttribute;
import model.dataset.Dataset;
import model.point.IPoint;
import model.point.Passenger;

public class DistanceManhattanPassengerTest {
    List<IPoint> listPassenger = new ArrayList<>();
    Passenger passenger1;
    Passenger passenger2;
    Passenger passenger3;
    DistancePassenger distance;
    ColumnNumber classColumn;
    ColumnNumber ageColumn;
    ColumnNumber sibspColumn;
    ColumnNumber parchColumn;
    ColumnNumber ticketPriceColumn;
    ColumnString sexColumn;
    ColumnString cabinColumn;
    ColumnString portEmbarkationColumn;
    ColumnNumberAttribute numberAttribute;
    ColumnStringAttribute stringAttribute;
    Dataset dataset;
    Dataset suspectDataset = new Dataset(new ArrayList<>(), "suspects");



    @BeforeEach
    public void setup(){
        EasyRandom generator = new EasyRandom();
        for(int i = 0 ; i < 3 ; i++){
            Passenger Passenger = generator.nextObject(Passenger.class);
            listPassenger.add(Passenger);
        }

        passenger1 = (Passenger) listPassenger.get(0);
        passenger2 = (Passenger) listPassenger.get(1);
        passenger3 = (Passenger) listPassenger.get(2);

        dataset = new Dataset(listPassenger, "PASSENGER");

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
        sexColumn = new ColumnString(stringAttribute, stringAttribute.toString(), dataset, suspectDataset);

        stringAttribute = ColumnStringAttribute.PASSENGER_CABIN;
        cabinColumn = new ColumnString(stringAttribute, stringAttribute.toString(), dataset, suspectDataset);

        stringAttribute = ColumnStringAttribute.PASSENGER_PORT_OF_EMBARKATION;
        portEmbarkationColumn = new ColumnString(stringAttribute, stringAttribute.toString(), dataset, suspectDataset);
        
    }

    @Test
    public void should_get_distance_passenger_not_normalised(){
        distance = new DistancePassenger(false,DistanceAlgorithm.MANHATTAN, classColumn, ageColumn, sibspColumn, parchColumn, ticketPriceColumn, sexColumn, cabinColumn, portEmbarkationColumn);
        
        double distanceExpected =
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger2, classColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger2, ageColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger2, sibspColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger2, parchColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger2, ticketPriceColumn) +
            stringDistance(passenger1, passenger2, sexColumn) +
            stringDistance(passenger1, passenger2, cabinColumn) +
            stringDistance(passenger1, passenger2, portEmbarkationColumn);
        assertEquals(distanceExpected, distance.distanceManhattanNotNormalized(passenger1, passenger2) , 1E-5 * distanceExpected);

        distanceExpected =
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger3, classColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger3, ageColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger3, sibspColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger3, parchColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger1, passenger3, ticketPriceColumn) +
            stringDistance(passenger1, passenger3, sexColumn) +
            stringDistance(passenger1, passenger3, cabinColumn) +
            stringDistance(passenger1, passenger3, portEmbarkationColumn);
        assertEquals(distanceExpected, distance.distanceManhattanNotNormalized(passenger1, passenger3) , 1E-5 * distanceExpected);

        distanceExpected =
            calculDistanceNotNormalizedAttributeIsolated(passenger2, passenger3, classColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger2, passenger3, ageColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger2, passenger3, sibspColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger2, passenger3, parchColumn) +
            calculDistanceNotNormalizedAttributeIsolated(passenger2, passenger3, ticketPriceColumn) +
            stringDistance(passenger2, passenger3, sexColumn) +
            stringDistance(passenger2, passenger3, cabinColumn) +
            stringDistance(passenger2, passenger3, portEmbarkationColumn);
        assertEquals(distanceExpected, distance.distanceManhattanNotNormalized(passenger2, passenger3) , 1E-5 * distanceExpected);
    }

    @Test
    public void should_get_passenger_distance_normalised(){
        distance = new DistancePassenger(true, DistanceAlgorithm.MANHATTAN,classColumn, ageColumn, sibspColumn, parchColumn, ticketPriceColumn, sexColumn, cabinColumn, portEmbarkationColumn);
        
        double distanceExpected =
            (calculDistanceNormalizedAttributeIsolated(passenger1, passenger2, classColumn))+
            calculDistanceNormalizedAttributeIsolated(passenger1, passenger2, ageColumn) +
            calculDistanceNormalizedAttributeIsolated(passenger1, passenger2, sibspColumn) +
            calculDistanceNormalizedAttributeIsolated(passenger1, passenger2, parchColumn) +
            calculDistanceNormalizedAttributeIsolated(passenger1, passenger2, ticketPriceColumn) +
            stringDistance(passenger1, passenger2, sexColumn) +
            stringDistance(passenger1, passenger2, cabinColumn) +
            stringDistance(passenger1, passenger2, portEmbarkationColumn);
        assertEquals(distanceExpected, distance.distanceManhattanNormalized(passenger1, passenger2) , 1E-5 * distanceExpected);
    }

    protected double calculDistanceNotNormalizedAttributeIsolated(Passenger p1, Passenger p2, ColumnNumber column){
        Number value1 = (Number) p1.getValue(column);
        Number value2 = (Number) p2.getValue(column);
        if(distance.isNormalized.getValue()) return Math.pow(Math.abs(value1.doubleValue() - value2.doubleValue()), column.getType().getCoefficientForDistanceNormalized());

        return Math.pow(Math.abs(value1.doubleValue() - value2.doubleValue()), column.getType().getCoefficientForDistanceNotNormalized());
    }

    protected double calculDistanceNormalizedAttributeIsolated(Passenger p1, Passenger p2, ColumnNumber column){
        if(distance.isNormalized.getValue()) return Math.pow(Math.abs((double)p1.getNormalizedValue(column) - (double)p2.getNormalizedValue(column)), column.getType().getCoefficientForDistanceNormalized());

        return Math.pow(Math.abs((double)p1.getNormalizedValue(column) - (double)p2.getNormalizedValue(column)), column.getType().getCoefficientForDistanceNotNormalized());
    }

    protected double stringDistance(Passenger p1, Passenger p2, ColumnString column){
        if(p1.getValue(column).equals(p2.getValue(column))) return 0;
        return Math.pow(1, column.getType().getCoefficientForDistanceNormalized());
    }
}
