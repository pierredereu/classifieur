package model.distance;

import java.util.List;

import model.column.ColumnNumber;
import model.column.IColumn;
import model.point.Iris;

public class DistanceIris extends Distance<Iris> {
    protected ColumnNumber sepalLengthColumn;
    protected ColumnNumber sepalWidthColumn;
    protected ColumnNumber petalLengthColumn;
    protected ColumnNumber petalWidthColumn;

    public DistanceIris(boolean isNormalized, DistanceAlgorithm algorithm, ColumnNumber sepalLengthColumn,
            ColumnNumber sepalWidthColumn, ColumnNumber petalLengthColumn, ColumnNumber petalWidthColumn) {
        this.isNormalized.setValue(isNormalized);
        this.algorithmUsed.setValue(algorithm);
        this.sepalLengthColumn = sepalLengthColumn;
        this.sepalWidthColumn = sepalWidthColumn;
        this.petalLengthColumn = petalLengthColumn;
        this.petalWidthColumn = petalWidthColumn;
    }

    @Override
    public double distance(Iris iris1, Iris iris2) {
        switch (algorithmUsed.getValue()) {
            case MANHATTAN:
                return distanceManhattan(iris1, iris2);
            case RANDOM:
                return distanceRandom();
            default:
                return distanceEuclidean(iris1, iris2);
        }
    }

    protected double distanceEuclidean(Iris iris1, Iris iris2) {
        if (isNormalized.getValue())
            return distanceEuclideanNormalized(iris1, iris2);
        return distanceEuclideanNotNormalized(iris1, iris2);
    }

    protected double distanceManhattan(Iris iris1, Iris iris2) {
        if (isNormalized.getValue())
            return distanceManhattanNormalized(iris1, iris2);
        return distanceManhattanNotNormalized(iris1, iris2);
    }

    @Override
    public double distanceManhattanNormalized(Iris iris1, Iris iris2) {
        List<IColumn> columns = List.of(sepalLengthColumn, sepalWidthColumn, petalLengthColumn, petalWidthColumn);
        double res = 0;
        for (IColumn column : columns) {
            res += Math.pow((Math.abs(iris1.getNormalizedValue(column) - iris2.getNormalizedValue(column))),
                    getTypeCoefficient((ColumnNumber) column));
        }
        return res;
    }

    @Override
    public double distanceManhattanNotNormalized(Iris iris1, Iris iris2) {
        List<IColumn> columns = List.of(sepalLengthColumn, sepalWidthColumn, petalLengthColumn, petalWidthColumn);
        double res = 0;
        for (IColumn column : columns) {
            res += Math.pow((Math.abs((double) iris1.getValue(column) - (double) iris2.getValue(column))),
                    getTypeCoefficient((ColumnNumber) column));
        }
        return res;
    }

    @Override
    public double distanceEuclideanNormalized(Iris iris1, Iris iris2) {
        List<IColumn> columns = List.of(sepalLengthColumn, sepalWidthColumn, petalLengthColumn, petalWidthColumn);
        double res = 0;
        for (IColumn column : columns) {
            res += Math.pow((Math.pow(iris1.getNormalizedValue(column) - iris2.getNormalizedValue(column), 2)),
                    getTypeCoefficient((ColumnNumber) column));
        }
        return Math.sqrt(res);
    }

    @Override
    public double distanceEuclideanNotNormalized(Iris iris1, Iris iris2) {
        List<IColumn> columns = List.of(sepalLengthColumn, sepalWidthColumn, petalLengthColumn, petalWidthColumn);
        double res = 0;
        for (IColumn column : columns) {
            res += Math.pow((Math.pow((double) iris1.getValue(column) - (double) iris2.getValue(column), 2)),
                    getTypeCoefficient((ColumnNumber) column));
        }
        return Math.sqrt(res);
    }

    protected double getTypeCoefficient(ColumnNumber column) {
        if (isNormalized.getValue())
            return column.getType().getCoefficientForDistanceNormalized();
        return column.getType().getCoefficientForDistanceNotNormalized();
    }

}
