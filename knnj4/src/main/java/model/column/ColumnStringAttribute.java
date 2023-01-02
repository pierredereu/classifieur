package model.column;

import model.point.IPoint;
import model.point.Passenger;


interface TransformToString{
    public String columnTypeToValue(IPoint point);
}

public enum ColumnStringAttribute {
    PASSENGER_NAME (v -> ((Passenger) v).getPassengerName() , DataType.PASSENGER , 100 , 0),
    PASSENGER_SEX(v -> ((Passenger) v).getPassengerSex().getCsvString() , DataType.PASSENGER , 1 , 10),
    PASSENGER_TICKET_LABEL(v -> ((Passenger) v).getPassengerTicketLabel() , DataType.PASSENGER, 100 , 0),
    PASSENGER_CABIN(v -> ((Passenger) v).getPassengerCabin() , DataType.PASSENGER, 10, 1),
    PASSENGER_PORT_OF_EMBARKATION(v -> ((Passenger) v).getPassengerPortOfEmbarkation() , DataType.PASSENGER, 10, 1);

    TransformToString value;
    DataType dataType;
    double coefficientForDistanceNormalized;
    double coefficientForDistanceNotNormalized;

    private ColumnStringAttribute(TransformToString value, DataType dataType, double coeffDistNorm, double coeffDistNotNorm){
        this.value=value;
        this.dataType = dataType;
        this.coefficientForDistanceNormalized = coeffDistNorm;
        this.coefficientForDistanceNotNormalized = coeffDistNotNorm;
    }  

    public String columnTypeToValue(IPoint point){
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
