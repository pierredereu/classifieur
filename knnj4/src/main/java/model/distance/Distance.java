package model.distance;

import java.util.Random;

import model.point.IPoint;
import utils.ObservableProperty;

public abstract class Distance<PointType extends IPoint> {
    protected ObservableProperty<Boolean> isNormalized = new ObservableProperty<>();
    protected ObservableProperty<DistanceAlgorithm> algorithmUsed = new ObservableProperty<>();
    protected Random random = new Random();

    public abstract double distance(PointType instance1, PointType instance2);

    public abstract double distanceManhattanNormalized(PointType instance1, PointType instance2);

    public abstract double distanceManhattanNotNormalized(PointType instance1, PointType instance2);

    public abstract double distanceEuclideanNormalized(PointType instance1, PointType instance2);

    public abstract double distanceEuclideanNotNormalized(PointType instance1, PointType instance2);

    public double distanceRandom() {
        return random.nextDouble();
    }

    public ObservableProperty<DistanceAlgorithm> getAlgorithmUsed() {
        return this.algorithmUsed;
    }

    public void setDistanceAlgorithm(DistanceAlgorithm algorithm) {
        this.algorithmUsed.setValue(algorithm);
    }

    public ObservableProperty<Boolean> isDistanceNormalized() {
        return this.isNormalized;
    }

    public void setDistanceNormalize(Boolean value) {
        this.isNormalized.setValue(value);
    }
}
