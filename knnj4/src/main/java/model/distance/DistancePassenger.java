package model.distance;

import java.util.List;

import model.column.ColumnNumber;
import model.column.ColumnString;
import model.point.Passenger;

public class DistancePassenger extends Distance<Passenger> {
    protected ColumnNumber classColumn;
    protected ColumnNumber ageColumn;
    protected ColumnNumber sibspColumn;
    protected ColumnNumber parchColumn;
    protected ColumnNumber ticketPriceColumn;
    protected ColumnString sexColumn;
    protected ColumnString cabinColumn;
    protected ColumnString portEmbarkationColumn;

    public DistancePassenger(boolean isNormalized, DistanceAlgorithm algorithm, ColumnNumber classColumn,
            ColumnNumber ageColumn, ColumnNumber sibspColumn, ColumnNumber parchColumn, ColumnNumber ticketPriceColumn,
            ColumnString sexColumn, ColumnString cabinColumn, ColumnString portEmbarkationColumn) {
        this.isNormalized.setValue(isNormalized);
        this.algorithmUsed.setValue(algorithm);
        this.classColumn = classColumn;
        this.ageColumn = ageColumn;
        this.sibspColumn = sibspColumn;
        this.parchColumn = parchColumn;
        this.ticketPriceColumn = ticketPriceColumn;
        this.sexColumn = sexColumn;
        this.cabinColumn = cabinColumn;
        this.portEmbarkationColumn = portEmbarkationColumn;
    }

    @Override
    public double distance(Passenger passenger1, Passenger passenger2) {
        switch (algorithmUsed.getValue()) {
            case MANHATTAN:
                return distanceManhattan(passenger1, passenger2);
            case RANDOM:
                return distanceRandom();
            default:
                return distanceEuclidean(passenger1, passenger2);
        }
    }

    protected double distanceEuclidean(Passenger passenger1, Passenger passenger2) {
        if (isNormalized.getValue())
            return distanceEuclideanNormalized(passenger1, passenger2);
        return distanceEuclideanNotNormalized(passenger1, passenger2);
    }

    protected double distanceManhattan(Passenger passenger1, Passenger passenger2) {
        if (isNormalized.getValue())
            return distanceManhattanNormalized(passenger1, passenger2);
        return distanceManhattanNotNormalized(passenger1, passenger2);
    }

    @Override
    public double distanceManhattanNormalized(Passenger passenger1, Passenger passenger2) {
        List<ColumnNumber> columnsNumber = List.of(classColumn, ageColumn, sibspColumn, parchColumn, ticketPriceColumn);
        double res = 0;
        for (ColumnNumber column : columnsNumber) {
            res += Math.pow((Math.abs(passenger1.getNormalizedValue(column) - passenger2.getNormalizedValue(column))),
                    getTypeCoefficientNumberColumn(column));
        }

        res = addStringColumnsDistanceValues(passenger1, passenger2, res);
        return res;
    }

    @Override
    public double distanceManhattanNotNormalized(Passenger passenger1, Passenger passenger2) {
        List<ColumnNumber> columnsNumber = List.of(classColumn, ageColumn, sibspColumn, parchColumn, ticketPriceColumn);
        double res = 0;
        for (ColumnNumber column : columnsNumber) {
            res += Math.pow((Math.abs(value(passenger1, column) - value(passenger2, column))),
                    getTypeCoefficientNumberColumn(column));
        }

        res = addStringColumnsDistanceValues(passenger1, passenger2, res);

        return res;
    }

    @Override
    public double distanceEuclideanNormalized(Passenger passenger1, Passenger passenger2) {
        List<ColumnNumber> columnsNumber = List.of(classColumn, ageColumn, sibspColumn, parchColumn, ticketPriceColumn);
        double res = 0;
        for (ColumnNumber column : columnsNumber) {
            res += Math.pow(
                    (Math.pow(passenger1.getNormalizedValue(column) - passenger2.getNormalizedValue(column), 2)),
                    getTypeCoefficientNumberColumn(column));
        }
        res = addStringColumnsDistanceValues(passenger1, passenger2, res);
        return Math.sqrt(res);
    }

    @Override
    public double distanceEuclideanNotNormalized(Passenger passenger1, Passenger passenger2) {
        List<ColumnNumber> columnsNumber = List.of(classColumn, ageColumn, sibspColumn, parchColumn, ticketPriceColumn);
        double res = 0;
        for (ColumnNumber column : columnsNumber) {
            res += Math.pow((Math.pow(value(passenger1, column) - value(passenger2, column), 2)),
                    getTypeCoefficientNumberColumn(column));
        }
        res = addStringColumnsDistanceValues(passenger1, passenger2, res);
        return Math.sqrt(res);
    }

    protected double getStringDistance(String string1, String string2) {
        if (string1.equals(string2))
            return 0;
        return 1;
    }

    protected double value(Passenger p, ColumnNumber column) {
        Number value = (Number) p.getValue(column);
        return value.doubleValue();
    }

    protected double getTypeCoefficientNumberColumn(ColumnNumber column) {
        if (isNormalized.getValue())
            return column.getType().getCoefficientForDistanceNormalized();
        return column.getType().getCoefficientForDistanceNotNormalized();
    }

    protected double getTypeCoefficientStringColumn(ColumnString column) {
        if (isNormalized.getValue())
            return column.getType().getCoefficientForDistanceNormalized();
        return column.getType().getCoefficientForDistanceNotNormalized();
    }

    protected double addStringColumnsDistanceValues(Passenger passenger1, Passenger passenger2, double res) {
        res += Math.pow(getStringDistance(passenger1.getPassengerSex().getCsvString(),
                passenger2.getPassengerSex().getCsvString()), getTypeCoefficientStringColumn(sexColumn));

        res += Math.pow(getStringDistance(passenger1.getPassengerCabin(), passenger2.getPassengerCabin()),
                getTypeCoefficientStringColumn(cabinColumn));

        res += Math.pow(
                getStringDistance(passenger1.getPassengerPortOfEmbarkation(),
                        passenger2.getPassengerPortOfEmbarkation()),
                getTypeCoefficientStringColumn(portEmbarkationColumn));
        return res;
    }
}
