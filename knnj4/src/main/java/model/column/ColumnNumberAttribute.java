package model.column;

import model.point.IPoint;
import model.point.Iris;
import model.point.Passenger;

interface TransformToNumber{
    public Number columnTypeToValue(IPoint point);
}

public enum ColumnNumberAttribute {
    PASSENGER_ID(v -> ((Passenger) v).getPassengerID() , DataType.PASSENGER , 100, 0),
    PASSENGER_CLASS(v -> ((Passenger) v).getPassengerClass() , DataType.PASSENGER , 8, 2),
    SIBSP(v -> ((Passenger) v).getPassengerNumberOfSiblingAndSpousesAboard() , DataType.PASSENGER , 8, 2),
    PARCH(v -> ((Passenger) v).getPassengerNumberOfParentsAndChildrenAboard() , DataType.PASSENGER , 8 , 2),
    PASSENGER_AGE(v -> ((Passenger) v).getPassengerAge(), DataType.PASSENGER , 2 , 8),
    PASSENGER_TICKET_PRICE(v -> ((Passenger) v).getPassengerTicketPrice(), DataType.PASSENGER , 10 , 1),
    SEPAL_LENGTH(v -> ((Iris) v).getSepalLength(), DataType.IRIS, 10, 1),
    SEPAL_WIDTH(v -> ((Iris) v).getSepalWidth() , DataType.IRIS , 10 , 1),
    PETAL_LENGTH(v -> ((Iris) v).getPetalLength() , DataType.IRIS , 1 , 10),
    PETAL_WIDTH(v -> ((Iris) v).getPetalWidth() , DataType.IRIS , 7 , 3);

    TransformToNumber value;
    DataType dataType;
    double coefficientForDistanceNormalized;
    double coefficientForDistanceNotNormalized;


    private ColumnNumberAttribute(TransformToNumber value, DataType dataType, double coefficientForDistanceNormalized, double coefficientForDistanceNotNormalized){
        this.value=value;
        this.dataType = dataType;
        this.coefficientForDistanceNormalized = coefficientForDistanceNormalized;
        this.coefficientForDistanceNotNormalized = coefficientForDistanceNotNormalized;
    }  

    public Number columnTypeToValue(IPoint point){
        return value.columnTypeToValue(point);
    }

    public DataType getDataType(){
        return this.dataType;
    }

    public double getCoefficientForDistanceNormalized(){
        return this.coefficientForDistanceNormalized;
    }

    public double getCoefficientForDistanceNotNormalized(){
        return this.coefficientForDistanceNotNormalized;
    }
}
